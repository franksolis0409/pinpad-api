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
public enum ParametrosGenerales {
	
	MID_PINPAD("MID_PINPAD"),
	TID_PINPAD("TID_PINPAD"),
	TIME_OUT_PINPAD("TIME_OUT_PINPAD");
	
	private String name;

}
