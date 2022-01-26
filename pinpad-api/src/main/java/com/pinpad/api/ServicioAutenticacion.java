/**
 * 
 */
package com.pinpad.api;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.pinpad.dto.ResponseOk;
import com.pinpad.dto.ResponseUnauthorized;
import com.pinpad.ejb.dto.AutenticacionDTO;
import com.pinpad.ejb.exceptions.BOException;
import com.pinpad.ejb.exceptions.UnauthorizedException;
import com.pinpad.ejb.util.GenericUtil;
import com.pinpad.ejb.util.MensajesUtil;
import com.pinpad.ejb.util.SecurityUtil;
import com.pinpad.exceptions.CustomExceptionHandler;
import com.pinpad.util.ServiciosEjb;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author H P
 *
 */
@Path("/v1/autenticacion")
public class ServicioAutenticacion {

	// Loggers
	private static final Logger logger = Logger.getLogger(ServicioAutenticacion.class.getName());

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@HeaderParam(HttpHeaders.AUTHORIZATION) String strAuthorization,
			@HeaderParam("Accept-Language") String strLanguage) throws BOException {

		// Valida que se envie (Authorization)
		GenericUtil.validarCampoRequerido(strAuthorization, "pin.campos.headerAutorizacion",
				MensajesUtil.validateSupportedLocale(strLanguage));

		// Obtiene las Credenciales de (Authorization) con el Token de Tipo (Basic)
		String[] arrCrendencialesAuth = SecurityUtil.obtenerBasicAuth(strAuthorization);
		String strUsername = null;
		String strPassword = null;
		try {
			strUsername = arrCrendencialesAuth[0];
			strPassword = arrCrendencialesAuth[1];
			AutenticacionDTO objAutenticacionDTO = ServiciosEjb.getAutenticacionEjb().userLogin(strUsername, strPassword);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objAutenticacionDTO)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		} catch (UnauthorizedException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			return Response.status(Status.UNAUTHORIZED).entity(new ResponseUnauthorized(e.getTranslatedMessage(strLanguage), null)).build();
		}
	}

}
