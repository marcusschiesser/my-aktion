package de.dpunkt.myaktion.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Spende;

@Stateless
public class AktionService implements IAktionService {

	@Inject
	EntityManager entityManager;

	@Override
	public List<Aktion> getAllAktionen() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Aktion> criteria = cb.createQuery(Aktion.class);
		Root<Aktion> aktion = criteria.from(Aktion.class);
		// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
		criteria.select(aktion).orderBy(cb.asc(aktion.get("name")));
		List<Aktion> aktionen = entityManager.createQuery(criteria).getResultList();
		// transientes bisher gespendet Feld aktualisieren
		for(Aktion a: aktionen) {
			Double bisherGespendet = getBisherGespendet(a.getId());
			a.setBisherGespendet(bisherGespendet);
		}
		return aktionen;
	}
	
	@SuppressWarnings("unchecked")
	private Double getBisherGespendet(Long aktionId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Double> criteria = cb.createQuery(Double.class);
		Root spende = criteria.from(Spende.class);
		criteria.select(cb.sum(spende.get("betrag")));
		criteria.where(cb.equal(spende.get("aktion"), aktionId));
		return entityManager.createQuery(criteria).getSingleResult().doubleValue();
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
