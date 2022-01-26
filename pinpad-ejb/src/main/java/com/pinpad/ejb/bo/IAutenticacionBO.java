/**
 * 
 */
package com.pinpad.ejb.bo;

import javax.ejb.Local;

import com.pinpad.ejb.dto.AutenticacionDTO;
import com.pinpad.ejb.exceptions.BOException;
import com.pinpad.ejb.exceptions.UnauthorizedException;

/**
 * @author H P
 *
 */
@Local
public interface IAutenticacionBO {
	
	/**
	 * Autenticación PinPad.
	 * 
	 * @author Frank Solis
	 * @param strUsuario
	 * @param strClave
	 * @return
	 * @throws BOException
	 */
	AutenticacionDTO userLogin(String strUsuario, String strClave)
			throws BOException, UnauthorizedException;

}
