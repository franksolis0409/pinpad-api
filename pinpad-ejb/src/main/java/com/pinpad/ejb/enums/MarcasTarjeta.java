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
public enum MarcasTarjeta {

	UNION_PAY("UNIONPAY"), 
	MASTERCARD("MASTERCARD"),
	VISA("VISA"), 
	DINERS("DINERS");
	
	private String name;
	
}
