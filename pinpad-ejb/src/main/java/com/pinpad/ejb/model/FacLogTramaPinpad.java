/**
 * 
 */
package com.pinpad.ejb.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author H P
 *
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "FAC_LOG_TRAMA_PINPAD")
public class FacLogTramaPinpad {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAC_SEQ_TRAMA_PINPAD")
	@SequenceGenerator(sequenceName = "FAC_SEQ_TRAMA_PINPAD", allocationSize = 1, name = "FAC_SEQ_TRAMA_PINPAD")
	@NotNull
	@Column(name = "SECUENCIA")
	private Long secuencia;
	
//	@JoinColumn(name = "SECUENCIA_LOG_ORIGINAL", referencedColumnName = "SECUENCIA")
//	@ManyToOne(optional = true, fetch = FetchType.LAZY)
//	private FacLogTramaPinpad facLogTramaPinpadOriginal;

	@NotNull
	@Size(max = 30)
	@Column(name = "CID")
	private String cid;

	@Column(name = "TIPO_MENSAJE")
	private String tipoMensaje;
	
	@Column(name = "CODIGO_RESPUESTA")
	private String codigoRespuesta;

	@Column(name = "COD_RESPUESTA_AUTORIZADOR")
	private String codRespuestaAutorizador;

	@Column(name = "CODIGO_RED")
	private String codigoRed;

	@Column(name = "RESPUESTA_PINPAD")
	private String respuestaPinPad;

	@Column(name = "SECUENCIA_TRANSACCION")
	private String secuenciaTransaccion;

	@Column(name = "LOTE")
	private String lote;	

	@Column(name = "HORA")
	private String hora;	

	@Column(name = "FECHA")
	private String fecha;
	
	@Column(name = "NUMERO_AUTORIZACION")
	private String numeroAutorizacion;

	@Column(name = "TERMINAL_ID")
	private String terminalId;

	@Column(name = "COMERCIO_ID")
	private String comercioId;
	
	@Column(name = "MENSAJE_IMPRESION")
	private String mensajeImpresion;

	@Column(name = "COD_BANCO_ADQUIRENTE")
	private String codBancoAdquirente;

	@Column(name = "BANCO_EMISOR")
	private String bancoEmisor;

	@Column(name = "GRUPO_TARJETA")
	private String grupoTarjeta;

	@Column(name = "COD_MODO_LECTURA")
	private String codModoLectura;

	@Column(name = "TARJETA_HABIENTE")
	private String tarjetaHabiente;

	@Column(name = "APLICACION_LABEL")
	private String aplicacionLabel;
	
	@Column(name = "AID_EMV")
	private String aidEmv;
	
	@Column(name = "TIPO_CRIPTOGRAMA")
	private String tipoCriptograma;
	
	@Column(name = "VERIFICACION_PIN")
	private String verificacionPIN;
	
	@Column(name = "ARQC")
	private String arqc;
	
	@Column(name = "TVR")
	private String tvr;
	
	@Column(name = "TSI")
	private String tsi;
	
	@Column(name = "NUM_TARJETA_TRUNCADO")
	private String numTarjetaTruncado;
	
	@Column(name = "FECHA_VCTO_TARJETA")
	private String fechaVctoTarjeta;
	
	@Column(name = "NUM_TARJETA_ENCRIPTADO")
	private String numTarjetaEncriptado;

	@Column(name = "TRAMA_POS")
	private String tramaPos;
	
	@Column(name = "COD_BANCO_LIQUIDADOR")
	private Integer codBancoLiquidador;
	
	@Column(name = "COD_BANCO_EMISOR")
	private Integer codBancoEmisor;
	
	@Column(name = "FECHA_INGRESO", insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaIngreso;
	
	@Size(max = 100)
	@Column(name = "USUARIO_INGRESO")
	private String usuarioIngreso;
	
	@Column(name = "TRAMA_REQUEST")
	private String tramaRequest;
	
	@Column(name = "PLAZO_DIFERIDO")
	private Integer plazoDiferido;
	
	@Column(name = "COD_TIPO_TRANSACCION")
	private String tipoTransaccion;
	
	@Column(name = "COD_TRANSACCION_ORIGINAL")
	private String codTransaccionOriginal;	
	
	@Column(name = "MONTO_BASE_CON_IVA")
	private Double montoBaseConIva;
	
	@Column(name = "MONTO_BASE_SIN_IVA")
	private Double montoBaseSinIva;
	
	@Column(name = "SUBTOTAL")
	private Double subtotal;
	
	@Column(name = "VALOR_IVA")
	private Double valorIva;
	
	@Column(name = "SUBTOTAL_BASE_IMPONIBLE")
	private Double subtotalBaseImponible;
	
	@Column(name = "VALOR_INTERES")
	private Double valorInteres;
	
	@Column(name = "VALOR_TOTAL")
	private Double valorTotal;
	
	@Column(name = "TIPO_TARJETA")
	private String tipoTarjeta;
	
	@Column(name = "MODO_LECTURA")
	private String modoLectura;

	// Nuevos Campos
	@Column(name = "RED_CORRIENTE")
	private String redCorriente;
	
	@Column(name = "RED_DIFERIDO")
	private String redDiferido;
	
	@Column(name = "BIN_TARJETA")
	private String binTarjeta;
	
	@Column(name = "ERROR")
	private String error;
	
	@Column(name = "NOMBRE_BANCO_LIQUIDADOR")
	private String nombreBancoLiquidador;
	
	@Column(name = "NOMBRE_BANCO_EMISOR")
	private String nombreBancoEmisor;
	
	@Column(name = "COD_TARJETA")
	private BigDecimal codTarjeta;
	
	@Column(name = "NOMBRE_TARJETA")
	private String nombreTarjeta;
	
	@Column(name = "MID")
	private String mid;

	@Column(name = "TID")
	private String tid;
	
	@Column(name = "CODIGO_DIFERIDO")
	private String codigoDiferido;
	
	@Column(name = "ERROR_INTERNO")
	private String errorInterno;
}
