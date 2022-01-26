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
public class LecturaTarjetaDatafastDTO {

	private BigDecimal secuencia;
	private String cid;
	private String codigoRespuesta;
	private String tipoMensaje;
	private String redCorriente;
	private String redDiferido;
	private String numeroTarjeta;
	private String fechaVencimiento;
	private String respuestaPinPad;
	private String numTarjetaEncriptado;
	private String binTarjeta;
	
}
