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
import javax.ws.rs.core.Response;

import de.dpunkt.myaktion.model.Konto;
import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.Spende.Status;
import de.dpunkt.myaktion.services.SpendeService;
import de.dpunkt.myaktion.services.exceptions.ObjectNotFoundException;

@Path("/")
public class SpendeResource {
	
	@Inject
	private SpendeService spendeService;
	
	/**
	 * Vor dem Aufrufen erst Anmelden (siehe AktionResource), dann folgendes aufrufen:
	 * 
	 * curl -k -b cookie.txt https://localhost:8443/my-aktion/rest/organisator/spende/list/11
	 */
	@GET
	@Path("/organisator/spende/list/{aktionId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Spende> getSpendeList(@PathParam(value="aktionId") Long aktionId) {
		List<Spende> spenden = spendeService.getSpendeList(aktionId);
		// Die Aktion jeder Spende muss zurückgesetzt werden, sonst
		// kann die Spende wg. der bidirektionalen Relation nicht serialisiert werden
		for (Spende spende : spenden) {
			spende.setAktion(null);
		}
		return spenden;
	}

	/**
	 * wie getSpendeList, kann jedoch ohne Authentifizierung aufgerufen werden
	 */ 
	@GET
	@Path("/spende/list/{aktionId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSpendeListPublic(@PathParam(value="aktionId") Long aktionId) {
		List<Spende> spenden;
		try {
			spenden = spendeService.getSpendeListPublic(aktionId);
			return Response.ok(spenden).build();
		} catch (ObjectNotFoundException e) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * curl -k -d "spenderName=Hans Bier&betrag=10&blz=12345678&kontoNr=12&nameDerBank=ABC&quittung=false" https://localhost:8443/my-aktion/rest/spende/34
	 *   
	 */
	@POST
	@Path("/spende/{aktionId}")
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
		spende.setStatus(Status.IN_BEARBEITUNG);
		spendeService.addSpende(aktionId, spende);
	}

}
