package co.edu.unbosque.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import co.edu.unbosque.persistence.Carta;
import co.edu.unbosque.persistence.Imagen64;
import co.edu.unbosque.persistence.Jugador;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@SuppressWarnings("serial")
@Named("juego")
@ViewScoped
public class JuegoBean implements Serializable {
	private Random rand;
	private List<Jugador> jugadores;
	private Jugador jugadorActual;
	private Carta cartaActual;
	private Stack<Carta> mazo;
	private String colorAux;

	public Stack<Carta> getMazo() {
		return mazo;
	}

	public void setMazo(Stack<Carta> mazo) {
		this.mazo = mazo;
	}

	public Stack<Carta> getJugadas() {
		return jugadas;
	}

	public void setJugadas(Stack<Carta> jugadas) {
		this.jugadas = jugadas;
	}

	private String numero;
	private Carta cartaJugada; // Agregamos una nueva propiedad para almacenar la carta jugada
	private boolean confirmacion;
	private boolean sentido;
	private boolean ganador;
	private Stack<Carta> jugadas;
	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		jugadores.get(0).setNombre(nombre);
		this.nombre = nombre;
	}

	public boolean isConfirmacion() {
		return confirmacion;
	}

	public String getColorAux() {
		return colorAux;
	}

	public void setColorAux(String colorAux) {
		this.colorAux = colorAux;
	}

	public void setConfirmacion(boolean confirmacion) {
		this.confirmacion = confirmacion;
	}

	public void reiniciarJuego() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

		try {

			externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@PostConstruct
	public void init() {
		// Inicializar el juego
		jugadores = new ArrayList<>();
		jugadores.add(new Jugador("Jugador 1"));
		jugadores.add(new Jugador("Jugador 2"));
		jugadores.add(new Jugador("Jugador 3"));

		mazo = generarMazo();
		jugadas = new Stack<>();
		rand = new Random();
		Collections.shuffle(mazo);
		repartirCartas();
		jugadorActual = jugadores.get(0); // Comienza el juego con el primer jugador
		boolean especial = true;

		while (especial) {

			cartaActual = obtenerCartaAleatoria();
			jugadas.push(cartaActual);

			if (!cartaActual.getNumero().equals("+4") && !cartaActual.getNumero().equals("Multi")
					&& !cartaActual.getNumero().equals("+2") && !cartaActual.getNumero().equals("Reversa")
					&& !cartaActual.getNumero().equals("Salto")) {
				especial = false;
			}
		}

		ganador = false;
		confirmacion = false;
		sentido = true;

	}

	public Carta obtenerCartaAleatoria() {
		return mazo.pop();
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
					cartaActual = cartaJugada;
					jugadorActual.getCartas().remove(cartaJugada);

					if (jugadorActual.getCartas().size() == 0) {
						ganador = true;
						jugadores.remove(jugadorActual);
					}

					jugadas.push(cartaJugada);
					System.out.println(cartaJugada.getColor() + cartaJugada.getNumero());

					if (cartaJugada.getNumero().equals("Reversa")) {
						sentido = !sentido;
					}

					seleccionarSiguienteJugador();

					if (cartaJugada.getNumero().equals("Salto")) {
						seleccionarSiguienteJugador();
					}
					if (cartaJugada.getNumero().equals("+2")) {
						robarCarta();
						robarCarta();
					}
					if (cartaJugada.getColor().equals("Especiales")) {
						confirmar();
						if (cartaJugada.getNumero().equals("+4")) {
							robarCarta();
							robarCarta();
							robarCarta();
							robarCarta();
						}
					}

					if (!cartaJugada.getColor().equals("Especiales")) {
						if (!cartaJugada.getNumero().equals("+4")) {
							if (!getJugadorActual().getNombre().equals("Jugador 1")) {
								jugarBot();
							}
						}
					}

					if (!getJugadorActual().getNombre().equals("Jugador 1")) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Carta jugada: " + jugadorActual.getNombre() + " " + cartaJugada.toString(), ""));
					}

					cartaJugada = null;

				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La carta seleccionada no es válida", null));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La carta seleccionada no pertenece al jugador actual", null));
			}
		}
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void repartirCartas() {
		int cartasPorJugador = 7; // Repartir cuatro cartas a cada jugador

		for (Jugador jugador : jugadores) {
			jugador.setCartas(new ArrayList<>()); // Limpiar las cartas anteriores del jugador
			for (int i = 0; i < cartasPorJugador; i++) {
				Carta carta = mazo.pop(); // Tomar la carta del mazo de repartir
				jugador.getCartas().add(carta);
			}
		}
	}

	public Stack<Carta> generarMazo() {
		Stack<Carta> mazo = new Stack<>();

		for (String color : Arrays.asList("Rojo", "Verde", "Azul", "Amarillo")) {
			// Cartas numeradas
			for (int numero = 0; numero <= 9; numero++) {

				Carta nueva = new Carta(color, String.valueOf(numero));
				nueva.setImagen(Imagen64.cargarImg("../persistence/images/" + color + "/" + numero + color + ".png"));
				mazo.push(nueva);

				if (numero != 0) {
					mazo.push(nueva);
				}
			}

			Carta nueva = new Carta(color, "+2");
			nueva.setImagen(Imagen64.cargarImg("../persistence/images/" + color + "/" + "+2" + color + ".png"));
			mazo.push(nueva);
			Carta nueva2 = new Carta(color, "Salto");
			nueva2.setImagen(Imagen64.cargarImg("../persistence/images/" + color + "/" + "Proh" + color + ".png"));
			mazo.push(nueva2);
			Carta nueva3 = new Carta(color, "Reversa");
			nueva3.setImagen(Imagen64.cargarImg("../persistence/images/" + color + "/" + "Rev" + color + ".png"));
			mazo.push(nueva3);
			Carta nueva4 = new Carta(color, "+2");
			nueva4.setImagen(Imagen64.cargarImg("../persistence/images/" + color + "/" + "+2" + color + ".png"));
			mazo.push(nueva4);
			Carta nueva5 = new Carta(color, "Salto");
			nueva5.setImagen(Imagen64.cargarImg("../persistence/images/" + color + "/" + "Proh" + color + ".png"));
			mazo.push(nueva5);
			Carta nueva6 = new Carta(color, "Reversa");
			nueva6.setImagen(Imagen64.cargarImg("../persistence/images/" + color + "/" + "Rev" + color + ".png"));
			mazo.push(nueva6);
		}
		for (int i = 0; i <= 3; i++) {
			Carta nuevaE1 = new Carta("Especiales", "+4");
			nuevaE1.setImagen(Imagen64.cargarImg("../persistence/images/Especiales/+4.png"));
			mazo.push(nuevaE1);
			Carta nuevaE2 = new Carta("Especiales", "Multi");
			nuevaE2.setImagen(Imagen64.cargarImg("../persistence/images/Especiales/Multi.png"));
			mazo.push(nuevaE2);
		}

		return mazo;
	}

	public void seleccionarSiguienteJugador() {

		int indexActual = jugadores.indexOf(jugadorActual);
		int siguienteIndex = 0;

		if (sentido) {
			siguienteIndex = (indexActual + 1) % jugadores.size();
		}
		if (!sentido) {
			if (jugadorActual.equals(jugadores.get(0))) {
				siguienteIndex = jugadores.size() - 1;
			} else {
				siguienteIndex = (indexActual - 1);
			}
		}

		jugadorActual = jugadores.get(siguienteIndex);

		if (jugadorActual.getIndiceCartaActual() >= jugadorActual.getCartas().size()) {
			jugadorActual.setIndiceCartaActual(0);
		}
	}

	public void robarCarta() {
		Jugador jugadorActual = getJugadorActual();

		if (!mazo.isEmpty()) {
			Carta cartaRobada = mazo.pop(); // Tomar la carta del mazo de repartir
			jugadorActual.getCartas().add(cartaRobada);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Carta robada: " + cartaRobada.toString() + " por " + getJugadorActual().getNombre(), "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			if (jugadas.size() > 1) {
				for (Carta aux : jugadas) {
					mazo.push(aux);
				}

				Carta actual = jugadas.pop();
				jugadas = new Stack<>();
				jugadas.push(actual);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "!Mazo revuelto!", "");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "No quedan cartas en el mazo", null));
			}
		}
	}

	private boolean esCartaValida(Carta carta) {
		if (carta.getColor().equals(cartaActual.getColor()) || carta.getNumero().equals(cartaActual.getNumero())) {
			return true;
		}

		if (carta.getColor().equals("Especiales")) {
			return true;
		}

		return false;
	}

	public void cambiarColorAzul() {
		cartaActual.setColor("Azul");
		confirmacion = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a azul", null));
		if (cartaActual.getColor().equals("Especiales")) {
			seleccionarSiguienteJugador();
		}
		if (!getJugadorActual().getNombre().equals("Jugador 1")) {
			jugarBot();
		}
	}

	public void cambiarColorRojo() {
		cartaActual.setColor("Rojo");
		confirmacion = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a rojo", null));
		if (cartaActual.getColor().equals("Especiales")) {
			seleccionarSiguienteJugador();
		}
		if (!getJugadorActual().getNombre().equals("Jugador 1")) {
			jugarBot();
		}
	}

	public void cambiarColorAmarillo() {
		cartaActual.setColor("Amarillo");
		confirmacion = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a amarillo", null));
		if (cartaActual.getColor().equals("Especiales")) {
			seleccionarSiguienteJugador();
		}
		if (!getJugadorActual().getNombre().equals("Jugador 1")) {
			jugarBot();
		}
	}

	public void cambiarColorVerde() {
		cartaActual.setColor("Verde");
		confirmacion = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a verde", null));
		if (cartaActual.getColor().equals("Especiales")) {
			seleccionarSiguienteJugador();
		}
		if (!getJugadorActual().getNombre().equals("Jugador 1")) {
			jugarBot();
		}
	}

	public void jugarBot() {

		int r = 0;
		boolean jugable = false;

		while (r < jugadorActual.getCartas().size() && !jugable) {

			jugable = esCartaValida(jugadorActual.getCartas().get(r));

			if (jugable) {
				cartaJugada = jugadorActual.getCartas().get(r);
				setConfirmacion(false);
				System.out.println(cartaJugada.getColor() + cartaJugada.getNumero());
				if (cartaJugada.getColor().equals("Especiales")) {

					int ran = rand.nextInt(1, 5);

					if (ran == 1) {
						cambiarColorAzul();
					}
					if (ran == 2) {
						cambiarColorRojo();
					}
					if (ran == 3) {
						cambiarColorVerde();
					}
					if (ran == 4) {
						cambiarColorAmarillo();
					}

				}
				jugarCarta();
			}

			r++;

		}

		if (!jugable) {

			boolean jugable2 = false;

			while (!jugable2) {

				robarCarta();
				jugable2 = esCartaValida(jugadorActual.getCartas().get(r));

				if (jugable2) {
					cartaJugada = jugadorActual.getCartas().get(r);
					setConfirmacion(false);
					System.out.println(cartaJugada.getColor() + cartaJugada.getNumero());
					if (cartaJugada.getColor().equals("Especiales")) {

						int ran = rand.nextInt(1, 5);

						if (ran == 1) {
							cambiarColorAzul();
						}
						if (ran == 2) {
							cambiarColorRojo();
						}
						if (ran == 3) {
							cambiarColorVerde();
						}
						if (ran == 4) {
							cambiarColorAmarillo();
						}

					}
					jugarCarta();
				}

				r++;

			}

		}
	}

	public void confirmar() {
		if (!jugadorActual.getNombre().equals("Jugador 1")) {
			confirmacion = true;
		} else {
			confirmacion = false;
		}
	}

	public Carta getCartaJugada() {
		return cartaJugada;
	}

	public void setCartaJugada(Carta cartaJugada) {
		this.cartaJugada = cartaJugada;
	}

	public boolean isGanador() {
		return ganador;
	}

	public void setGanador(boolean ganador) {
		this.ganador = ganador;
	}
}
