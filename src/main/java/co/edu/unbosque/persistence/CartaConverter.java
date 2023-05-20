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
public class CartaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}

		String[] partes = value.split("-");
		if (partes.length != 2) {
			throw new ConverterException("Formato de carta inválido: " + value);
		}

		String color = partes[0];
		String numero = partes[1];
		JOptionPane.showMessageDialog(null, new Carta(color, numero).toString());
		return new Carta(color, numero);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null || !(value instanceof Carta)) {
			return null;
		}

		Carta carta = (Carta) value;

		String representacion = carta.getColor() + "-" + carta.getNumero();

		return representacion;
	}
}
