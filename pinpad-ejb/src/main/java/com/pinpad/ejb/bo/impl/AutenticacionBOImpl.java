/**
 * 
 */
package com.pinpad.ejb.bo.impl;

import javax.ejb.Stateless;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.pinpad.ejb.bo.IAutenticacionBO;
import com.pinpad.ejb.dto.AutenticacionDTO;
import com.pinpad.ejb.enums.AuthenticationScheme;
import com.pinpad.ejb.exceptions.BOException;
import com.pinpad.ejb.exceptions.UnauthorizedException;
import com.pinpad.ejb.util.GenericUtil;
import com.pinpad.ejb.util.JWTUtil;
import com.pinpad.ejb.util.SecurityUtil;

/**
 * @author H P
 *
 */
@Stateless
public class AutenticacionBOImpl implements IAutenticacionBO {

	private static final Logger logger = Logger.getLogger(AutenticacionBOImpl.class.getName());

	@Override
	public AutenticacionDTO userLogin(String strUsuario, String strClave)
			throws BOException, UnauthorizedException {
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(strUsuario, "pin.campos.usuario");
		GenericUtil.validarCampoRequeridoBO(strClave, "pin.campos.clave");
		AutenticacionDTO objAuthentication = new AutenticacionDTO();
		// Valida Credenciales
		if (SecurityUtil.validarCredencialesPinPadWS(strUsuario, strClave)) {
			// Asigna Tiempo de 1 dia
//			long ttTiempoValido = 1000 * 60 * 60 * 24 * 1;
			// Asigna Tiempo de 1 hora
			long ttTiempoValido = 1000 * 60 * 60 * 1;
			// Asignamos valor para retorna Token de tipo (Bearer)			
			objAuthentication.setTokenType(AuthenticationScheme.BEARER.getName());
			objAuthentication.setExpires("1 hora");
			try {
				objAuthentication.setAccesToken(JWTUtil.createJWT(strUsuario, ttTiempoValido));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "Exception: " + e);
				throw new BOException(e.getMessage(), e.getCause());
			}
			return objAuthentication;	
		} else
			throw new UnauthorizedException("pin.error.credencialesIncorrectas");
	}

}
