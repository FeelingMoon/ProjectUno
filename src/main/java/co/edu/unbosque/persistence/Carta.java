package co.edu.unbosque.persistence;

/**
 * La clase Carta representa una carta de juego.
 */
public class Carta {
	private String color;//representa el color de la carta.
	private String numero;//representa el número de la carta.
	private String imagen;//representa la imagen asociada a la carta.

	/**
     * Crea una nueva instancia de Carta con el color y número especificados.
     *
     * @param color  El color de la carta.
     * @param numero El número de la carta.
     */
	public Carta(String color, String numero) {

		this.color = color;
		this.numero = numero;
		
	}
	
	/**
     * Obtiene el color de la carta.
     *
     * @return El color de la carta.
     */
	public String getColor() {
		return color;
	}

	/**
     * Establece el color de la carta.
     *
     * @param color El nuevo color de la carta.
     */
	public void setColor(String color) {
		this.color = color;
	}
	
	/**
     * Obtiene el número de la carta.
     *
     * @return El número de la carta.
     */
	public String getNumero() {
		return numero;
	}
	
	/**
     * Establece el número de la carta.
     *
     * @param numero El nuevo número de la carta.
     */
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	/**
     * Obtiene la imagen asociada a la carta.
     *
     * @return La imagen de la carta.
     */
	public String getImagen() {
		return imagen;
	}

	/**
     * Establece la imagen asociada a la carta.
     *
     * @param imagen La nueva imagen de la carta.
     */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	/**
     * Devuelve una representación en forma de cadena de la carta.
     *
     * @return Una cadena que representa la carta.
     */
	@Override
	public String toString() {
		return color + " " + numero;
	}
}
