package de.dpunkt.myaktion.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.dpunkt.myaktion.model.Konto;
import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.services.SpendeService;

@Path("/spende")
public class SpendeResource {
	
	@Inject
	private SpendeService spendeService;
	
	@GET
	@Path("/list/{aktionId}")
	@Produces("text/xml")
	public List<Spende> getSpendeList(@PathParam(value="aktionId") Long aktionId) {
		return spendeService.getSpendeList(aktionId);
	}
	
	@POST
	@Path("/{aktionId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addSpende(@PathParam(value="aktionId") Long aktionId, 
			@FormParam(value="spenderName") String spenderName, 
			@FormParam(value="betrag") Double betrag, 
			@FormParam(value="blz") String blz, 
			@FormParam(value="kontoNr") String kontoNr, 
			@FormParam(value="nameDerBank") String nameDerBank, 
			@FormParam(value="quittung") Boolean quittung) {
		Spende spende = new Spende();
		spende.setSpenderName(spenderName);
		spende.setBetrag(betrag);
		Konto konto = new Konto();
		konto.setBlz(blz);
		konto.setKontoNr(kontoNr);
		konto.setName(spenderName);
		konto.setNameDerBank(nameDerBank);
		spende.setKonto(konto);
		spende.setQuittung(quittung);
		spendeService.addSpende(aktionId, spende);
	}

}
