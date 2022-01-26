/**
 * 
 */
package com.pinpad.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.ObjectUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.core.Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.pinpad.ejb.util.MensajesUtil;
import com.pinpad.ejb.util.SecurityUtil;

/**
 * @author H P
 *
 */
@Provider
@Secure
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

	/**
	 * 
	 */
	public SecurityFilter() {
		// TODO Auto-generated constructor stub
	}

	// Loggers
	private static final Logger logger = Logger.getLogger(SecurityFilter.class.getName());

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Decodificar y Validar Bearer Token
		String strResultToken = null;
		try {
			strResultToken = SecurityUtil.validarBearerToken(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new ClientErrorException(MensajesUtil.getMensaje("pin.error.tokenRequerido", MensajesUtil.Locale),
					Status.UNAUTHORIZED, e);
		}
		if (!ObjectUtils.isEmpty(strResultToken)) {
			logger.log(Level.SEVERE, "Error: " + strResultToken);
			throw new ClientErrorException(strResultToken, Status.UNAUTHORIZED, null);
		}
	}

}
