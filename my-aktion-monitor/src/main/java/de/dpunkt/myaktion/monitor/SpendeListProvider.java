package de.dpunkt.myaktion.monitor;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.SpendeListMBR;
import de.dpunkt.myaktion.monitor.util.DisableHostnameVerifier;

/**
 * @author marcus
 */
public class SpendeListProvider {

    private static final String REST_HOST = "localhost";
    private static final int REST_PORT = 8443;

    private static final String REST_SPENDE_LIST = "https://" + REST_HOST + ":"
	    + REST_PORT + "/my-aktion/rest/spende/list/";

    private Client restClient;

    public SpendeListProvider() {
	ClientBuilder builder = ClientBuilder.newBuilder();
	builder.register(SpendeListMBR.class);
	builder.hostnameVerifier(new DisableHostnameVerifier());
	restClient = builder.build();
    }

    /**
     * Gibt die Liste aller Spenden zu der Aktion mit der angegebenen ID zurück.
     * 
     * @param aktionId
     * @return
     * @throws NotFoundException
     * @throws WebApplicationException
     */
    public List<Spende> getSpendeList(long aktionId) throws NotFoundException,
	    WebApplicationException {
	WebTarget target = restClient.target(REST_SPENDE_LIST + aktionId);
	GenericType<List<Spende>> list = new GenericType<List<Spende>>() {
	};
	List<Spende> result = target.request(MediaType.APPLICATION_JSON).get(
		list);
	return result;
    }

}
