package de.dpunkt.myaktion.monitor;

import java.text.NumberFormat;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import de.dpunkt.myaktion.model.Spende;

public class SpendeEncoder implements Encoder.Text<Spende> {

    @Override
    public String encode(Spende spende) {
	NumberFormat format = NumberFormat.getCurrencyInstance();
	String betrag = format.format(spende.getBetrag());
	Object args[] = { spende.getSpenderName(), betrag };
	return String.format("%s hat %s gespendet.", args);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig arg0) {
    }

}
