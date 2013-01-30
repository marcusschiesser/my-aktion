package de.dpunkt.myaktion.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.services.AktionService;
import de.dpunkt.myaktion.util.Events.Added;
import de.dpunkt.myaktion.util.Events.Deleted;
import de.dpunkt.myaktion.util.Events.Updated;

@RequestScoped
@Named
public class AktionListProducer {
	
	private List<Aktion> aktionen;
	
	@Inject
	private AktionService aktionService;

	@PostConstruct
	public void init() {
		aktionen = aktionService.getAllAktionen();
	}

	public List<Aktion> getAktionen() {
		return aktionen;
	}

	public void onAktionAdded(@Observes @Added Aktion aktion) {
		aktionService.addAktion(aktion);
		init();
	}
	
	public void onAktionDeleted(@Observes @Deleted Aktion aktion) {
		aktionService.deleteAktion(aktion);
		init();
	}

	public void onAktionUpdated(@Observes @Updated Aktion aktion) {
		aktionService.updateAktion(aktion);
		init();
	}

}
