/**
 * 
 */
package com.pinpad.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.pinpad.ejb.bo.IAutenticacionBO;
import com.pinpad.ejb.bo.IDatafastBO;

/**
 * @author H P
 *
 */
public class ServiciosEjb {

	private static final Logger logger = Logger.getLogger(ServiciosEjb.class.getName());

	public static final String jndiAutenticacionEjb = "java:global/pinpad-api/AutenticacionBOImpl!com.pinpad.ejb.bo.IAutenticacionBO";
	public static final String jndiDatafastEjb = "java:global/pinpad-api/DatafastBOImpl!com.pinpad.ejb.bo.IDatafastBO";

	/**
	 * Retorna EJB de Datafast.
	 * 
	 * @return
	 */
	public static IDatafastBO getDatafastEjb() {
		Context context;
		try {
			context = new InitialContext();
			return (IDatafastBO) context.lookup(jndiDatafastEjb);
		} catch (NamingException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
		}
		return null;
	}
	
	/**
	 * Retorna EJB de Autenticacion.
	 * 
	 * @return
	 */
	public static IAutenticacionBO getAutenticacionEjb() {
		Context context;
		try {
			context = new InitialContext();
			return (IAutenticacionBO) context.lookup(jndiAutenticacionEjb);
		} catch (NamingException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
		}
		return null;
	}

}
