package de.dpunkt.myaktion.services;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Organisator;

@Stateless
@RolesAllowed("Organisator")
public class AktionServiceBean implements AktionService {

	@Inject
	private EntityManager entityManager;

	@Resource
	private SessionContext sessionContext;
	
	public List<Aktion> getAllAktionen() {
		TypedQuery<Aktion> query = entityManager.createNamedQuery(Aktion.findByOrganisator, Aktion.class);
		query.setParameter("organisator", getLoggedinOrganisator());
		List<Aktion> aktionen = query.getResultList();
		// transientes bisher gespendet Feld aktualisieren
		for(Aktion a: aktionen) {
			Double bisherGespendet = getBisherGespendet(a);
			a.setBisherGespendet(bisherGespendet);
		}
		return aktionen;
	}
	
	private Double getBisherGespendet(Aktion aktion) {
		TypedQuery<Double> query = entityManager.createNamedQuery(Aktion.getBisherGespendet, Double.class);
		query.setParameter("aktion", aktion);
		Double result = query.getSingleResult();
		if(result==null)
			result = 0d;
		return result;
	}

	public void addAktion(Aktion aktion) {
		Organisator organisator = getLoggedinOrganisator();
		aktion.setOrganisator(organisator);
		entityManager.persist(aktion);
	}

	private Organisator getLoggedinOrganisator() {
		String organisatorEmail = sessionContext.getCallerPrincipal().getName();
		Organisator organisator = entityManager.createNamedQuery(Organisator.findByEmail, Organisator.class)
			.setParameter("email", organisatorEmail).getSingleResult();
		return organisator;
	}

	public void deleteAktion(Aktion aktion) {
		deleteAktion(aktion.getId());
	}

	public void updateAktion(Aktion aktion) {
		entityManager.merge(aktion);
	}

	public void deleteAktion(Long aktionId) {
		Aktion managedAktion = entityManager.find(Aktion.class, aktionId);
		entityManager.remove(managedAktion);
	}

}
