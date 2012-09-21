package de.dpunkt.myaktion.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


//Achtung: Entspricht nicht dem Original. Aktion und Konto wurden entfernt. Alle Fachklassen müssen wegen JMS Serializable implementieren.
@Entity
public class Spende implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1392398877357073405L;

	/**
	 * 
	 */
	

	@GeneratedValue
	@Id
	private Long id;
	
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
	
	
	public enum Status {
		UEBERWIESEN, IN_BEARBEITUNG;
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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}

