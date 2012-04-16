package de.dpunkt.myaktion.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class Konto {
	@NotNull
	@Size(min=5, max=60, message="Der Name des Besitzers eines Kontos muss min. 5 und darf max. 60 Zeichen lang sein.")
	private String name;
	@NotNull
	@Size(min=4, max=40, message="Der Name einer Bank muss min. 4 und darf max. 40 Zeichen lang sein.")
	private String nameDerBank;
	@NotNull
	@Pattern(regexp="\\d+", message="Eine Konto-Nr. besteht nur aus Ziffern.")
	private String kontoNr;
	@NotNull
	@Pattern(regexp="\\d{8}", message="Eine Bankleitzahl besteht immer aus 8 Ziffern.")
	private String blz;
	
	public Konto() {
		this(null, null, null, null);
	}
	
	public Konto(String name, String nameDerBank, String kontoNr, String blz) {
		super();
		this.name = name;
		this.nameDerBank = nameDerBank;
		this.kontoNr = kontoNr;
		this.blz = blz;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameDerBank() {
		return nameDerBank;
	}
	public void setNameDerBank(String nameDerBank) {
		this.nameDerBank = nameDerBank;
	}
	public String getKontoNr() {
		return kontoNr;
	}
	public void setKontoNr(String kontoNr) {
		this.kontoNr = kontoNr;
	}
	public String getBlz() {
		return blz;
	}
	public void setBlz(String blz) {
		this.blz = blz;
	}
	

}
