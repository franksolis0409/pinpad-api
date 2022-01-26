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
public enum ModoLectura {

	MANUAL("MANUAL"), 
	BANDA("BANDA"),
	CHIP("CHIP"),
	FALLBACK_MANUAL("FALLBACK_MANUAL"),
	FALLBACK_BANDA("FALLBACK_BANDA"),
	CONTACTLESS("CONTACTLESS");
	
	private String name;
	
}
