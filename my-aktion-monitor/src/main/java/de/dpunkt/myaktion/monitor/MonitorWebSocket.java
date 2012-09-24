package de.dpunkt.myaktion.monitor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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

@WebSocket(path = "/spende", remote = SpendeRemote.class)
public class MonitorWebSocket {

	private static int instanceCounter = 0;

	@WebSocketContext
	public EndpointContext wsc; // Muss public sein!

	private static MonitorWebSocket _instance = null;
	
	static {
	    //for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){
 
	        public boolean verify(String hostname,
	                javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals("localhost")) {
	                return true;
	            }
	            return false;
	        }
	        
	    });
	}

	public MonitorWebSocket() {
		instanceCounter++;
		System.out.println("SpendeWebSocket instance number: "
				+ instanceCounter);
		_instance = this;
	}

	public static MonitorWebSocket getInstance() {
		return _instance;
	}

	@WebSocketOpen
	public void init(SpendeRemote remote) {
		System.out.println("Peer entered conversation: " + remote);
		Client client = ClientFactory.newClient();
		client.configuration().register(SpendeListMBR.class);
        WebTarget target = client.target("http://localhost:8180/my-aktion/rest/spende/list/11");
        GenericType<List<Spende>> list = new GenericType<List<Spende>>() {};
        List<Spende> result = target.request(MediaType.APPLICATION_JSON).get(list);
        for (Spende spende : result) {
			sendSpende(spende);
		}
	}

	@WebSocketClose
	public void destroy(SpendeRemote remote) {
		System.out.println("Peer leaved conversation: " + remote);
	}

	@SuppressWarnings("rawtypes")
	public void sendSpende(Spende spende) {
		if (wsc == null)
			System.out.println("wsc == null");
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
	public String echo(String message) {
		return message + " (vom Server)";
	}
}
