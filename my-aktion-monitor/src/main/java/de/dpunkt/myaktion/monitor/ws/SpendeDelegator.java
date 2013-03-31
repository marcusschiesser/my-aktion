package de.dpunkt.myaktion.monitor.ws;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.websocket.EncodeException;
import javax.websocket.Session;

import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.monitor.MonitorWebSocket;

@WebService
public class SpendeDelegator {

    private Logger logger = Logger.getLogger(SpendeDelegator.class.getName());

    @WebMethod
    public void sendSpende(Long aktionId, Spende spende) {
	// Spende an alle Clients senden, die für diese Aktion registriert sind
	for (Session session : MonitorWebSocket.getSessions()) {
	    // Aktion ID besorgen, für die der aktuelle Client registriert ist
	    Long clientAktionId = (Long) session.getUserProperties().get(
		    MonitorWebSocket.AKTION_ID);
	    if (aktionId.equals(clientAktionId)) {
		try {
		    session.getBasicRemote().sendObject(spende);
		} catch (IOException | EncodeException e) {
		    logger.log(Level.INFO, "Keine Verbindung zu Client: "
			    + session, e);
		}
	    }
	}
    }

}
