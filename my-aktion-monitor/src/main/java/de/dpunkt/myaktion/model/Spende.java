package de.dpunkt.myaktion.model;

import java.io.Serializable;

public class Spende implements Serializable {

    private static final long serialVersionUID = -1392398877357073405L;

    public Spende() {
    }

    private Double betrag;
    private String spenderName;

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

}
