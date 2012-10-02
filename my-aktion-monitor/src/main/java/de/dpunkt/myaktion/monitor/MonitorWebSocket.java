package de.dpunkt.myaktion.monitor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.websocket.api.Conversation;
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

	private Long aktionId = null;

	@WebSocketContext
	public EndpointContext wsc; // Muss public sein!

	private Client client;

	private static MonitorWebSocket _instance = null;

	static {
		// erlaubt ausschliesslich localhost fuer SSL
		HttpsURLConnection
				.setDefaultHostnameVerifier(new LocalHostnameVerifier());
	}

	public MonitorWebSocket() {
		instanceCounter++;
		System.out.println("SpendeWebSocket instance number: "
				+ instanceCounter);
		_instance = this;
		client = ClientFactory.newClient();
		client.configuration().register(SpendeListMBR.class);
	}

	public static MonitorWebSocket getInstance() {
		return _instance;
	}

	@WebSocketOpen
	public void init(SpendeRemote remote) {
		System.out.println("Peer entered conversation: " + remote);
		updateSpendeList(this.aktionId);
	}

	public void updateSpendeList(Long aktionId) {
		if (aktionId != null) {
			WebTarget target = client.target(REST_SPENDE_LIST + aktionId);
			GenericType<List<Spende>> list = new GenericType<List<Spende>>() {
			};
			List<Spende> result = target.request(MediaType.APPLICATION_JSON)
					.get(list);
			for (Spende spende : result) {
				sendSpende(spende);
			}
		}
	}

	@WebSocketClose
	public void destroy(SpendeRemote remote) {
		System.out.println("Peer leaved conversation: " + remote);
	}

	@SuppressWarnings("rawtypes")
	public void sendSpende(Spende spende) {
		// Alle Browser informieren:
		Set<Conversation> conversations = wsc.getConversations();
		for (Conversation con : conversations) {
			SpendeRemote remote = (SpendeRemote) con.getPeer();
			try {
				remote.sendMessage(spende);
			} catch (IOException e) {
				System.out.println("No connection to: " + remote);
				e.printStackTrace();
			} catch (ConversionException e) {
				e.printStackTrace();
			}
		}
	}

	@WebSocketMessage
	public String setAktionId(Long aktionId) {
		try {
			updateSpendeList(aktionId);
			this.aktionId = aktionId;
			return "Aktion geändert zu: " + aktionId;
		} catch (NotFoundException e) {
			return "Die Aktion mit der ID: " + aktionId
					+ " ist nicht verfügbar";
		}
	}
}
