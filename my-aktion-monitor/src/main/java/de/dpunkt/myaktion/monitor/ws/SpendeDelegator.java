package de.dpunkt.myaktion.monitor.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.monitor.MonitorWebSocket;

@WebService
public class SpendeDelegator {
	@WebMethod
	public void receiveSpende(Long aktionId, Spende spende) {
		MonitorWebSocket.getInstance().informClients(spende, aktionId);
	}
}
