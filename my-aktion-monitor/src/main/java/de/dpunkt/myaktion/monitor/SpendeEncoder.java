package de.dpunkt.myaktion.monitor;

import java.text.NumberFormat;

import org.glassfish.websocket.api.TextEncoder;

import de.dpunkt.myaktion.model.Spende;

public class SpendeEncoder implements TextEncoder<Spende> {

	@Override
	public String encode(Spende spende) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		String betrag = format.format(spende.getBetrag());
		Object args[] = {spende.getSpenderName(), betrag};
		return String.format("%s hat %s gespendet.", args);
	}

}
