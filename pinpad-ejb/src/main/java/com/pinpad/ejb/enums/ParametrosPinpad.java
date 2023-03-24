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
public enum ParametrosPinpad {
	
	TIME_OUT_PINPAD("TIME_OUT_PINPAD"),
	MID_PINPAD("MID_PINPAD"),
	TID_PINPAD("TID_PINPAD"),
	ROUTE_SFTP_DATAFAST("ROUTE_SFTP_DATAFAST"),
	DIR_SFTP_DATAFAST("DIR_SFTP_DATAFAST"),
	PORT_SFTP_DATAFAST("PORT_SFTP_DATAFAST"),
	USER_SFTP_DATAFAST("USER_SFTP_DATAFAST"),
	PASSWORD_SFTP_DATAFAST("PASSWORD_SFTP_DATAFAST");
	
	private String name;

}
