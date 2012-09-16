package de.dpunkt.myaktion.services;

import java.util.List;

import de.dpunkt.myaktion.model.Aktion;

public interface AktionService {
	List<Aktion> getAllAktionen();
	Aktion addAktion(Aktion aktion);
	void deleteAktion(Aktion aktion);
	Aktion updateAktion(Aktion aktion);
	void deleteAktion(Long aktionId);
	Aktion getAktion(Long aktionId);
}
