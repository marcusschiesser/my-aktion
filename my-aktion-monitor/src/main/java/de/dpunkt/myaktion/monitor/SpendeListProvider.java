package de.dpunkt.myaktion.monitor;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.SpendeListMBR;
import de.dpunkt.myaktion.monitor.util.LocalHostnameVerifier;

/**
 * TODO: Alternative Implementierung, die mit JCache cached.
 * Diese Implementierung wird mit @Observes-Methode auf das
 * Event des SpendeDelegator hören, um den Cache zu aktualisieren
 * 
 * @author marcus
 */
public class SpendeListProvider {
	
	private static final String REST_SPENDE_LIST = "https://localhost:8543/my-aktion/rest/spende/list/";

	private Client restClient;
	
	public SpendeListProvider() {
		restClient = ClientBuilder.newClient();
		restClient.getConfiguration().getClasses().add(SpendeListMBR.class);
		// erlaubt ausschliesslich localhost fuer SSL
		HttpsURLConnection
				.setDefaultHostnameVerifier(new LocalHostnameVerifier());
	}

	/**
	 * Gibt die Liste aller Spenden zu der Aktion mit der angegebenen ID zurück.
	 * 
	 * @param aktionId
	 * @return
	 * @throws NotFoundException
	 * @throws WebApplicationException
	 */
	public List<Spende> getSpendeList(long aktionId) throws NotFoundException, WebApplicationException {
		WebTarget target = restClient.target(REST_SPENDE_LIST + aktionId);
		GenericType<List<Spende>> list = new GenericType<List<Spende>>() {
		};
		List<Spende> result = target.request(MediaType.APPLICATION_JSON).get(
				list);
		return result;
	}

}
