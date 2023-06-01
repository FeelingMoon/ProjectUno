package co.edu.unbosque.persistence;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.BaseEncoding;

/**
 * La clase Imagen64 proporciona métodos para cargar una imagen desde una ruta y
 * convertirla a una representación en base64.
 */
public class Imagen64 {
	
	/**
	 * Carga una imagen desde la ruta especificada y la convierte en una cadena en formato base64.
	 * 
	 * @param rutaImg La ruta de la imagen a cargar.
	 * @return Una cadena que representa la imagen en formato base64.
	 */
	public static String cargarImg(String rutaImg) {
		
		// Obtener un InputStream para la imagen desde la ruta
		InputStream entradaStream = Imagen64.class.getResourceAsStream(rutaImg);
		byte[] img = null;
		
		try {
			// Leer todos los bytes de la imagen desde el InputStream
			img = entradaStream.readAllBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Calcular el tamaño de la imagen
		int length = img.length;
		
		// Calcular la cantidad de bytes de relleno necesarios para que el tamaño sea múltiplo de 3
		int padding = length % 3 == 0 ? 0 : 3 - length % 3;
		
		// Crear un nuevo array de bytes con el tamaño ajustado y copiar los bytes de la imagen original
		byte[] paddedBytes = new byte[length + padding];
		System.arraycopy(img, 0, paddedBytes, 0, length);
		
		// Codificar los bytes en formato base64 utilizando la biblioteca Guava
		String base64Encoded = BaseEncoding.base64().encode(paddedBytes);
		
		// Devolver la cadena en formato base64 que representa la imagen
		return base64Encoded;
		
	}
	
}


