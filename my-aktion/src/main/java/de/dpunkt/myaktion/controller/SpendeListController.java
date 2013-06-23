package de.dpunkt.myaktion.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Spende.Status;
import de.dpunkt.myaktion.services.SpendeService;

@RequestScoped
@Named
public class SpendeListController {
	private Aktion aktion;
	
	@Inject
	private SpendeService spendeService;

	public Aktion getAktion() {
		return aktion;
	}

	public void setAktion(Aktion aktion) {
		aktion.setSpenden(spendeService.getSpendeList(aktion.getId()));
		this.aktion = aktion;
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
