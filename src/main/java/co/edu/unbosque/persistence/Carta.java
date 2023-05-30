package co.edu.unbosque.persistence;

public class Carta {
	private String color;
	private String numero;
	private String imagen;

	public Carta(String color, String numero) {

		this.color = color;
		this.numero = numero;
		
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	@Override
	public String toString() {
		return color + " " + numero;
	}
}
