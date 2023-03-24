package com.pinpad.ejb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * Entity implementation class for Entity: DafXParametroXEmpresa
 *
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "GRL_PARAMETROS_PINPAD")
public class GrlParametrosPinpad implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@EqualsAndHashCode.Include
	@NotNull
	@Column(name = "NEMONICO_PARAMETRO")
	private String nemonicoParametro;

	@NotNull
	@Size(max = 100)
	@Column(name = "DESCRIPCION")
	private String descripcion;

	@NotNull
	@Size(max = 100)
	@Column(name = "VALOR_NUMBER")
	private BigDecimal valorNumber;

	@NotNull
	@Size(max = 100)
	@Column(name = "VALOR_STRING")
	private String valorString;

	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "ES_ACTIVO")
	private String esActivo;

	@NotNull
	@Size(min = 1, max = 100)
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
