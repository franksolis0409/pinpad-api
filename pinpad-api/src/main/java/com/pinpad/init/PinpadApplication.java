/**
 * 
 */
package com.pinpad.init;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.pinpad.api.ServicioAutenticacion;
import com.pinpad.api.ServicioDatafast;

/**
 * @author H P
 *
 */
@ApplicationPath("/*")
public class PinpadApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public PinpadApplication() {
		singletons.add(new ServicioDatafast());
		singletons.add(new ServicioAutenticacion());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
