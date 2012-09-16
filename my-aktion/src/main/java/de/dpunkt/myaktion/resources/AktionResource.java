package de.dpunkt.myaktion.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.services.AktionService;

@Path("/organisator/aktion")
public class AktionResource {
	
	@Inject
	private AktionService aktionService;
	
	/**
	 * Um auf den Service zuzugreifen, zunächst gewünschte URL aufrufen (erzeugt Cookie):
	 * curl -k -c cookie.txt https://localhost:8443/my-aktion/rest/organisator/aktion/list 
	 * Dann Anmeldung durchführen:
	 * curl -k -c cookie.txt -b cookie.txt --data-urlencode j_username=max@mustermann.de -d j_password=secret https://localhost:8443/my-aktion/j_security_check
     * Mit dem Cookie, der eine Session repräsentiert, können dann die Service-Methoden aufgerufen werden, z.B.:
     * curl -k -b cookie.txt https://localhost:8443/my-aktion/rest/organisator/aktion/list
     * Der Cookie beschriebt die Session und kann für andere Abfragen verwendet werden
	 * @return
	 */
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Aktion> getAllAktionen() {
		List<Aktion> allAktionen = aktionService.getAllAktionen();
		// Spenden-Objekte sollen nicht übertragen werden (wurden auch nicht gefetched)
		// Organisator wird auch nicht übertragen
		for (Aktion aktion : allAktionen) {
			aktion.setSpenden(null);
			aktion.setOrganisator(null);
		}
		return allAktionen;
	}
	
	/**
	 * Zum Testen: curl -k -b cookie.txt -X DELETE https://localhost:8443/my-aktion/rest/organisator/aktion/24
	 */
	@DELETE
	@Path("/{aktionId}")
	public void deleteAktion(@PathParam(value="aktionId") Long aktionId) {
		aktionService.deleteAktion(aktionId);
	}

	/**
	 * curl -k -b cookie.txt -H "Content-Type: application/json" -X PUT --data "@test.json" https://localhost:8443/my-aktion/rest/organisator/aktion/11
	 * 
	 * Inhalt der Datei test.json
	 * {"name":"Frau Vogel","spendenZiel":112.0,"spendenBetrag":25.0}
	 */
	@PUT
	@Path("/{aktionId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Aktion updateAktion(@PathParam(value="aktionId") Long aktionId, 
			Aktion newAktion) {
		Aktion aktion = aktionService.getAktion(aktionId);
		// Nutzer darf nur name, betrag und ziel aktualisieren
		aktion.setName(newAktion.getName());
		aktion.setSpendenBetrag(newAktion.getSpendenBetrag());
		aktion.setSpendenZiel(newAktion.getSpendenZiel());
		return aktionService.updateAktion(aktion);
	}
	
	/**
	 * curl -k -b cookie.txt -H "Content-Type: application/json" -X POST --data "@test2.json" https://localhost:8443/my-aktion/rest/organisator/aktion
	 * 
	 * Inhalt der Datei test2.json
	 * {"name":"Test Aktion","spendenZiel":1120.0,"spendenBetrag":30.0,"konto":{"name":"Eva Mustermann","nameDerBank":"ABC Bank","kontoNr":"32432","blz":"12345678"}}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Aktion addAktion( 
			Aktion aktion) {
		return aktionService.addAktion(aktion);
	}

}
