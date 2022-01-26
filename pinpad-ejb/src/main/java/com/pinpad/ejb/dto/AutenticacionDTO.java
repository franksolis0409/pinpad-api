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
public class AutenticacionDTO {

	private String tokenType;
	private String accesToken;
	private String expires;

}
