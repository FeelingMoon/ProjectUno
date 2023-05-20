package co.edu.unbosque.persistence;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
	private String nombre;
	private List<Carta> cartas;
	private int indiceCartaActual;

	public String getNombre() {
		return nombre;
	}

	public Jugador(String nombre) {
		this.nombre = nombre;
		this.cartas = new ArrayList<>();
		this.indiceCartaActual = 0;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Carta> getCartas() {
		return cartas;
	}

	public void setCartas(List<Carta> cartas) {
		this.cartas = cartas;
	}

	public int getIndiceCartaActual() {
		return indiceCartaActual;
	}

	public void setIndiceCartaActual(int indiceCartaActual) {
		this.indiceCartaActual = indiceCartaActual;
	}
}
