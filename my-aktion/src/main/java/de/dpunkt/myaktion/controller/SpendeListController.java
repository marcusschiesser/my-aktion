package de.dpunkt.myaktion.controller;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.Spende.Status;
import de.dpunkt.myaktion.services.ISpendeService;

@RequestScoped
@Named
public class SpendeListController {
	private Aktion aktion;
	private List<Spende> spenden;
	
	@Inject
	private ISpendeService spendeService;

	public List<Spende> getSpenden() {
		return spenden;
	}

	public void setSpenden(List<Spende> spenden) {
		this.spenden = spenden;
	}

	public Aktion getAktion() {
		return aktion;
	}

	public void setAktion(Aktion aktion) {
		this.aktion = aktion;
		spenden = spendeService.getSpendeList(aktion.getId());
		System.err.println();
	}

	public String doOk() {
		return Pages.AKTION_LIST;
	}
	
	public String convertStatus(Status status) {
		if(status==null)
			return "";
		switch(status) {
		case UEBERWIESEN:
			return "überwiesen";
		case IN_BEARBEITUNG:
			return "in Bearbeitung";
		default:
			return "";
		}
	}

}
