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
 * ID implementation class for GrlMarcasTarjetaCredito Composite Primary Key
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GrlMarcasTarjetaCreditoCPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CODIGO_MARCA_TC")
	private Short codigoMarcaTc;
	
	@NotNull
	@Column(name = "CODIGO_EMPRESA")
	private Short codigoEmpresa;

}
