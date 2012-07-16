package de.dpunkt.myaktion.test;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.dpunkt.myaktion.controller.GeldSpendenController;
import de.dpunkt.myaktion.controller.SpendeListController;
import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Konto;
import de.dpunkt.myaktion.model.Spende;
import de.dpunkt.myaktion.services.AktionService;
import de.dpunkt.myaktion.services.AktionServiceBean;
import de.dpunkt.myaktion.services.SpendeService;
import de.dpunkt.myaktion.services.SpendeServiceBean;
import de.dpunkt.myaktion.util.Resources;

@RunWith(Arquillian.class)
public class SpendeListTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(Aktion.class, Konto.class, Spende.class,
						SpendeListController.class,
						GeldSpendenController.class, SpendeService.class,
						SpendeServiceBean.class, AktionService.class,
						AktionServiceBean.class, Resources.class,
						MockFactory.class)
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");
	}

	@Inject
	private GeldSpendenController geldSpendenController;

	@Inject
	private SpendeService spendeService;

	@Inject
	private AktionService aktionService;

	@Produces
	public FacesContext produceFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Test
	public void testAddSpende() throws Exception {
		Aktion aktion = MockFactory.createMockAktion();
		aktionService.addAktion(aktion);
		geldSpendenController.setAktionId(aktion.getId());
		geldSpendenController.setSpende(MockFactory
				.createMockSpende());
		geldSpendenController.addSpende();
		// Überprüfen, ob nach der Spende eine Liste von Spende-Objekten für die
		// Aktion existiert
		List<Spende> spenden = spendeService.getSpendeList(aktion.getId());
		Assert.assertNotNull(spenden);
		// Überprüfen, ob der Betrag der Spende in der Liste derselbe ist, wie in
		// der gemockten Spende
		Assert.assertEquals(MockFactory
				.createMockSpende().getBetrag(), spenden.get(0).getBetrag());
	}

	@Test
	public void testBisherGespendet() throws Exception {
		Aktion aktion = MockFactory.createMockAktion();
		aktionService.addAktion(aktion);
		// zwei Spenden erstellen (pro Spende 20,-)
		spendeService.addSpende(aktion.getId(),
				MockFactory.createMockSpende());
		spendeService.addSpende(aktion.getId(),
				MockFactory.createMockSpende());
		// persistierte Aktion in der Liste aller Aktionen suchen
		List<Aktion> aktionen = aktionService.getAllAktionen();
		Aktion persistedAktion = null;
		for (Aktion a : aktionen) {
			if (a.getId() == aktion.getId())
				persistedAktion = a;
		}
		// Überprüfen, ob der gespendete Wert für die Aktion der zweifache Spendenbetrag ist
		Double bisherGespendet = persistedAktion.getBisherGespendet();
		Assert.assertEquals(new Double(2*MockFactory.createMockSpende().getBetrag()), bisherGespendet);
	}

}
