package de.dpunkt.myaktion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Spende {
	private Double betrag;
	private String spenderName;
	private Boolean quittung;
	private Status status;
	private Konto konto;
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
