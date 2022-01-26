/**
 * 
 */
package com.pinpad.ejb.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author H P
 *
 */
@RequiredArgsConstructor
@Getter
public enum TipoArchivoEnum {
	PDF("application/pdf", "pdf"), 
	XLS("application/vnd.ms-excel", "xls"),
	TXT("text/plain", "txt"),
	PNG("image/png","png"),
	VER("text/plain", "VER");

	private final String contentType;
	private final String extension;
}
