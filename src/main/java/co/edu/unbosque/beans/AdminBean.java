package co.edu.unbosque.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@FacesConfig
@Named("AdminBean")
@RequestScoped
public class AdminBean {
	private String saludo = "Bienvenido";
	private String nombre = "";
	private String contrase = "";
	private String nombreComprobar = "Valeria";
	private String contraComprobar = "12345";

	/**
	 * @return the contrase
	 */
	public String getContrase() {
		return contrase;
	}

	/**
	 * @param contrase the contrase to set
	 */
	public void setContrase(String contrase) {
		this.contrase = contrase;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSaludo() {
		return saludo;
	}

	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}

	public void saludar() {
		if (nombre.equalsIgnoreCase(nombreComprobar) && contrase.equalsIgnoreCase(contraComprobar)) {
			saludo = saludo + " " + nombre + " su contraseña es " + contrase;
			addMessage(FacesMessage.SEVERITY_INFO, "Acceso concedido", "Bienvenido/a " + nombre);
		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, "Acceso denegado",
					"Revise los datos ingresados y vuelva a intentar.");
		}
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
