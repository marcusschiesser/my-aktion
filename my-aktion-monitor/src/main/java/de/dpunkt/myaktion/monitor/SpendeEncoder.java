package de.dpunkt.myaktion.monitor;

import java.text.NumberFormat;

import javax.websocket.Encoder;

import de.dpunkt.myaktion.model.Spende;

public class SpendeEncoder extends Encoder.Adapter implements Encoder.Text<Spende> {

	@Override
	public String encode(Spende spende) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		String betrag = format.format(spende.getBetrag());
		Object args[] = {spende.getSpenderName(), betrag};
		return String.format("%s hat %s gespendet.", args);
	}

}
