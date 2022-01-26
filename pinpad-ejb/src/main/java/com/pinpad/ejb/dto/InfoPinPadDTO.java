/**
 * 
 */
package com.pinpad.ejb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author H P
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InfoPinPadDTO {
	
	private String ipPinpad;
	private String puertoPinpad;
	private String macPinpad;
	private String seriePinpad;
	private String mid;
	private String tid;
	
}
