package de.dpunkt.myaktion.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.dpunkt.myaktion.model.Aktion;

@Stateless
public class AktionServiceBean implements AktionService {

	@Inject
	EntityManager entityManager;

	public List<Aktion> getAllAktionen() {
		TypedQuery<Aktion> query = entityManager.createNamedQuery(Aktion.findAll, Aktion.class);
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
		entityManager.persist(aktion);
	}

	public void deleteAktion(Aktion aktion) {
		Aktion managedAktion = entityManager.find(Aktion.class, aktion.getId());
		entityManager.remove(managedAktion);
	}

	public void updateAktion(Aktion aktion) {
		entityManager.merge(aktion);
	}

}
