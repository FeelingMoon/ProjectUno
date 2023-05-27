package co.edu.unbosque.persistence;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.BaseEncoding;

public class Imagen64 {
	
	public static String cargarImg(String rutaImg) {
		
		InputStream entradaStream = Imagen64.class.getResourceAsStream(rutaImg);
		byte[] img = null;
		
		try {
			img = entradaStream.readAllBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int length = img.length;
		
		int padding = length % 3 == 0 ? 0 : 3 - length % 3;
		
		byte[] paddedBytes = new byte[length + padding];
		System.arraycopy(img, 0, paddedBytes, 0, length);
		
		String base64Encoded = BaseEncoding.base64().encode(paddedBytes);
		
		return base64Encoded;
		
	}
	
}


