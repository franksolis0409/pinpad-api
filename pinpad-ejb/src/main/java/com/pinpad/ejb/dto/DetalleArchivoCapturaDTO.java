/**
 * 
 */
package com.pinpad.ejb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author H P
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DetalleArchivoCapturaDTO {
	
	private String tid;
	private String fecha;
	private String hora;
	private String secuenciaTransaccion;
	private String numeroAutorizacion;
	private String lote;
	private Double valorTotal;
	private String tipoCredito;
	private Integer plazoDiferido;
	private Double valorIva;
	private String valorServicio;
	private String valorPropina;
	private Double valorInteres;
	private String montoFijo;
	private String valorIce;
	private String otrosImpuestos;
	private String cashOver;
	private Double montoBaseSinIva;
	private Double montoBaseConIva;
	
}
