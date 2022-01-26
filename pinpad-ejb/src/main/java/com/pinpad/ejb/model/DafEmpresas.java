package com.pinpad.ejb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity implementation class for Entity: DafEmpresas
 *
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "DAF_EMPRESAS")
public class DafEmpresas implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @NotNull
    @Column(name = "CODIGO_EMPRESA")
    private Short codigoEmpresa;
    
    @Size(max = 100)
    @Column(name = "NOMBRE_EMPRESA")
    private String nombreEmpresa;
    
    @Size(max = 20)
    @Column(name = "NUMERO_IDENTIFICACION")
    private String numeroIdentificacion;

    @Size(max = 1)
    @Column(name = "ES_ACTIVO")
    private String esActivo;
    
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "ID_EMPRESA_HOMOLOGACION")
    private String idEmpresaHomologacion;

    @Column(name = "CODIGO_MONEDA")
    private Short codigoMoneda;
    
}
