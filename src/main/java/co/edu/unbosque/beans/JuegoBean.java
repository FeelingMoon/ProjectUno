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

/**
 * Esta clase representa un bean para el juego UNO.
 * Almacena información sobre los jugadores, el mazo de cartas, la carta actual, etc.
 */

@SuppressWarnings("serial")
@Named("juego")
@ViewScoped
public class JuegoBean implements Serializable {
	private Random rand;// Generador de números aleatorios
	private List<Jugador> jugadores;// Lista de jugadores
	private Jugador jugadorActual;// Jugador actual
	private Jugador jugadorAnt;// Jugador anterior
	private Carta cartaActual;// Carta actual
	private Stack<Carta> mazo;// Mazo de cartas
	private String colorAux;// Color auxiliar
	
	/**
     * Obtiene el mazo de cartas.
     * @return El mazo de cartas
     */
	public Stack<Carta> getMazo() {
		return mazo;
	}

	 /**
     * Establece el mazo de cartas.
     * @param mazo El mazo de cartas a establecer
     */
	public void setMazo(Stack<Carta> mazo) {
		this.mazo = mazo;
	}

	 /**
     * Obtiene las cartas jugadas.
     * @return Las cartas jugadas
     */
	public Stack<Carta> getJugadas() {
		return jugadas;
	}

	/**
     * Establece las cartas jugadas.
     * @param jugadas Las cartas jugadas a establecer
     */
	public void setJugadas(Stack<Carta> jugadas) {
		this.jugadas = jugadas;
	}

	private String numero;// Número
	private Carta cartaJugada; // Agregamos una nueva propiedad para almacenar la carta jugada
	private boolean confirmacion;// Confirmación
	private boolean sentido;// Sentido del juego
	private boolean ganador;// Ganador
	private Stack<Carta> jugadas;// Cartas jugadas
	private String nombre;// Nombre del jugador

	/**
     * Obtiene el nombre del jugador.
     * @return El nombre del jugador
     */
	public String getNombre() {
		return nombre;
	}

	  /**
     * Establece el nombre del jugador.
     * @param nombre El nombre del jugador a establecer
     */
	public void setNombre(String nombre) {
		jugadores.get(0).setNombre(nombre);
		this.nombre = nombre;
	}

	/**
     * Verifica si se ha confirmado algo.
     * @return true si se ha confirmado algo, false de lo contrario
     */
	public boolean isConfirmacion() {
		return confirmacion;
	}

	/**
     * Obtiene el color auxiliar.
     * @return El color auxiliar
     */
	public String getColorAux() {
		return colorAux;
	}

	 /**
     * Establece el color auxiliar.
     * @param colorAux El color auxiliar a establecer
     */
	public void setColorAux(String colorAux) {
		this.colorAux = colorAux;
	}

	 /**
     * Establece la confirmación.
     * @param confirmacion La confirmación a establecer
     */
	public void setConfirmacion(boolean confirmacion) {
		this.confirmacion = confirmacion;
	}

	/**
	 * Reinicia el juego redireccionando a la página de inicio.
	 */
	public void reiniciarJuego() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

		try {

			externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * Inicializa el juego. Se ejecuta después de que se crea una instancia de la clase.
	 */
	@PostConstruct
	public void init() {
		// Inicializar el juego
		jugadores = new ArrayList<>();
		jugadores.add(new Jugador("Jugador 1"));
		jugadores.add(new Jugador("Jugador 2"));
		jugadores.add(new Jugador("Jugador 3"));

		mazo = generarMazo();// Genera un nuevo mazo de cartas
		jugadas = new Stack<>();
		rand = new Random();
		Collections.shuffle(mazo);// Baraja el mazo de cartas de forma aleatoria
		repartirCartas();// Reparte las cartas a los jugadores
		jugadorAnt = jugadores.get(jugadores.size() - 1);
		jugadorActual = jugadores.get(0); // Comienza el juego con el primer jugador
		boolean especial = true;

		while (especial) {

			cartaActual = obtenerCartaAleatoria();// Obtiene una carta aleatoria
			jugadas.push(cartaActual);
			
			// Verifica si la carta actual no es una carta especial
			if (!cartaActual.getNumero().equals("+4") && !cartaActual.getNumero().equals("Multi")
					&& !cartaActual.getNumero().equals("+2") && !cartaActual.getNumero().equals("Reversa")
					&& !cartaActual.getNumero().equals("Salto")) {
				especial = false;// Se encontró una carta no especial, finaliza el bucle
			}
		}

		colorAux = "";
		ganador = false;
		confirmacion = false;
		sentido = true;

	}
	
	/**
	 * Obtiene una carta aleatoria del mazo.
	 * @return La carta obtenida
	 */
	public Carta obtenerCartaAleatoria() {
		return mazo.pop();// Obtiene y devuelve la carta en la parte superior del mazo
	}

	/**
	 * Obtiene la lista de jugadores.
	 * @return La lista de jugadores
	 */
	public List<Jugador> getJugadores() {
		return jugadores;// Devuelve la lista de jugadores
	}

	/**
	 * Obtiene la carta actual.
	 * @return La carta actual
	 */
	public Carta getCartaActual() {
		return cartaActual;// Devuelve la carta actual
	}

	/**
	 * Obtiene el jugador actual.
	 * @return El jugador actual
	 */
	public Jugador getJugadorActual() {
		return jugadorActual;// Devuelve el jugador actual
	}

	/**
	 * Juega la carta seleccionada por el jugador actual.
	 */
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
						confirmar(jugadorActual);
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
					if(jugadorActual.getNombre().equals("Jugador 2")) {
						if(cartaJugada.getColor().equals("Especiales")) {
							cambiarColorBot(rand.nextInt(1, 5));
							if(getJugadorActual().getNombre().equals("Jugador 3")) {
							jugarBot();
							}
						}
					}

					if (!getJugadorActual().getNombre().equals("Jugador 1")) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Carta jugada: " + jugadorActual.getNombre() + " " + cartaJugada.toString(), ""));
					}

				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La carta seleccionada no es vÃ¡lida", null));
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
	
	/**
	 * Obtiene el número.
	 * @return El número
	 */
	public String getNumero() {
		return numero;// Devuelve el número
	}

	/**
	 * Establece el número.
	 * @param numero El número a establecer
	 */
	public void setNumero(String numero) {
		this.numero = numero;// Establece el número
	}

	/**
	 * Reparte las cartas a los jugadores.
	 */
	public void repartirCartas() {
		int cartasPorJugador = 7; // Repartir cuatro cartas a cada jugador

		for (Jugador jugador : jugadores) {
			jugador.setCartas(new ArrayList<>()); // Limpiar las cartas anteriores del jugador
			for (int i = 0; i < cartasPorJugador; i++) {
				Carta carta = mazo.pop(); // Tomar la carta del mazo de repartir
				jugador.getCartas().add(carta);// Agregar la carta al jugador
			}
		}
	}

	/**
	 * Genera el mazo de cartas.
	 * @return El mazo de cartas generado
	 */
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

		return mazo;// Devuelve el mazo de cartas generado
	}

	/**
	 * Selecciona al siguiente jugador en el turno de juego.
	 */
	public void seleccionarSiguienteJugador() {

		int indexActual = jugadores.indexOf(jugadorActual);// Índice del jugador actual en la lista de jugadores
		int siguienteIndex = 0;// Índice del siguiente jugador

		if (sentido) {
			siguienteIndex = (indexActual + 1) % jugadores.size();// Calcula el siguiente índice en sentido normal
		}
		if (!sentido) {
			if (jugadorActual.equals(jugadores.get(0))) {
				siguienteIndex = jugadores.size() - 1;// Si es el primer jugador en sentido inverso, selecciona el último jugador
			} else {
				siguienteIndex = (indexActual - 1);// Calcula el siguiente índice en sentido inverso
			}
		}

		try {
		jugadorAnt = jugadores.get(indexActual);// Almacena el jugador actual como jugador anterior
		jugadorActual = jugadores.get(siguienteIndex);// Establece el siguiente jugador como jugador actual
		}catch(IndexOutOfBoundsException e) {
			ganador = true;// Si ocurre una excepción por índice fuera de rango, marca que hay un ganador
		}
		

		if (jugadorActual.getIndiceCartaActual() >= jugadorActual.getCartas().size()) {
			jugadorActual.setIndiceCartaActual(0);// Reinicia el índice de la carta actual del jugador si es mayor al tamaño de sus cartas
		}
	}

	/**
	 * Roba una carta del mazo y la añade al jugador actual.
	 */
	public void robarCarta() {
		Jugador jugadorActual = getJugadorActual();

		if (!mazo.isEmpty()) {
			Carta cartaRobada = mazo.pop(); // Tomar la carta del mazo de repartir
			jugadorActual.getCartas().add(cartaRobada);// Añadir la carta robada al jugador actual
			if(jugadorActual.getNombre().equals("Jugador 1")) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Carta robada: " + cartaRobada.toString(), "");
			FacesContext.getCurrentInstance().addMessage(null, message); // Mostrar mensaje en la interfaz para el jugador 1
			}
		} else {
			if (jugadas.size() > 1) {
				for (Carta aux : jugadas) {
					mazo.push(aux);// Devolver las cartas jugadas al mazo
				}

				Carta actual = jugadas.pop();
				jugadas = new Stack<>();
				jugadas.push(actual);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "!Mazo revuelto!", "");
				FacesContext.getCurrentInstance().addMessage(null, message);// Mostrar mensaje en la interfaz sobre el mazo revuelto
				robarCarta();
				
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "No quedan cartas en el mazo", null));
						// Mostrar mensaje en la interfaz de que no quedan cartas en el mazo
			}
		}
	}

	/**
	 * Verifica si una carta es válida para ser jugada.
	 *
	 * @param carta La carta a verificar.
	 * @return true si la carta es válida, false en caso contrario.
	 */
	private boolean esCartaValida(Carta carta) {
		if (carta.getColor().equals(cartaActual.getColor()) || carta.getNumero().equals(cartaActual.getNumero())) {
			return true;
		}

		if (carta.getColor().equals("Especiales")) {
			return true;
		}

		return false;
	}

	/**
	 * Cambia el color actual a azul.
	 */
	public void cambiarColorAzul() {
		cartaActual.setColor("Azul");
		confirmacion = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a azul", null));
		if (!getJugadorActual().getNombre().equals("Jugador 1")) {
			jugarBot();
		}
	}

	/**
	 * Cambia el color actual a rojo.
	 */
	public void cambiarColorRojo() {
		cartaActual.setColor("Rojo");
		confirmacion = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a rojo", null));
		if (!getJugadorActual().getNombre().equals("Jugador 1")) {
			jugarBot();
		}
	}

	/**
	 * Cambia el color actual a amarillo.
	 */
	public void cambiarColorAmarillo() {
		cartaActual.setColor("Amarillo");
		confirmacion = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a amarillo", null));
		if (!getJugadorActual().getNombre().equals("Jugador 1")) {
			jugarBot();
		}
	}

	/**
	 * Cambia el color actual a verde.
	 */
	public void cambiarColorVerde() {
		cartaActual.setColor("Verde");
		confirmacion = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a verde", null));
		if (!getJugadorActual().getNombre().equals("Jugador 1")) {
			jugarBot();
		}
	}


	/**
	 * Realiza la jugada del jugador automático (bot).
	 */
	public void jugarBot() {

		int r = 0;
		boolean jugable = false;

		while (r < getJugadorActual().getCartas().size() && !jugable) {

			jugable = esCartaValida(getJugadorActual().getCartas().get(r));

			if (jugable) {
				cartaJugada = getJugadorActual().getCartas().get(r);
				setConfirmacion(false);
				
				jugarCarta();
				
				if (cartaJugada.getColor().equals("Especiales")) {

					int ran = rand.nextInt(1, 5);
					cambiarColorBot(ran);
					
				}
				
			}

			r++;

		}

		if (!jugable) {

			boolean jugable2 = false;

			while (!jugable2) {

				robarCarta();
				jugable2 = esCartaValida(getJugadorActual().getCartas().get(r));

				if (jugable2) {
					cartaJugada = getJugadorActual().getCartas().get(r);
					setConfirmacion(false);
					
					jugarCarta();
					
					if (cartaJugada.getColor().equals("Especiales")) {

						int ran = rand.nextInt(1, 5);
						cambiarColorBot(ran);

					}
					
				}

				r++;

			}

		}
	}

	/**
	 * Establece la confirmación del jugador.
	 *
	 * @param jug El jugador para confirmar.
	 */
	public void confirmar(Jugador jug) {
		if (jug.getNombre().equals("Jugador 1")) {
			confirmacion = true;
		} else {
			confirmacion = false;
		}
	}

	/**
	 * Cambia el color de la carta jugada por el bot.
	 *
	 * @param ran El número aleatorio para determinar el color.
	 */
	public void cambiarColorBot(int ran) {
		// TODO Auto-generated method stub
		 if(ran == 1) {
			 cartaJugada.setColor("Azul");
		 }
		 if(ran == 2) {
			 cartaJugada.setColor("Rojo");
		 }
		 if(ran == 3) {
			 cartaJugada.setColor("Amarillo");
		 }
		 if(ran == 4) {
			 cartaJugada.setColor("Verde");
		 }
	}

	/**
	 * Obtiene la carta jugada.
	 *
	 * @return La carta jugada.
	 */
	public Carta getCartaJugada() {
		return cartaJugada;
	}

	/**
	 * Establece la carta jugada.
	 *
	 * @param cartaJugada La carta jugada.
	 */
	public void setCartaJugada(Carta cartaJugada) {
		this.cartaJugada = cartaJugada;
	}

	/**
	 * Verifica si hay un ganador.
	 *
	 * @return true si hay un ganador, false en caso contrario.
	 */
	public boolean isGanador() {
		return ganador;
	}

	/**
	 * Establece el estado de ganador.
	 *
	 * @param ganador El estado de ganador.
	 */
	public void setGanador(boolean ganador) {
		this.ganador = ganador;
	}
	
	/**
	 * Obtiene el jugador anterior.
	 *
	 * @return El jugador anterior.
	 */
	public Jugador getJugadorAnt() {
		return this.jugadorAnt;
	}
}