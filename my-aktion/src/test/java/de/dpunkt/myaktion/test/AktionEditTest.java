package de.dpunkt.myaktion.test;

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

import de.dpunkt.myaktion.controller.AktionEditController;
import de.dpunkt.myaktion.controller.AktionEditController.Mode;
import de.dpunkt.myaktion.model.Aktion;
import de.dpunkt.myaktion.model.Konto;
import de.dpunkt.myaktion.model.Spende;

@RunWith(Arquillian.class)
public class AktionEditTest {
   @Deployment
   public static Archive<?> createTestArchive() {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Aktion.class, Konto.class, Spende.class, AktionEditController.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Inject
   AktionEditController aktionEditController;

   @Test
   public void testAddAktion() throws Exception {
	   Assert.assertNotNull(aktionEditController);
	   aktionEditController.setAktionToEdit(Mode.ADD);
	   Aktion aktion = aktionEditController.getAktion();
	   Assert.assertNotNull(aktion);
	   aktion.setName("Testaktion");
	   aktion.setSpendenBetrag(10d);
	   aktion.setSpendenZiel(1000d);
	   Konto konto = aktion.getKonto();
	   Assert.assertNotNull(konto);
	   konto.setName("Test Tester");
	   konto.setBlz("123456");
	   konto.setKontoNr("012345");
	   konto.setNameDerBank("XXX Bank");
	   aktionEditController.doSave();
	   // TODO: Test ob Aktion-Objekt persistiert wurde
   }
   
}
