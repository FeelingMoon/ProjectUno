package co.edu.unbosque.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import co.edu.unbosque.persistence.Carta;
import co.edu.unbosque.persistence.Jugador;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@SuppressWarnings("serial")
@Named("juego")
@ViewScoped
public class JuegoBean implements Serializable {
	private List<Jugador> jugadores;
	private Jugador jugadorActual;
	private Carta cartaActual;
	private List<Carta> mazo;
	private List<Carta> mazoRepartir;
	private String color;
	private String numero;
	private Carta cartaJugada; // Agregamos una nueva propiedad para almacenar la carta jugada

	@PostConstruct
	public void init() {
		// Inicializar el juego
		jugadores = new ArrayList<>();
		jugadores.add(new Jugador("Jugador 1"));
		jugadores.add(new Jugador("Jugador 2"));
		jugadores.add(new Jugador("Jugador 3"));
		jugadores.add(new Jugador("Jugador 4"));

		mazo = generarMazo();
		mazoRepartir = new ArrayList<>(mazo);
		Collections.shuffle(mazoRepartir);
		repartirCartas();
		jugadorActual = jugadores.get(0); // Comienza el juego con el primer jugador
		cartaActual = obtenerCartaAleatoria();

	}

	public Carta obtenerCartaAleatoria() {
		int indiceAleatorio = (int) (Math.random() * mazoRepartir.size());
		return mazoRepartir.remove(indiceAleatorio);
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public Carta getCartaActual() {
		return cartaActual;
	}

	public Jugador getJugadorActual() {
		return jugadorActual;
	}

	public void jugarCarta() {
		Jugador jugadorActual = getJugadorActual();

		if (cartaJugada != null) {
			if (jugadorActual.getCartas().contains(cartaJugada)) {
				if (esCartaValida(cartaJugada)) {
					Carta cartaAnterior = cartaActual;
					cartaActual = cartaJugada;
					jugadorActual.getCartas().remove(cartaJugada);
					seleccionarSiguienteJugador();

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Carta jugada: " + cartaJugada.toString(), ""));

					cartaJugada = null;
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La carta seleccionada no es válida", null));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La carta seleccionada no pertenece al jugador actual", null));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado ninguna carta", null));
		}
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

	public void repartirCartas() {
		int cartasPorJugador = 4; // Repartir cuatro cartas a cada jugador

		for (Jugador jugador : jugadores) {
			jugador.setCartas(new ArrayList<>()); // Limpiar las cartas anteriores del jugador
			for (int i = 0; i < cartasPorJugador; i++) {
				Carta carta = mazoRepartir.remove(0); // Tomar la carta del mazo de repartir
				jugador.getCartas().add(carta);
			}
		}
	}

	public List<Carta> generarMazo() {
		List<Carta> mazo = new ArrayList<>();

		for (String color : Arrays.asList("Rojo", "Verde", "Azul", "Amarillo")) {
			// Cartas numeradas
			for (int numero = 0; numero <= 9; numero++) {
				mazo.add(new Carta(color, String.valueOf(numero)));
				if (numero != 0) {
					mazo.add(new Carta(color, String.valueOf(numero)));
				}
			}

			mazo.add(new Carta(color, "Salto"));
			mazo.add(new Carta(color, "Reversa"));
			mazo.add(new Carta(color, "+2"));
		}

		return mazo;
	}

	public void seleccionarSiguienteJugador() {
		int indexActual = jugadores.indexOf(jugadorActual);
		int siguienteIndex = (indexActual + 1) % jugadores.size();
		jugadorActual = jugadores.get(siguienteIndex);

		if (jugadorActual.getIndiceCartaActual() >= jugadorActual.getCartas().size()) {
			jugadorActual.setIndiceCartaActual(0);
		}
	}

	public void robarCarta() {
		Jugador jugadorActual = getJugadorActual();

		if (!mazoRepartir.isEmpty()) {
			Carta cartaRobada = mazoRepartir.remove(0); // Tomar la carta del mazo de repartir
			jugadorActual.getCartas().add(cartaRobada);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Carta robada: " + cartaRobada.toString(), "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "No quedan cartas en el mazo", null));
		}
	}

	private boolean esCartaValida(Carta carta) {
		if (carta.getColor().equals(cartaActual.getColor()) || carta.getNumero().equals(cartaActual.getNumero())) {
			return true;
		}

		if (carta.getNumero().equals("Comodín")) {
			return true;
		}

		return false;
	}

	public Carta getCartaJugada() {
		return cartaJugada;
	}

	public void setCartaJugada(Carta cartaJugada) {
		this.cartaJugada = cartaJugada;
	}
}
