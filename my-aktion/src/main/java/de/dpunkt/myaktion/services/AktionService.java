package de.dpunkt.myaktion.services;

import java.util.List;

import de.dpunkt.myaktion.model.Aktion;

public interface AktionService {
	List<Aktion> getAllAktionen();
	void addAktion(Aktion aktion);
	void deleteAktion(Aktion aktion);
	void updateAktion(Aktion aktion);
}
