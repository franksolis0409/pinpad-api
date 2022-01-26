/**
 * 
 */
package com.pinpad.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

import javax.ws.rs.NameBinding;

/**
 * @author H P
 *
 */

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, METHOD })
public @interface Secure {

}
