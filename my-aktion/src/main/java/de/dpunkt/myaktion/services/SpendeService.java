package de.dpunkt.myaktion.services;

import java.util.List;

import de.dpunkt.myaktion.model.Spende;

public interface SpendeService {
	List<Spende> getSpendeListPublic(Long aktionId);
	List<Spende> getSpendeList(Long aktionId);
	void addSpende(Long aktionId, Spende spende);
	void transferSpende();
}
