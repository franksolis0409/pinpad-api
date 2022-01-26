package com.pinpad.ejb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "DAF_TIPOS_TARJETA")
public class DafTiposTarjeta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@EqualsAndHashCode.Include
	protected DafTiposTarjetaCPK dafTiposTarjetaCPK;
	
	@NotNull
	@Size(max = 300)
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@NotNull
	@Size(max = 1)
	@Column(name = "ES_ACTIVO")
	private String esActivo;
	
	@NotNull
	@Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
	private Date fechaIngreso;

	@Size(max = 15)
    @Column(name = "USUARIO_INGRESO")
	private String usuarioIngreso;
	
	@Column(name = "FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
	private Date fechaModificacion;
	
	@Size(max = 15)
    @Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
	
	@Size(max = 8)
    @Column(name = "REPRESENTACION_SIMBOLO")
	private String representacionSimbolo;
    
}
