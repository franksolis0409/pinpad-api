package com.pinpad.ejb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ID implementation class for DafXParametrosXEmpresa Composite Primary Key
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DafXParametrosXEmpresaCPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "CODIGO_EMPRESA")
	private Short codigoEmpresa;

	@NotNull
	@Size(max = 60)
	@Column(name = "NEMONICO_PARAMETRO")
	private String nemonicoParametro;

}
