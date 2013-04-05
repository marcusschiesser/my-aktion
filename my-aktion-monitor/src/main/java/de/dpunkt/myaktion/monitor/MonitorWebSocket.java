package de.dpunkt.myaktion.monitor;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import de.dpunkt.myaktion.model.Spende;

@ServerEndpoint(value = "/spende", encoders = { SpendeEncoder.class })
public class MonitorWebSocket {

    private Logger logger = Logger.getLogger(MonitorWebSocket.class.getName());

    public static final String AKTION_ID = "AktionId";

    private static Set<Session> sessions = Collections
	    .synchronizedSet(new HashSet<Session>());

    @Inject
    private SpendeListProvider spendeListProvider;

    public static Set<Session> getSessions() {
	return sessions;
    }

    @OnOpen
    public void onOpen(Session session) {
	logger.info("Client hat sich verbunden: " + session);
	sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
	logger.info("Client hat Verbindung getrennt: " + session);
	sessions.remove(session);
    }

    @OnMessage
    public void setAktionId(Long aktionId, Session session) {
	// Pre-Condition: aktionId!=null, da die eingehende Nachricht
	// nach Typ an eine Methode weitergeleitet wird
	logger.info("Client " + session.getId() + " hat Aktion " + aktionId
		+ " ausgewählt.");
	try {
	    List<Spende> result = new LinkedList<>();
	    try {
		result = spendeListProvider.getSpendeList(aktionId);
	    } catch (NotFoundException e) {
		session.getBasicRemote().sendText(
			"Die Aktion mit der ID: " + aktionId
				+ " ist nicht verfügbar");
	    } catch (WebApplicationException e) {
		logger.log(Level.SEVERE, "Die Spendenliste für Aktion mit ID: "
			+ aktionId
			+ " konnte nicht abgerufen werden. Läuft der JBoss?", e);
		session.getBasicRemote().sendText(
			"Fehler beim Abruf der initialen Spendenliste.");
	    }
	    // Die Spendenliste wurde erfolgreich vom Server abgerufen,
	    // daher kann der Client für die ausgewählte Aktion registriert
	    // werden
	    session.getUserProperties().put(AKTION_ID, aktionId);
	    // Die Spendenliste an den Client senden
	    for (Spende spende : result) {
		logger.info("Sende " + spende + " an Client " + session.getId());
		session.getBasicRemote().sendObject(spende);
	    }
	    session.getBasicRemote()
		    .sendText("Aktion geändert zu: " + aktionId);
	} catch (IOException | EncodeException e) {
	    logger.log(Level.INFO, "Keine Verbindung zu Client: " + session, e);
	}
    }

}
