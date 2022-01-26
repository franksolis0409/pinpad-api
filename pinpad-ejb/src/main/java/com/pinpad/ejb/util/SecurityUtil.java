/**
 * 
 */
package com.pinpad.ejb.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.util.Base64;

import com.pinpad.ejb.enums.AuthenticationScheme;
import com.pinpad.ejb.exceptions.BOException;

/**
 * @author H P
 *
 */
public class SecurityUtil {

	/**
	 * 
	 */
	public SecurityUtil() {
		// TODO Auto-generated constructor stub
	}

	// Loggers
	private static final Logger logger = Logger.getLogger(SecurityUtil.class.getName());

	public static String[] obtenerBasicAuth(String auth) throws BOException {
		String[] values = null;
		try {
			String base64Credentials = auth.substring(AuthenticationScheme.BASIC.getName().length()).trim();
			String credentials = new String(Base64.decode(base64Credentials.getBytes()), Charset.forName("UTF-8"));
			values = credentials.split(":", 2);
			if (values.length == 2) {
				return values;
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException("pin.error.errorDecodeAuth");
		}
		return values;
	}

	public static String validarBearerToken(String auth) {
		String response = null;
		if (auth == null || auth.isEmpty()) {
			response = MensajesUtil.getMensaje("pin.error.tokenRequerido", MensajesUtil.Locale);
		}

		String token = null;
		if (auth == null || !auth.startsWith(AuthenticationScheme.BEARER.getName() + " ")) {
			response = MensajesUtil.getMensaje("pin.error.tokenRequerido", MensajesUtil.Locale);
		} else {
			token = auth.substring(AuthenticationScheme.BEARER.getName().length()).trim();
		}

		if (token == null || token.isEmpty()) {
			response = MensajesUtil.getMensaje("pin.error.tokenRequerido", MensajesUtil.Locale);
		}
		if (token != null && !token.isEmpty()) {
			if (!JWTUtil.tokenValido(token)) {
				response = MensajesUtil.getMensaje("pin.error.tokenIncorrecto", MensajesUtil.Locale);
			}
			if (JWTUtil.tokenExpirado(token)) {
				response = MensajesUtil.getMensaje("pin.error.tokenIncorrecto", MensajesUtil.Locale);
			}
		}
		return response;
	}

	public static boolean validarCredencialesPinPadWS(String strUsername, String strPassword) {
		boolean booResult = false;
		//logger.info("User: " + strUsername);
		//logger.info("Password: " + strPassword);
		if (strUsername != null && strPassword != null && strUsername.equals("wspinpad")
				&& strPassword.equals("W$P¡NP@DAUT#3NT¡C@TI0N"))
			booResult = true;
		return booResult;
	}

}
