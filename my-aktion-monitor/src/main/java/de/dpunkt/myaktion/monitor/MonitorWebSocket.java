package de.dpunkt.myaktion.monitor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.glassfish.websocket.api.Conversation;
import org.glassfish.websocket.api.ConversionException;
import org.glassfish.websocket.api.EndpointContext;
import org.glassfish.websocket.api.annotations.WebSocket;
import org.glassfish.websocket.api.annotations.WebSocketClose;
import org.glassfish.websocket.api.annotations.WebSocketContext;
import org.glassfish.websocket.api.annotations.WebSocketMessage;
import org.glassfish.websocket.api.annotations.WebSocketOpen;

import de.dpunkt.myaktion.model.Spende;

@WebSocket(path = "/spende", remote = SpendeRemote.class)
public class MonitorWebSocket {

	private static final String AKTION_ID = "AktionId";

	// TODO: können entfernt werden, sobald CDI geht
	private static int instanceCounter = 0;
	private static MonitorWebSocket _instance = null;

	@WebSocketContext
	public EndpointContext wsc; // Muss public sein!

	private Logger logger = Logger.getLogger(MonitorWebSocket.class.getName());
	
	// TODO: Mit @Inject injizieren (geht aktuell nicht)
	private SpendeListProvider spendeListProvider;

	public MonitorWebSocket() {
		instanceCounter++;
		logger.info("SpendeWebSocket instance number: "
				+ instanceCounter);
		_instance = this;
		spendeListProvider = new SpendeListProvider();
	}

	public static MonitorWebSocket getInstance() {
		return _instance;
	}

	@WebSocketOpen
	public void init(SpendeRemote remote) {
		logger.info("Client hat sich verbunden: " + remote);
	}

	@WebSocketClose
	public void destroy(SpendeRemote client) {
		logger.info("Client hat Verbindung getrennt: " + client);
	}

	@SuppressWarnings("unchecked")
	@WebSocketMessage
	public String setAktionId(Long aktionId, SpendeRemote client) {
		// Pre-Condition: aktionId!=null, da die eingehende Nachricht
		// nach Typ an eine Methode weitergeleitet wird
		List<Spende> result = new LinkedList<>();
		try {
			result = spendeListProvider.getSpendeList(aktionId);
		} catch (NotFoundException e) {
			return "Die Aktion mit der ID: " + aktionId
					+ " ist nicht verfügbar";
		} catch (WebApplicationException e) {
			logger.log(Level.SEVERE, "Die Spendenliste für Aktion mit ID: " + aktionId + " konnte nicht abgerufen werden. Läuft der JBoss?", e);
			return "Fehler beim Abruf der initialen Spendenliste."; 
		}
		// Die Spendenliste wurde erfolgreich vom Server abgerufen,
		// daher kann der Client für die ausgewählte Aktion registriert werden
		client.getConversation().getProperties().put(AKTION_ID, aktionId);
		// Die Spendenliste an den Client senden
		try {
			for (Spende spende : result) {
				client.sendMessage(spende);
			}
		} catch (IOException | ConversionException e) {
			logger.log(Level.INFO, "Keine Verbindung zu Client: " + client, e);
			return ""; // Dummy, wird nicht zurückgegeben, da Verbindung nicht mehr vorhanden
		}
		return "Aktion geändert zu: " + aktionId;
	}

	// TODO: in @Observes Methode umwandeln (geht aktuell nicht)
	public void informClients(Spende spende, long aktionId) {
		// Spende an alle Clients senden, die für diese Aktion registriert sind
		@SuppressWarnings("rawtypes")
		Set<Conversation> conversations = wsc.getConversations();
		for (Conversation<SpendeRemote> conversation : conversations) {
			// Aktion ID besorgen, für die der aktuelle Client registriert ist
			Long clientAktionId = (Long) conversation.getProperties().get(AKTION_ID);
			if (aktionId == clientAktionId) {
				SpendeRemote client = conversation.getPeer();
				try {
					client.sendMessage(spende);
				} catch (IOException | ConversionException e) {
					logger.log(Level.INFO, "Keine Verbindung zu Client: " + client, e);
				}
			}
		}
	}

}
