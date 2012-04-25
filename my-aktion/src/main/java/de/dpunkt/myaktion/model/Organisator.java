package de.dpunkt.myaktion.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@NamedQueries({
	@NamedQuery(name=Organisator.findByEmail,query="SELECT o FROM Organisator o WHERE o.email = :email"),
})
@Entity
public class Organisator {
	public static final String findByEmail = "Organisator.findByEmail";
	
	@NotNull
	private String vorname;
	@NotNull
	private String nachname;
	@Id
	private String email;
	@NotNull
	private String passwort;

	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

}
