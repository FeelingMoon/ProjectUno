/**
 * La clase Jugador representa a un jugador en un juego.
 */
package co.edu.unbosque.persistence;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
	private String nombre;
	private List<Carta> cartas;
	private int indiceCartaActual;
	
	 /**
     * Obtiene el nombre del jugador.
     *
     * @return el nombre del jugador
     */
	public String getNombre() {
		return nombre;
	}

	/**
     * Crea un nuevo objeto Jugador con el nombre especificado.
     *
     * @param nombre el nombre del jugador
     */
	public Jugador(String nombre) {
		this.nombre = nombre;
		this.cartas = new ArrayList<>();
		this.indiceCartaActual = 0;
	}

	 /**
     * Establece el nombre del jugador.
     *
     * @param nombre el nuevo nombre del jugador
     */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	 /**
     * Obtiene la lista de cartas del jugador.
     *
     * @return la lista de cartas del jugador
     */
	public List<Carta> getCartas() {
		return cartas;
	}

	 /**
     * Establece la lista de cartas del jugador.
     *
     * @param cartas la nueva lista de cartas del jugador
     */
	public void setCartas(List<Carta> cartas) {
		this.cartas = cartas;
	}

	/**
     * Obtiene el índice de la carta actual del jugador.
     *
     * @return el índice de la carta actual del jugador
     */
	public int getIndiceCartaActual() {
		return indiceCartaActual;
	}

	/**
     * Establece el índice de la carta actual del jugador.
     *
     * @param indiceCartaActual el nuevo índice de la carta actual del jugador
     */
	public void setIndiceCartaActual(int indiceCartaActual) {
		this.indiceCartaActual = indiceCartaActual;
	}
}
