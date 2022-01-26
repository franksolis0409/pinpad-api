/**
 * 
 */
package com.pinpad.ejb.util;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Strings;

/**
 * @author H P
 *
 */
public class ImpresionTextoUtil {

	static ArrayList<String> LineasCabecera = new ArrayList<>();
	static ArrayList<String> LineasDetalle = new ArrayList<>();
	static ArrayList<String> LineasPie = new ArrayList<>();

	/**
	 * Añade una linea de cabecera
	 *
	 * @author Frank Solis
	 * @param line
	 */
	public static void addCabecera(String line, Integer saltoLinea) {
		LineasCabecera.add(line + darSaltoLinea(saltoLinea));
	}

	/**
	 * Añade una linea de detalle
	 *
	 * @author Frank Solis
	 * @param line
	 */
	public static void addDetalle(String line, Integer saltoLinea) {
		LineasDetalle.add(line + darSaltoLinea(saltoLinea));
	}

	/**
	 * Añade una linea de pie
	 *
	 * @author Frank Solis
	 * @param line
	 */
	public static void addPieLinea(String line, Integer saltoLinea) {
		LineasPie.add(line + darSaltoLinea(saltoLinea));
	}

	/**
	 * Alinea texto al centro
	 *
	 * @author Frank Solis
	 * @param str
	 * @param width
	 * @param padWithChar
	 * @return
	 */
	public static String alineaCentro(String str, int width, String padWithChar) {
		// Trim the leading and trailing whitespace ...
		str = str != null ? str.trim() : "";
		int addChars = width - str.length();
		if (addChars < 0) {
			// truncate
			return str.subSequence(0, width).toString();
		}
		// Write the content ...
		int prependNumber = addChars / 2;
		int appendNumber = prependNumber;
		if ((prependNumber + appendNumber) != addChars) {
			++prependNumber;
		}
		final StringBuilder sb = new StringBuilder();
		// Prepend the pad character(s) ...
		while (prependNumber > 0) {
			sb.append(padWithChar);
			--prependNumber;
		}
		// Add the actual content
		sb.append(str);
		// Append the pad character(s) ...
		while (appendNumber > 0) {
			sb.append(padWithChar);
			--appendNumber;
		}
		return sb.toString();
	}

	/**
	 * Añade Saltos de Linea
	 *
	 * @author Frank Solis
	 * @param totalSaltos
	 * @return
	 */
	public static String darSaltoLinea(int totalSaltos) {
		return Strings.repeat("\n", totalSaltos);
	}

	/**
	 * Formateo Texto, quita caracteres especiales
	 *
	 * @author Frank Solis
	 * @param strCadenaToEvaluar
	 * @return
	 */
	public static String formatear(String strCadenaToEvaluar) {
		String strCaracteresEspeciales = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ"; // Cadena de caracteres original a
																				// sustituir.
		String strCaracteresNormales = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC"; // Cadena de caracteres ASCII que
																				// reemplazarán los originales.
		String strRespuesta = "";
		if (strCadenaToEvaluar != null) {
			strRespuesta = strCadenaToEvaluar;
			for (int i = 0; i < strCaracteresEspeciales.length(); i++) {
				// Reemplazamos los caracteres especiales.
				strRespuesta = strRespuesta.replace(strCaracteresEspeciales.charAt(i), strCaracteresNormales.charAt(i));
			}
		}
		return strRespuesta;
	}

	/**
	 * Agrega Separador de linea
	 *
	 * @author Frank Solis
	 * @param maximoAncho
	 * @param tipoSeparador
	 * @return
	 */
	public static String agregarSeparadorLinea(int maximoAncho, String tipoSeparador) {
		return Strings.repeat(tipoSeparador, maximoAncho);
	}

	/**
	 *
	 *
	 * @author Frank Solis
	 * @param string
	 * @param padWithChar
	 * @return
	 */
	public static String reemplazarCaracteresAlrededor(String string, String padWithChar) {
		return string.replaceAll("^\\s|\\s$|(?<=\\B)\\s|\\s(?=\\B)", padWithChar);
	}

	public static List<String> generarDocumento() {
		List<String> lsCadena = new ArrayList<String>();
		try {
			if (LineasCabecera != null && !LineasCabecera.isEmpty() && LineasCabecera.size() > 0)
				lsCadena.addAll(LineasCabecera);
			if (LineasDetalle != null && !LineasDetalle.isEmpty() && LineasDetalle.size() > 0)
				lsCadena.addAll(LineasDetalle);
			if (LineasPie != null && !LineasPie.isEmpty() && LineasPie.size() > 0)
				lsCadena.addAll(LineasPie);
		} finally {
			// Limpia variables
			LineasCabecera = null;
			LineasDetalle = null;
			LineasPie = null;
			// Inicializa variales
			LineasCabecera = new ArrayList<String>();
			LineasDetalle = new ArrayList<String>();
			LineasPie = new ArrayList<String>();
		}
		return lsCadena;
	}
	
	/**
	 * Añade espacios en blanco
	 *
	 * @author Frank Solis
	 * @param totalSaltos
	 * @return
	 */
	public static String darEspacioBlanco(int totalEspacios) {
		return Strings.repeat(" ", totalEspacios);
	}

}
