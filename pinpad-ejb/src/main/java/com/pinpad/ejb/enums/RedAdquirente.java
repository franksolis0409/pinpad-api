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
public enum RedAdquirente {

	DATAFAST("DATAFAST"),
	MEDIANET("MEDIANET"),
	BANCO_DEL_AUSTRO("BANCO DEL AUSTRO");
	
	private String name;
	
}
