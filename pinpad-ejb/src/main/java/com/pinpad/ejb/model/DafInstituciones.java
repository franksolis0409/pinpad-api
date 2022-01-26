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

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "DAF_INSTITUCIONES")
public class DafInstituciones implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@NotNull
	@Column(name = "CODIGO_INSTITUCION")
	private Integer codigoInstitucion;
	
	@Column(name = "CODIGO_TIPO_ACTIVIDAD")
	private Short codigoTipoActividad;
	
	@Column(name = "CODIGO_DETALLE_ACTIVIDAD")
	private Short codigoDetalleActividad;
	
	@Size(max = 100)
	@Column(name = "NOMBRE_INSTITUCION")
	private String nombreInstitucion;

	@Size(max = 1)
	@Column(name = "ORIGEN")
	private String origen;
	
	@Size(max = 1)
	@Column(name = "ES_ACTIVO")
	private String esActivo;
	
	@Size(max = 300)
	@Column(name = "DIRECCION_PRINCIPAL")
	private String direccionPrincipal;
	
	@Size(max = 20)
	@Column(name = "TELEFONO1")
	private String telefono1;
	
	@Size(max = 20)
	@Column(name = "TELEFONO2")
	private String telefono2;
	
	@Size(max = 20)
	@Column(name = "TELEFONO3")
	private String telefono3;
	
	@Size(max = 20)
	@Column(name = "FAX1")
	private String fax1;
	
	@Size(max = 20)
	@Column(name = "FAX2")
	private String fax2;
	
	@Size(max = 100)
	@Column(name = "PAGINA_WEB")
	private String paginaWeb;
	
	@Size(max = 100)
	@Column(name = "REPRESENTANTE_LEGAL")
	private String representanteLegal;
	
	@Size(max = 100)
	@Column(name = "PRESIDENTE")
	private String presidente;
	
	@Size(max = 100)
	@Column(name = "GERENTE")
	private String gerente;
	
	@Size(max = 25)
	@Column(name = "REGISTRO_SANITARIO")
	private String registroSanitario;
	
	@Size(max = 25)
	@Column(name = "NUMERO_IDENTIFICACION")
	private String numeroIdentificacion;
	
	@Size(max = 60)
	@Column(name = "MAIL")
	private String mail;
		
	@Size(max = 1)
	@Column(name = "TIPO_PERSONA")
	private String tipoPersona;
	
	@Column(name = "CODIGO_GRUPO_INSTITUCION")
	private Integer codigoGrupoInstitucion;

	@Column(name = "CODIGO_PARROQUIA")
	private Short codigoParroquia;
	
	@Column(name = "CODIGO_CIUDAD")
	private Short codigoCiudad;
	
	@Column(name = "CODIGO_PROVINCIA")
	private Short codigoProvincia;
	
	@Column(name = "CODIGO_PAIS")
	private Short codigoPais;
	
	@Size(max = 1)
	@Column(name = "ES_CONTRIBUYENTE_ESPECIAL")
	private String esContribuyenteEspecial;
	
	@Size(max = 1)
	@Column(name = "ES_PUBLICO")
	private String esPublico;
	
	@Size(max = 100)
	@Column(name = "OBSERVACION")
	private String observacion;
	
	@Size(max = 100)
	@Column(name = "NOMBRE_COMERCIAL")
	private String nombreComercial;
	
	@Column(name = "CODIGO_DISTRIBUCION_IVA")
	private Long codigoDistribucionIva;
	
	@Size(max = 1)
	@Column(name = "ES_CONTRIBUYENTE_RISE")
	private String esContribuyenteRise;
	
	@Size(max = 1)
	@Column(name = "ES_GUBERNAMENTAL")
	private String esGubernamental;
	
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
	
	@Size(max = 1)
	@Column(name = "ES_LIQUIDADOR")
	private String esLiquidador;
	
	@Size(max = 1)
	@Column(name = "ES_PROVEEDOR")
	private String esProveedor;
	
	@Column(name = "CODIGO_FORMA_PAGO")
	private Short codigoFormaPago;
	
	@Size(max = 2)
	@Column(name = "LLEVA_CONTABILIDAD")
	private String llevaContabilidad;
	
	@Column(name = "CODIGO_PAIS_EMBARQUE")
	private Short codigoPaisEmbarque;
	
	@Size(max = 2)
	@Column(name = "REPRESENTANTE_ECUADOR")
	private String representanteEcuador;
	
	@Size(max = 1)
	@Column(name = "ES_BROKER")
	private String esBroker;
	
	@Column(name = "CODIGO_TIPO_CUENTA")
	private Long codigoTipoCuenta;
	
	@Size(max = 50)
	@Column(name = "NUMERO_CUENTA")
	private String numeroCuenta;
	
	@Column(name = "CODIGO_TIPO_TARJETA")
	private Long codigoTipoTarjeta;
	
	@Size(max = 50)
	@Column(name = "NUMERO_TARJETA")
	private String numeroTarjeta;
	
	@Size(max = 100)
	@Column(name = "CODIGO_CUENTA")
	private String codigoCuenta;
	
	@Column(name = "CODIGO_INSTITUCION_FINANCIERA")
	private Long codigoInstitucionFinanciera;
	
	@Size(max = 1)
	@Column(name = "ES_EXTRANJERO")
	private String esExtranjero;
	
	@Size(max = 1)
	@Column(name = "ES_PROVEEDOR_RELACIONADO")
	private String esProveedorRelacionado;
	
	@Size(max = 1)
	@Column(name = "REGIMEN_FISCAL_PREF")
	private String regimenFiscalPref;
	
	@Size(max = 1)
	@Column(name = "CONVENIO_DOBLE_TRIBUTACION")
	private String convenioDobleTributacion;
	
	@Size(max = 1)
	@Column(name = "PAGO_SUJETO_RETENCION")
	private String pagoSujetoRetencion;
	
	@Column(name = "CODIGO_EMPRESA")
	private Short codigoEmpresa;	
	
	@Size(max = 1)
	@Column(name = "USA_POST")
	private String usaPost;
	
	@Size(max = 1)
	@Column(name = "ES_PRESTADOR_EXTERNO")
	private String esPrestadorExterno;
	
	@Size(max = 1)
	@Column(name = "ES_CONSIGNACION")
	private String esConsignacion;
	
	@Size(max = 1)
	@Column(name = "USA_CODIGO_PRINCIPAL")
	private String usaCodigoPrincipal;
	
	@Size(max = 1)
	@Column(name = "APLICA_REMISIONES")
	private String aplicaRemisiones;
	
	@Size(max = 1)
	@Column(name = "APLICA_CIERRE_CAJA")
	private String aplicaCierreCaja;
	
}
