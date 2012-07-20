package de.dpunkt.myaktion.test;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Konto;
import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.Spende.Status;

public class TestDataFactory {

	public static Aktion createTestAktion() {
		Aktion aktion = new Aktion();
		aktion.setName("Trikots für A-Jugend");
		aktion.setSpendenZiel(1000d);
		aktion.setBisherGespendet(258d);
		aktion.setSpendenBetrag(20d);
		aktion.setKonto(new Konto("Max Mustermann", "ABC Bank", "100200300",
				"12345678"));
		return aktion;
	}
	
	public static Spende createTestSpende() {
		Spende spende = new Spende();
		spende.setSpenderName("Heinz Schmidt");
		spende.setBetrag(20d);
		spende.setQuittung(true);
		spende.setStatus(Status.UEBERWIESEN);
		spende.setKonto(new Konto(spende.getSpenderName(), "XXX Bank",
				"123456", "87654321"));
		return spende;
	}

}
