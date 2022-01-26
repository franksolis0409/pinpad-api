/**
 * 
 */
package com.pinpad.ejb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author H P
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum TipoTarjeta {

	CREDIT("CREDIT"), 
	DEBIT("DEBIT"),
	CREDITO("CREDITO"), 
	DEBITO("DEBITO");
	
	private String name;
	
}
