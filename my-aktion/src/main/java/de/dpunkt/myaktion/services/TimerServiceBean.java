package de.dpunkt.myaktion.services;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.Spende.Status;

@Singleton
public class TimerServiceBean {
	
	@Inject
	EntityManager entityManager;
	
	@Inject
	private Logger logger;
	
    @Schedule(minute="*/1", hour="*", persistent=false)
    public void doTransferSpende(){
    	logger.info("Zu bearbeitende Spenden werden überwiesen.");
    	TypedQuery<Spende> query = entityManager.createNamedQuery(Spende.findWorkInProcess, Spende.class);
    	query.setParameter("status", Status.IN_BEARBEITUNG);
		List<Spende> spenden = query.getResultList();
		int count = 0;
    	for (Spende spende : spenden) {
			spende.setStatus(Status.UEBERWIESEN);
			count++;
		}
    	logger.info("Es wurden " + count + " Spenden überwiesen.");
    }
}
