package com.pinpad.ejb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ID implementation class for GrlTiposTarjeta Composite Primary Key
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GrlTiposTarjetaCPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "CODIGO_TIPO_TARJETA")
	private Long codigoTipotarjeta;
	
	@NotNull
	@Column(name = "CODIGO_EMPRESA")
	private Short codigoEmpresa;

}
