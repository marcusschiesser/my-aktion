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

@Path("/aktion")
public class AktionResource {
	
	@Inject
	private AktionService aktionService;
	
	/**
	 * Um auf den Service zuzugreifen, zunächst anmelden:
	 * curl -c cookie.txt -b cookie.txt -k --data-urlencode j_username=max@mustermann.de -d j_password=secret https://localhost:8443/my-aktion/j_security_check
     * und dann über den Cookie abfragen:
     * curl -k -b cookie.txt https://localhost:8443/my-aktion/rest/aktion/list
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
	 * Zum Testen: curl -k -b cookie.txt -X DELETE https://localhost:8443/my-aktion/rest/aktion/24
	 */
	@DELETE
	@Path("/{aktionId}")
	public void deleteAktion(@PathParam(value="aktionId") Long aktionId) {
		aktionService.deleteAktion(aktionId);
	}

	@PUT
	@Path("/{aktionId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateAktion(@PathParam(value="aktionId") Long aktionId, 
			Aktion aktion) {
		// TODO: Nutzer darf nur name, betrag und ziel aktualisieren
		aktionService.updateAktion(aktion);
	}
	
	@POST
	@Path("/{aktionId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addAktion(@PathParam(value="aktionId") Long aktionId, 
			Aktion aktion) {
		aktionService.addAktion(aktion);
	}

}
