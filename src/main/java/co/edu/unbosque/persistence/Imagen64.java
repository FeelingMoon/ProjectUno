package co.edu.unbosque.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

public class Imagen64 {
	
	public static String cargarImg(String rutaImg) {
		
		File entrada = new File(rutaImg);
		byte[] img = new byte[(int) entrada.length()];
		
		FileInputStream entradaStream;
		try {
			entradaStream = new FileInputStream(entrada);
			entradaStream.read(img);
			entradaStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Base64.getEncoder().encodeToString(img);
		
	}
	
}


