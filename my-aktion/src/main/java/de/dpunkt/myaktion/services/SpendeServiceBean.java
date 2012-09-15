package de.dpunkt.myaktion.services;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.model.Spende.Status;

@Stateless
public class SpendeServiceBean implements SpendeService {

	@Inject
	EntityManager entityManager;
	
	@Inject
	private Logger logger;
	
	@Resource(mappedName="WebSocket")
	private Queue queue;
	
	@Resource(mappedName="WebSocketFactory")
	private QueueConnectionFactory queueConnectionFactory;
	
	@RolesAllowed("Organisator")
	public List<Spende> getSpendeList(Long aktionId) {
		Aktion managedAktion = entityManager.find(Aktion.class, aktionId);
		List<Spende> spenden = managedAktion.getSpenden();
		spenden.size(); // Wg. JPA Field-Access müssen wir eine Methode auf dem spenden Objekt aufrufen
		return spenden;
	}

	@PermitAll
	public void addSpende(Long aktionId, Spende spende) {
		// Spende in lokaler Datenbank hinzufügen
		Aktion managedAktion = entityManager.find(Aktion.class, aktionId);
		spende.setAktion(managedAktion);
		entityManager.persist(spende);
		// Spende an JMS-Queue senden
		Connection connection = null;
		try {
			connection = queueConnectionFactory.createQueueConnection();
			Session session = connection.createSession(true, 0);
			MessageProducer producer = session.createProducer(queue);
			ObjectMessage message = session.createObjectMessage(spende);
			producer.send(message);	
		} catch (JMSException e) {
			throw new EJBException("Fehler beim Senden der Spende-Nachricht", e);
		} finally {
			// oder ganz korrekt: http://shrubbery.mynetgear.net/c/display/W/Close+JMS+Objects
			if(connection!=null) {
				try {
					connection.close();
				} catch (JMSException e) {
					throw new EJBException("Fehler beim Schließen der JMS-Queue", e);
				}
			}
		}
	}
	
	@PermitAll
    public void transferSpende(){
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
