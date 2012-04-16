package de.dpunkt.myaktion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Spende {
	// Der NumberConverter konvertiert leere Strings in einen Null-Wert, daher
	// kommen bei Nicht-Angabe Null-Werte von Faces zurück - für diese wird daher ein Message-Wert benötigt.
	@NotNull(message="Bitte einen Spendenbetrag angeben.")
	@DecimalMin(value="1.00", message="Der Spendenbetrag muss min. 1 Euro sein.")
	private Double betrag;
	@NotNull
	@Size(min=5, max=40, message="Der Name eines Spenders muss min. 5 und darf max. 40 Zeichen lang sein.")
	private String spenderName;
	@NotNull
	private Boolean quittung;
	@NotNull
	private Status status;
	@NotNull
	private Konto konto;
	@NotNull
	@ManyToOne
	private Aktion aktion;
	
	@GeneratedValue
	@Id
	private Long id;
	
	public enum Status {
		UEBERWIESEN, IN_BEARBEITUNG;
	}

	public Spende() {
		this.konto = new Konto();
	}
	
	public Aktion getAktion() {
		return aktion;
	}

	public void setAktion(Aktion aktion) {
		this.aktion = aktion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getBetrag() {
		return betrag;
	}

	public void setBetrag(Double betrag) {
		this.betrag = betrag;
	}

	public String getSpenderName() {
		return spenderName;
	}

	public void setSpenderName(String spenderName) {
		this.spenderName = spenderName;
	}

	public Boolean getQuittung() {
		return quittung;
	}

	public void setQuittung(Boolean quittung) {
		this.quittung = quittung;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Konto getKonto() {
		return konto;
	}

	public void setKonto(Konto konto) {
		this.konto = konto;
	}

	
}
