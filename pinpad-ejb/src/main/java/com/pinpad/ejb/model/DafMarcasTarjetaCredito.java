package com.pinpad.ejb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "DAF_MARCAS_TARJETA_CREDITO")
public class DafMarcasTarjetaCredito implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@NotNull
	@Column(name = "CODIGO_MARCA_TC")
	private Short codigoMarcaTc;
	
	@JoinColumns({ @JoinColumn(name = "CODIGO_TIPO_TARJETA", referencedColumnName = "CODIGO_TIPO_TARJETA"), 
				   @JoinColumn(name = "CODIGO_EMPRESA", referencedColumnName = "CODIGO_EMPRESA")})
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private DafTiposTarjeta dafTiposTarjeta;
	
	@Size(max = 100)
	@Column(name = "NOMBRE_MARCA_TC")
	private String nombreMarcaTc;
	
	@Column(name = "MESES_PLAZO")
	private Short mesesPlazo;
	
	@Size(max = 1)
	@Column(name = "ES_ACTIVO")
	private String esActivo;
	
	@Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
	private Date fechaIngreso;

	@Size(max = 30)
    @Column(name = "USUARIO_INGRESO")
	private String usuarioIngreso;
	
	@Column(name = "FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
	private Date fechaModificacion;
	
	@Size(max = 30)
    @Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
    
}
