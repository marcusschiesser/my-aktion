package de.dpunkt.myaktion.monitor;

import org.glassfish.websocket.api.TextEncoder;
import de.dpunkt.myaktion.model.Spende;

public class SpendeEncoder implements TextEncoder<Spende> {

	@Override
	public String encode(Spende spende) {
		StringBuffer res = new StringBuffer();
		res.append(spende.getSpenderName() + ": ").append(spende.getBetrag());
		return res.toString();
	}

}
