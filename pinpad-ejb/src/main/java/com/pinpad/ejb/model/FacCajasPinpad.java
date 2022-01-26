/**
 * 
 */
package com.pinpad.ejb.model;

import java.io.Serializable;
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
 * @author H P
 *
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "FAC_CAJAS_PINPAD")
public class FacCajasPinpad implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@NotNull
	@Column(name = "ID_CAJAS_PINPAD")
	private Long idPinpad;
	
	@NotNull
	@Size(max = 30)
	@Column(name = "IDENTIFICADOR_CAJA")
	private String identificadorCaja;
	
	@Size(max = 30)
	@Column(name = "NOMBRE_CAJA")
	private String nombreCaja;
	
	@Size(max = 1)
	@Column(name = "ES_ACTIVO")
	private String esActivo;
	
	@NotNull
	@Size(max = 30)
	@Column(name = "IP_PINPAD")
	private String ipPinpad;
	
	@NotNull
	@Size(max = 30)
	@Column(name = "PUERTO_PINPAD")
	private String puertoPinpad;
	
	@Size(max = 30)
	@Column(name = "MAC_ADDRESS_PINPAD")
	private String macAddressPinpad;
	
	@Size(max = 100)
	@Column(name = "SERIE_PINPAD")
	private String seriePinpad;
	
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
	
	@NotNull
	@Size(max = 50)
	@Column(name = "MID")
	private String mid;
	
	@NotNull
	@Size(max = 50)
	@Column(name = "TID")
	private String tid;

}
