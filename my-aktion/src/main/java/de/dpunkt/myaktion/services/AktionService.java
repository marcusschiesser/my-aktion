package de.dpunkt.myaktion.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.dpunkt.myaktion.model.Aktion;

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
		return entityManager.createQuery(criteria).getResultList();
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
