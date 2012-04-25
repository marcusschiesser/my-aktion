package de.dpunkt.myaktion.controller;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@ViewScoped
@Named
public class SpendeFormEditController implements Serializable {
	
	private static final long serialVersionUID = -4210085664588144340L;

	private String textColor = "000000";
	private String bgColor = "ffffff";

	public String doOk() {
		return Pages.AKTION_LIST;
	}
	
	public String getUrl() {
		return "http://localhost:8080/my-aktion/geldSpenden.jsf?bgColor=" + bgColor + "&textColor=" + textColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	
	public String updateUrl() {
		return Pages.SPENDE_FORM_EDIT;
	}
}
