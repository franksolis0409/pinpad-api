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
public class ProcesoAnulacionPagoDatafastDTO {

	private BigDecimal secuencia;
	private String cid;
	private String codigoRespuesta;
	private String tipoMensaje;
	private String usuarioIngreso;
	private String tramaPos;
	private String codigoRed;
	private String codRespuestaAutorizador;
	private String respuestaPinPad;
	private String secuenciaTransaccion;
	private String lote;
	private String hora;
	private String fecha;
	private String numeroAutorizacion;
	private String terminalId;
	private String codBancoAdquirente;
	private String codModoLectura;
	private String tarjetaHabiente;
	private String aplicacionLabel;
	private String aidEmv;
	private String tipoCriptograma;
	private String verificacionPIN;
	private String arqc;
	private String tvr;
	private String tsi;
	private String numTarjetaTruncado;
	private String fechaVctoTarjeta;
	private String numTarjetaEncriptado;
	private Integer codBancoLiquidador;
	private String nombreBancoLiquidador;
	private Integer codBancoEmisor;
	private String nombreBancoEmisor;
	private BigDecimal codTarjeta;
	private String nombreTarjeta;
	private String tipoTarjeta;
	private String modoLectura;
	private Integer plazoDiferido;	
	private String tipoTransaccion;
	// valores
	private Double montoBaseConIva;
	private Double montoBaseSinIva;
	private Double subtotal;
	private Double valorIva;
	private Double subtotalBaseImponible;
	private Double valorInteres;
	private Double valorTotal;
	
	private String secuenciaTransaccionOriginal;
	
}
