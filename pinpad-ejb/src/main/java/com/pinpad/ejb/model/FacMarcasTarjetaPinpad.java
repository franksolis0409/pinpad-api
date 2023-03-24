/**
 * 
 */
package com.pinpad.ejb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "FAC_MARCAS_TARJETA_PINPAD")
public class FacMarcasTarjetaPinpad implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAC_SEQ_LIQ_PINPAD")
	@SequenceGenerator(sequenceName = "FAC_SEQ_LIQ_PINPAD", allocationSize = 1, name = "FAC_SEQ_LIQ_PINPAD")
	@NotNull
	@Column(name = "SECUENCIA")
	private Long secuencia;
	
	@NotNull
	@Size(max = 100)
	@Column(name = "GRUPO_TARJETA")
	private String grupoTarjeta;
	
	@JoinColumns({
		@JoinColumn(name = "CODIGO_TARJETA", referencedColumnName = "CODIGO_MARCA_TC"),
		@JoinColumn(name = "CODIGO_EMPRESA", referencedColumnName = "CODIGO_EMPRESA") })	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private DafMarcasTarjetaCredito dafMarcasTarjetaCredito;
	
	@Size(max = 1)
	@Column(name = "ES_ACTIVO")
	private String esActivo;
	
	@NotNull
	@Size(min = 1, max = 15)
	@Column(name = "USUARIO_INGRESO")
	private String usuarioIngreso;

	@NotNull
	@Column(name = "FECHA_INGRESO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaIngreso;
	
	@Size(max = 15)
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;

	@Column(name = "FECHA_MODIFICACION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaModificacion;

}
