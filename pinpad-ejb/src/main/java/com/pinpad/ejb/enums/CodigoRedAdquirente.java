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
public enum CodigoRedAdquirente {

	DATAFAST(1),
	MEDIANET(2),
	BANCO_DEL_AUSTRO(3);
	
	private Integer codigo;
	
}
