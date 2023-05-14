package co.edu.unbosque.beans;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import com.google.common.io.BaseEncoding;

import jakarta.faces.annotation.FacesConfig;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;

@SuppressWarnings("serial")
@FacesConfig
@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {
	private String imagen;
	private String imagenTrasera;
	private String imagenJuego;

	/**
	 * @return the imagenJuego
	 */
	public String getImagenJuego() {

		return imagenJuego;
	}

	/**
	 * @param imagenJuego the imagenJuego to set
	 */
	public void setImagenJuego(String imagenJuego) {

		this.imagenJuego = imagenJuego;
	}

	public void jugar() {
		String base64Image = null;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
			String realPath = servletContext.getRealPath("resources/+4.png");
			Path path = Paths.get(realPath);
			byte[] imageBytes = Files.readAllBytes(path);
			int length = imageBytes.length;
			int padding = length % 3 == 0 ? 0 : 3 - length % 3;
			byte[] paddedBytes = new byte[length + padding];
			System.arraycopy(imageBytes, 0, paddedBytes, 0, length);
			base64Image = Base64.getEncoder().encodeToString(imageBytes);
			String base64Encoded = BaseEncoding.base64().encode(paddedBytes);
			base64Image = base64Encoded;
		} catch (IOException e) {
			System.err.println(e);
		}

		String imageString = "data:image/jpg;base64," + base64Image;
		imagenJuego = imageString;
		System.out.println(imageString);
	}

	public void cargarImagenTrasera() {
		String base64Image = null;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
			String realPath = servletContext.getRealPath("resources/ParteTrasera.png");
			Path path = Paths.get(realPath);
			byte[] imageBytes = Files.readAllBytes(path);
			int length = imageBytes.length;
			int padding = length % 3 == 0 ? 0 : 3 - length % 3;
			byte[] paddedBytes = new byte[length + padding];
			System.arraycopy(imageBytes, 0, paddedBytes, 0, length);
			base64Image = Base64.getEncoder().encodeToString(imageBytes);
			String base64Encoded = BaseEncoding.base64().encode(paddedBytes);
			base64Image = base64Encoded;
		} catch (IOException e) {
			System.err.println(e);
		}

		String imageString = "data:image/jpg;base64," + base64Image;
		imagenTrasera = imageString;
	}

	public void cargarImagen() {
		String base64Image = null;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
			String realPath = servletContext.getRealPath("resources/+4.png");
			Path path = Paths.get(realPath);
			byte[] imageBytes = Files.readAllBytes(path);
			int length = imageBytes.length;
			int padding = length % 3 == 0 ? 0 : 3 - length % 3;
			byte[] paddedBytes = new byte[length + padding];
			System.arraycopy(imageBytes, 0, paddedBytes, 0, length);
			base64Image = Base64.getEncoder().encodeToString(imageBytes);
			String base64Encoded = BaseEncoding.base64().encode(paddedBytes);
			base64Image = base64Encoded;
		} catch (IOException e) {
			System.err.println(e);
		}

		String imageString = "data:image/jpg;base64," + base64Image;
		imagen = imageString;
	}

	public void robar() {
		String base64Image = null;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
			String realPath = servletContext.getRealPath("resources/0Azul.png");
			Path path = Paths.get(realPath);
			byte[] imageBytes = Files.readAllBytes(path);
			int length = imageBytes.length;
			int padding = length % 3 == 0 ? 0 : 3 - length % 3;
			byte[] paddedBytes = new byte[length + padding];
			System.arraycopy(imageBytes, 0, paddedBytes, 0, length);
			base64Image = Base64.getEncoder().encodeToString(imageBytes);
			String base64Encoded = BaseEncoding.base64().encode(paddedBytes);
			base64Image = base64Encoded;
		} catch (IOException e) {
			System.err.println(e);
		}

		String imageString = "data:image/jpg;base64," + base64Image;
		imagenTrasera = imageString;
	}

	public void moverIzquierda() {

	}

	public void moverDerecha() {

	}

	/**
	 * @return the imagenTrasera
	 */
	public String getImagenTrasera() {
		cargarImagenTrasera();
		return imagenTrasera;
	}

	/**
	 * @param imagenTrasera the imagenTrasera to set
	 */
	public void setImagenTrasera(String imagenTrasera) {
		this.imagenTrasera = imagenTrasera;
	}

	/**
	 * @return the imagen
	 */
	public String getImagen() {
		cargarImagen();
		return imagen;
	}

	/**
	 * @param imagen the imagen to set
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}
