package de.dpunkt.myaktion.services;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Konto;
import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.Spende.Status;

@RequestScoped
@Alternative
public class MockAktionService implements IAktionService {

	@Override
	public List<Aktion> getAllAktionen() {

		Spende spende1 = createMockSpende();
		Spende spende2 = new Spende();
		spende2.setSpenderName("Karl Meier");
		spende2.setBetrag(30d);
		spende2.setQuittung(false);
		spende2.setStatus(Status.IN_BEARBEITUNG);
		spende2.setKonto(new Konto(spende2.getSpenderName(), "YYY Bank",
				"654321", "86427531"));
		List<Spende> spenden = new LinkedList<Spende>();
		spenden.add(spende1);
		spenden.add(spende2);

		Aktion aktion1 = createMockAktion();
		aktion1.setSpenden(spenden);

		Aktion aktion2 = new Aktion();
		aktion2.setName("Rollstuhl für Maria");
		aktion2.setSpendenZiel(2500d);
		aktion2.setBisherGespendet(742d);
		aktion2.setSpendenBetrag(25d);
		aktion2.setKonto(aktion1.getKonto());
		aktion2.setSpenden(spenden);

		List<Aktion> ret = new LinkedList<Aktion>();
		ret.add(aktion1);
		ret.add(aktion2);
		return ret;
	}

	@Override
	public void addAktion(Aktion aktion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAktion(Aktion aktion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAktion(Aktion aktion) {
		// TODO Auto-generated method stub
		
	}

	public static Aktion createMockAktion() {
		Aktion aktion = new Aktion();
		aktion.setName("Trikots für A-Jugend");
		aktion.setSpendenZiel(1000d);
		aktion.setBisherGespendet(258d);
		aktion.setSpendenBetrag(20d);
		aktion.setKonto(new Konto("Max Mustermann", "ABC Bank", "100200300",
				"12345678"));
		return aktion;
	}
	
	public static Spende createMockSpende() {
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
