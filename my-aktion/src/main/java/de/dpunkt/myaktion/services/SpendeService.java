package de.dpunkt.myaktion.services;

import java.util.List;

import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.services.exceptions.ObjectNotFoundException;

public interface SpendeService {
	List<Spende> getSpendeListPublic(Long aktionId) throws ObjectNotFoundException;
	List<Spende> getSpendeList(Long aktionId);
	void addSpende(Long aktionId, Spende spende);
	void transferSpende();
}
