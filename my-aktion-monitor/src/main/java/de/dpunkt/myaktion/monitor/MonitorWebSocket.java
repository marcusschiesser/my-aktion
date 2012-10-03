package de.dpunkt.myaktion.monitor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.websocket.api.ConversionException;
import org.glassfish.websocket.api.EndpointContext;
import org.glassfish.websocket.api.annotations.WebSocket;
import org.glassfish.websocket.api.annotations.WebSocketClose;
import org.glassfish.websocket.api.annotations.WebSocketContext;
import org.glassfish.websocket.api.annotations.WebSocketMessage;
import org.glassfish.websocket.api.annotations.WebSocketOpen;

import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.SpendeListMBR;
import de.dpunkt.myaktion.monitor.util.LocalHostnameVerifier;

@WebSocket(path = "/spende", remote = SpendeRemote.class)
public class MonitorWebSocket {

	private static final String REST_SPENDE_LIST = "https://localhost:8543/my-aktion/rest/spende/list/";

	private static int instanceCounter = 0;
	private static MonitorWebSocket _instance = null;

	@WebSocketContext
	public EndpointContext wsc; // Muss public sein!

	private Client restClient;
	private Map<SpendeRemote, Long> sessionMap;
	private Logger logger = Logger.getLogger(MonitorWebSocket.class.getName());

	public MonitorWebSocket() {
		instanceCounter++;
		System.out.println("SpendeWebSocket instance number: "
				+ instanceCounter);
		_instance = this;
		sessionMap = new ConcurrentHashMap<>();
		restClient = ClientFactory.newClient();
		restClient.configuration().register(SpendeListMBR.class);
		// erlaubt ausschliesslich localhost fuer SSL
		HttpsURLConnection
				.setDefaultHostnameVerifier(new LocalHostnameVerifier());
	}

	public static MonitorWebSocket getInstance() {
		return _instance;
	}

	@WebSocketOpen
	public void init(SpendeRemote remote) {
		logger.info("Client hat sich verbunden: " + remote);
	}

	@WebSocketMessage
	public String setAktionId(Long aktionId, SpendeRemote client) {
		// Pre-Condition: aktionId!=null, da die eingehende Nachricht
		// nach Typ an eine Methode weitergeleitet wird
		List<Spende> result = new LinkedList<>();
		try {
			result = getSpendeList(aktionId);
		} catch (NotFoundException e) {
			sessionMap.remove(client);
			return "Die Aktion mit der ID: " + aktionId
					+ " ist nicht verfügbar";
		} catch (WebApplicationException e) {
			sessionMap.remove(client);
			logger.log(Level.SEVERE, "Die Spendenliste für Aktion mit ID: " + aktionId + " konnte nicht abgerufen werden. Läuft der JBoss?", e);
			return "Fehler beim Abruf der initialen Spendenliste."; 
		}
		// Die Spendenliste wurde erfolgreich vom Server abgerufen,
		// daher kann der Client für die ausgewählte Aktion registriert werden
		sessionMap.put(client, aktionId);
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

	public List<Spende> getSpendeList(Long aktionId) throws NotFoundException {
		WebTarget target = restClient.target(REST_SPENDE_LIST + aktionId);
		GenericType<List<Spende>> list = new GenericType<List<Spende>>() {
		};
		List<Spende> result = target.request(MediaType.APPLICATION_JSON).get(
				list);
		return result;
	}

	@WebSocketClose
	public void destroy(SpendeRemote client) {
		logger.info("Client hat Verbindung getrennt: " + client);
		sessionMap.remove(client);
	}

	public void informClients(Spende spende, Long aktionId) {
		// Spende an alle Clients senden, die für diese Aktion registriert sind
		Set<SpendeRemote> clients = sessionMap.keySet();
		for (SpendeRemote client : clients) {
			if (sessionMap.get(client).equals(aktionId)) {
				try {
					client.sendMessage(spende);
				} catch (IOException | ConversionException e) {
					logger.log(Level.INFO, "Keine Verbindung zu Client: " + client, e);
				}
			}
		}
	}

}
