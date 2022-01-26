/**
 * 
 */
package com.pinpad.ejb.util;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import com.pinpad.ejb.exceptions.BOException;

/**
 * @author H P
 *
 */
public class GenericUtil {

	/**
	 * Valida campo requerido con isEmpty() y lanza excepcion dinamica de campo.
	 * obligatorio.
	 * 
	 * @author Frank Solis
	 * @param objeto
	 * @param strNombreCampo
	 * @return BOException
	 * @throws BOException
	 */
	public static void validarCampoRequeridoBO(Object objeto, String strNombreCampo) throws BOException {
		if (ObjectUtils.isEmpty(objeto)) {
			throw new BOException("pin.warn.campoObligatorio", new Object[] { strNombreCampo });
		}
	}

	/**
	 * Valida campo requerido con isBlank() y lanza excepcion dinamica de campo
	 * obligatorio.
	 * 
	 * @author Frank Solis
	 * @param strCampoRequerido
	 * @param strNombreCampo
	 * @param locale
	 * @return NotAuthorizedException
	 */
	public static void validarCampoRequerido(String strCampoRequerido, String strNombreCampo, Locale locale) {
		if (StringUtils.isBlank(strCampoRequerido)) {
			throw new NotAuthorizedException(
					MessageFormat.format(MensajesUtil.getMensaje("pin.warn.campoObligatorio", locale),
							MensajesUtil.getMensaje(strNombreCampo, locale)),
					Status.UNAUTHORIZED);
		}
	}
	
	/**
	 * Convierte valor double a formato requerido para proceso de pagos pinpad.
	 * 
	 * @author Frank Solis
	 * @param douValor
	 * @return String
	 */
	public static String convertDoubleToStringFormatPP(Double douValor, Integer intLong) {
        DecimalFormat df = new DecimalFormat("0.00"); 
        String strValor = douValor != 0.00 ? df.format(douValor).replace(",", "") : "0";
        StringBuilder sb = new StringBuilder();
        while (sb.length() + strValor.length() < intLong) {
            sb.append("0");
        }
        return sb.append(strValor).toString();
	}
	
	/**
	 * Convierte valor int a formato requerido para proceso de pagos pinpad.
	 * 
	 * @author Frank Solis
	 * @param intValor
	 * @return String
	 */
	public static String convertIntegerToStringFormat(Integer intValor, Integer intLong) {
        String strValor = intValor != 0 ? intValor.toString() : "0";
        StringBuilder sb = new StringBuilder();
        while (sb.length() + strValor.length() < intLong) {
            sb.append("0");
        }
        return sb.append(strValor).toString();
	}
	
	/**
	 * Devuelve un booleano si encuentra coincidencias de la variable en la cadena recibida.
	 * 
	 * @author Frank Solis
	 * @param strCadena
	 * @param strRegex
	 * @return Boolean
	 */
	public static Boolean contieneValorString(String strCadena, String strRegex) {
		try {
			Pattern pattern = Pattern.compile(".*" + strRegex + ".*");
	        if (pattern.matcher(strCadena.toUpperCase()).matches())
	            return true;
	        else
	            return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * Convierte valor nnúmero de lote a formato requerido para proceso de control pinpad.
	 * 
	 * @author Frank Solis
	 * @param intLote
	 * @return String
	 */
	public static String convierteNumeroLoteReferencia(Integer intLote) {
        String strValor = intLote >= 0 ? intLote.toString() : "000000";
        StringBuilder sb = new StringBuilder();
        while (sb.length() + strValor.length() < 6) {
            sb.append("0");
        }
        return sb.append(strValor).toString();
	}
	
	/**
	 * Validar formato de fecha dd/mm/yyyy
	 * 
	 * @author Frank Solis
	 * @param strFecha
	 * @return
	 * @throws ParseException
	 */

	public static boolean validarFormatoFecha(String strFecha) {
		String regx = "(^(((0[1-9]|1[0-9]|2[0-8])[/](0[1-9]|1[012]))|((29|30|31)[/](0[13578]|1[02]))|((29|30)[/](0[4,6,9]|11)))[/](19|[2-9][0-9])\\d\\d$)|(^29[/]02[/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)";
		Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(strFecha);
		return matcher.find();
	}
	
	/**
	 * Valida si la fecha indicada es futura respecto a otra fecha.
	 * 
	 * @author Frank Solis.
	 * @param fechaFin fecha en Date
	 * @param fechaIni fecha en Date
	 * @return
	 */
	public static boolean esRangoFechaFutura(Date fechaFin, Date fechaIni) {
		return fechaFin.before(fechaIni);
	}
	
	/**
	 * Convierte valor double a formato requerido para consulta de transacciones.
	 * 
	 * @author Frank Solis
	 * @param douValor
	 * @param intLong
	 * @return String
	 */
	public static String convertDoubleToStringFormat(Double douValor, Integer intLong) {
        DecimalFormat df = new DecimalFormat("0.00"); 
        String strValor = douValor != 0.00 ? df.format(douValor).replace(".", "") : "0";
        StringBuilder sb = new StringBuilder();
        while (sb.length() + strValor.length() < intLong) {
            sb.append("0");
        }
        return sb.append(strValor).toString();
	}

}
