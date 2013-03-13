package de.dpunkt.myaktion.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import de.dpunkt.myaktion.model.Spende;

@ServerEndpoint(value = "/spende",
encoders = {
        SpendeEncoder.class
}
)
public class MonitorWebSocket {

	private static final String AKTION_ID = "AktionId";

	// TODO: können entfernt werden, sobald CDI geht
	private static int instanceCounter = 0;
	private static MonitorWebSocket _instance = null;

	private Logger logger = Logger.getLogger(MonitorWebSocket.class.getName());
	
	private List<Session> sessions = new ArrayList<Session>();
	
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

	@OnOpen
	public synchronized void init(Session remote) {
		logger.info("Client hat sich verbunden: " + remote);
		sessions.add(remote);
	}

	@OnClose
	public synchronized void destroy(Session session) {
		logger.info("Client hat Verbindung getrennt: " + session);
		sessions.remove(session);
	}

    @OnMessage
	public String setAktionId(Long aktionId, Session session) {
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
		session.getUserProperties().put(AKTION_ID, aktionId);
		// Die Spendenliste an den Client senden
		try {
			for (Spende spende : result) {
				session.getBasicRemote().sendObject(spende);
			}
		} catch (IOException | EncodeException e) {
			logger.log(Level.INFO, "Keine Verbindung zu Client: " + session, e);
			return ""; // Dummy, wird nicht zurückgegeben, da Verbindung nicht mehr vorhanden
		}
		return "Aktion geändert zu: " + aktionId;
	}

	// TODO: in @Observes Methode umwandeln (geht aktuell nicht)
	public void informClients(Spende spende, long aktionId) {
		// Spende an alle Clients senden, die für diese Aktion registriert sind
		for (Session session : sessions) {
			// Aktion ID besorgen, für die der aktuelle Client registriert ist
			Long clientAktionId = (Long) session.getUserProperties().get(AKTION_ID);
			if (aktionId == clientAktionId) {
				try {
					session.getBasicRemote().sendObject(spende);
				} catch (IOException | EncodeException e) {
					logger.log(Level.INFO, "Keine Verbindung zu Client: " + session, e);
				}
			}
		}
	}

}
