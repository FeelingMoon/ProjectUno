package co.edu.unbosque.persistence;

import javax.swing.JOptionPane;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@ApplicationScoped
@FacesConverter("cartaConverter")
/**
 * La clase CartaConverter es un convertidor utilizado para convertir objetos Carta a cadenas y viceversa.
 */
public class CartaConverter implements Converter {

	@Override
	/**
	 * Convierte una cadena en un objeto Carta.
	 *
	 * @param context   El contexto FacesContext.
	 * @param component El componente UIComponent asociado.
	 * @param value     La cadena a convertir.
	 * @return El objeto Carta resultante.
	 * @throws ConverterException Si el formato de la cadena no es válido.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}

		String[] partes = value.split("-");
		if (partes.length != 2) {
			throw new ConverterException("Formato de carta invÃ¡lido: " + value);
		}

		String color = partes[0];
		String numero = partes[1];
		JOptionPane.showMessageDialog(null, new Carta(color, numero).toString());
		return new Carta(color, numero);
	}

	@Override
	/**
	 * Convierte un objeto Carta en una cadena.
	 *
	 * @param context   El contexto FacesContext.
	 * @param component El componente UIComponent asociado.
	 * @param value     El objeto Carta a convertir.
	 * @return La representación en forma de cadena del objeto Carta.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null || !(value instanceof Carta)) {
			return null;
		}

		Carta carta = (Carta) value;

		String representacion = carta.getColor() + "-" + carta.getNumero();

		return representacion;
	}
}
