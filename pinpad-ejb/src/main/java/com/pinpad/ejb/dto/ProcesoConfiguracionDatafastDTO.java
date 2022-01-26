package com.pinpad.ejb.dto;

import java.math.BigDecimal;

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
public class ProcesoConfiguracionDatafastDTO {

	private BigDecimal secuencia;
	private String cid;
	private String tipoMensaje;
	private String codigoRespuesta;
	private String mensajeRespuesta;
	
}
