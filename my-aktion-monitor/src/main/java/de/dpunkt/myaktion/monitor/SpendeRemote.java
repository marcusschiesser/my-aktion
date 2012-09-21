package de.dpunkt.myaktion.monitor;

import java.io.IOException;

import org.glassfish.websocket.api.ConversionException;
import org.glassfish.websocket.api.Peer;
import org.glassfish.websocket.api.annotations.WebSocketRemote;

import de.dpunkt.myaktion.model.Spende;

@WebSocketRemote(encoders={SpendeEncoder.class})
public interface SpendeRemote extends Peer {
	public void sendMessage(Spende spende) throws IOException,
			ConversionException;
}
