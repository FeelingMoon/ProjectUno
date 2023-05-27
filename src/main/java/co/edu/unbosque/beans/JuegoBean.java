package co.edu.unbosque.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

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
	private Random rand;
	private List<Jugador> jugadores;
	private Jugador jugadorActual;
	private Carta cartaActual;
	private Stack<Carta> mazo;
	private String color;
	private String colorAux;
	private String numero;
	private Carta cartaJugada; // Agregamos una nueva propiedad para almacenar la carta jugada
	private boolean confirmacion;
	private boolean sentido;
	private boolean ganador;
	private Stack<Carta> jugadas;

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
		
		while(especial) {
			
			cartaActual = obtenerCartaAleatoria();
			jugadas.push(cartaActual);
			
			if(!cartaActual.getNumero().equals("+4") && !cartaActual.getNumero().equals("Multi") && !cartaActual.getNumero().equals("+2") &&
					!cartaActual.getNumero().equals("Reversa") && !cartaActual.getNumero().equals("Salto")) {
				especial = false;
			}
		}
		
		color = cartaActual.getColor();
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
					
					if(jugadorActual.getCartas().size() == 0) {
						ganador = true;
						jugadores.remove(jugadorActual);
					}
					
					jugadas.push(cartaJugada);
					System.out.println(cartaJugada.getColor() + cartaJugada.getNumero());
					seleccionarSiguienteJugador();
					
					if(cartaJugada.getNumero().equals("Reversa")) {
						sentido = !sentido;
					}
					if(cartaJugada.getNumero().equals("Salto")) {
						seleccionarSiguienteJugador();
					}
					if (cartaJugada.getNumero().equals("+2")) {
						robarCarta();
						robarCarta();
					}
					if (cartaJugada.getColor().equals("Especiales")) {
						setConfirmacion(true);
						if (cartaJugada.getNumero().equals("+4")) {
							robarCarta();
							robarCarta();
							robarCarta();
							robarCarta();
						}
					}
					
					color = cartaJugada.getColor();
					
					
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
		
		if(!getJugadorActual().getNombre().equals("Jugador 1")) {
			synchronized(color){
			while(color.equals("Especiales")) {
				try {
					color.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			jugarBot();
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
//				nueva.setImagen(Imagen64.cargarImg("../persistence/images/"+ color + "/" + numero + color + ".png"));
				mazo.push(nueva);

				if (numero != 0) {
					mazo.push(nueva);
				}
			}

			mazo.push(new Carta(color, "Salto"));
			mazo.push(new Carta(color, "Reversa"));
			mazo.push(new Carta(color, "+2"));
			mazo.push(new Carta(color, "Salto"));
			mazo.push(new Carta(color, "Reversa"));
			mazo.push(new Carta(color, "+2"));

		}
		for (int i = 0; i <= 3; i++) {
			mazo.push(new Carta("Especiales", "Multi"));
			mazo.push(new Carta("Especiales", "+4"));
		}

		return mazo;
	}

	public void seleccionarSiguienteJugador() {
		
		int indexActual = jugadores.indexOf(jugadorActual);
		int siguienteIndex = 0;
		
		if(sentido) {
		siguienteIndex = (indexActual + 1) % jugadores.size();
		}if(!sentido){
			if(jugadorActual.getNombre().equals("Jugador 1")) {
				siguienteIndex = jugadores.size()-1;
			}else {
			siguienteIndex = (indexActual-1);
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
					"Carta robada: " + cartaRobada.toString(), "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			if(jugadas.size() > 1){
				for(Carta aux: jugadas) {
					mazo.push(aux);
				}
				
				Carta actual = jugadas.pop();
				jugadas = new Stack<>();
				jugadas.push(actual);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"!Mazo revuelto!", "");
				FacesContext.getCurrentInstance().addMessage(null, message);
				
			}else {
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
		synchronized(color) {
		cartaActual.setColor("Azul");
		color = "Azul";
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a azul", null));
		color.notify();
		}

	}

	public void cambiarColorRojo() {
		synchronized(color){
		cartaActual.setColor("Rojo");
		color = "Rojo";
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a rojo", null));
		color.notify();
		}
	}

	public void cambiarColorAmarillo() {
		synchronized(color) {
		cartaActual.setColor("Amarillo");
		color = "Amarillo";
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a amarillo", null));
		color.notify();
		}
	}

	public void cambiarColorVerde() {
		synchronized(color) {
		cartaActual.setColor("Verde");
		color = "Verde";
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Se cambio de color a verde", null));
		color.notify();
		}
	}
	
	public void jugarBot() {
			
			int r = 0;
			boolean jugable = false;
			setConfirmacion(false);
			
			while(r < jugadorActual.getCartas().size() && !jugable) {
				
				jugable = esCartaValida(jugadorActual.getCartas().get(r));
				
				if(jugable) {
					cartaJugada = jugadorActual.getCartas().get(r);
					setConfirmacion(false);
					System.out.println(cartaJugada.getColor() + cartaJugada.getNumero());
					if(cartaJugada.getColor().equals("Especiales")) {
						
						int ran = rand.nextInt(1,5);
						
						if(ran == 1) {
							cambiarColorAzul();
						}
						if(ran == 2) {
							cambiarColorRojo();
						}
						if(ran == 3) {
							cambiarColorVerde();
						}
						if(ran == 4) {
							cambiarColorAmarillo();
						}
						
					}
					jugarCarta();
				}
				
				r++;
				
			}
			
			if(!jugable) {
				
				boolean jugable2 = false;
				
				while(!jugable2) {
					
					robarCarta();
					jugable2 = esCartaValida(jugadorActual.getCartas().get(r));
					
					if(jugable2) {
						cartaJugada = jugadorActual.getCartas().get(r);
						setConfirmacion(false);
						System.out.println(cartaJugada.getColor() + cartaJugada.getNumero());
						if(cartaJugada.getColor().equals("Especiales")) {
							
							int ran = rand.nextInt(1,5);
							
							if(ran == 1) {
								cambiarColorAzul();
							}
							if(ran == 2) {
								cambiarColorRojo();
							}
							if(ran == 3) {
								cambiarColorVerde();
							}
							if(ran == 4) {
								cambiarColorAmarillo();
							}
							
						}
						jugarCarta();
					}
					
					r++;
					
				}
				
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
