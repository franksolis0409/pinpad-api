/**
 * 
 */
package com.pinpad.ejb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "FAC_PINPAD_BIN_TARJ_DATAFAST")
public class FacPinpadBinTarjDatafast implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@NotNull
	@Column(name = "CODIGO_BIN")
	private BigDecimal codigoBin;
	
	@Size(max = 200)
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Size(max = 500)
	@Column(name = "NOMBRE_BANCO_EMISOR")
	private String nombreBancoEmisor;
	
	@JoinColumn(name = "CODIGO_BANCO_EMISOR", referencedColumnName = "CODIGO_INSTITUCION")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private DafInstituciones dafInstituciones;
	
	@JoinColumn(name = "CODIGO_BANCO_LIQUIDADOR", referencedColumnName = "CODIGO_INSTITUCION")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private DafInstituciones dafInstitucionLiquidador;

	@Size(max = 100)
	@Column(name = "MARCA_TARJETA")
	private String marcaTarjeta;
		
	@JoinColumn(name = "CODIGO_MARCA_TARJETA", referencedColumnName = "CODIGO_MARCA_TC")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private DafMarcasTarjetaCredito dafMarcasTarjetaCredito;

	@Size(max = 100)
	@Column(name = "TIPO_TARJETA")
	private String tipoTarjeta;
	
	@Size(max = 1)
	@Column(name = "ES_ACTIVO")
	private String esActivo;
	
	@NotNull
	@Size(max = 100)
	@Column(name = "USUARIO_INGRESO")
	private String usuarioIngreso;

	@NotNull
	@Column(name = "FECHA_INGRESO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaIngreso;
	
	@Size(max = 100)
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;

	@Column(name = "FECHA_MODIFICACION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaModificacion;

}
