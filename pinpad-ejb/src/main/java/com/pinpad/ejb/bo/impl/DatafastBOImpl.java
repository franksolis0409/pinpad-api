/**
 * 
 */
package com.pinpad.ejb.bo.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.ObjectUtils;

import com.amazonaws.util.IOUtils;
import com.jcraft.jsch.JSchException;
import com.pinpad.ejb.bo.IDatafastBO;
import com.pinpad.ejb.dao.impl.DafXParametrosXEmpresaDAOImpl;
import com.pinpad.ejb.dao.impl.FacCajasPinpadDAOImpl;
import com.pinpad.ejb.dao.impl.FacLogTramaPinpadDAOImpl;
import com.pinpad.ejb.dao.impl.FacParametrosGeneralesDAOImpl;
import com.pinpad.ejb.dao.impl.FacPinpadBinTarjDatafastDAOImpl;
import com.pinpad.ejb.dao.impl.FacPinpadBinesMedianetDAOImpl;
import com.pinpad.ejb.dto.DetalleArchivoCapturaDTO;
import com.pinpad.ejb.dto.InfoPinPadDTO;
import com.pinpad.ejb.dto.LecturaTarjetaDatafastDTO;
import com.pinpad.ejb.dto.ProcesoAnulacionPagoDatafastDTO;
import com.pinpad.ejb.dto.ProcesoConfiguracionDatafastDTO;
import com.pinpad.ejb.dto.ProcesoControlDatafastDTO;
import com.pinpad.ejb.dto.ProcesoPagoDatafastDTO;
import com.pinpad.ejb.dto.ProcesoReversoDatafastDTO;
import com.pinpad.ejb.enums.CodigoRedAdquirente;
import com.pinpad.ejb.enums.MarcasTarjeta;
import com.pinpad.ejb.enums.ParametrosXEmpresa;
import com.pinpad.ejb.enums.RedAdquirente;
import com.pinpad.ejb.enums.TipoArchivoEnum;
import com.pinpad.ejb.exceptions.BOException;
import com.pinpad.ejb.exceptions.BOExceptionUpt;
import com.pinpad.ejb.model.DafXParametrosXEmpresa;
import com.pinpad.ejb.model.DafXParametrosXEmpresaCPK;
import com.pinpad.ejb.model.FacCajasPinpad;
import com.pinpad.ejb.model.FacLogTramaPinpad;
import com.pinpad.ejb.model.FacPinpadBinTarjDatafast;
import com.pinpad.ejb.model.FacPinpadBinesMedianet;
import com.pinpad.ejb.util.DatafastUtil;
import com.pinpad.ejb.util.GenericUtil;
import com.pinpad.ejb.util.ImpresionTextoUtil;
import com.pinpad.ejb.util.SFTPUtil;

import DF.LANConfig;

/**
 * @author H P
 *
 */
@Stateless
public class DatafastBOImpl implements IDatafastBO {

	private static final Logger logger = Logger.getLogger(DatafastBOImpl.class.getName());

	@EJB
	private FacCajasPinpadDAOImpl objFacCajasPinpadDAOImpl;
	@EJB
	private FacParametrosGeneralesDAOImpl objFacParametrosGeneralesDAOImpl;
	@EJB
	private FacLogTramaPinpadDAOImpl objFacLogTramaPinpadDAOImpl;
	@EJB
	private DatafastUtil objDatafastUtil;
	@EJB
	private DafXParametrosXEmpresaDAOImpl objDafXParametrosXEmpresaDAOImpl;
	@EJB
	private FacPinpadBinesMedianetDAOImpl objFacPinpadBinesMedianetDAOImpl;
	@EJB
	private FacPinpadBinTarjDatafastDAOImpl objFacPinpadBinTarjDatafastDAOImpl;

	public InfoPinPadDTO infoPinpad(String strIdCaja) throws BOException {
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(strIdCaja, "pin.campos.idCaja");
		InfoPinPadDTO objInfoPinPadDTO = new InfoPinPadDTO();
		FacCajasPinpad objFacCajasPinpad = objFacCajasPinpadDAOImpl.findByIdentificadorCaja(strIdCaja.trim());
		if (ObjectUtils.isEmpty(objFacCajasPinpad))
			throw new BOException("pin.warn.objetoCajasPinPad", new Object[] { strIdCaja });
		objInfoPinPadDTO.setIpPinpad(objFacCajasPinpad.getIpPinpad());
		objInfoPinPadDTO.setPuertoPinpad(objFacCajasPinpad.getPuertoPinpad());
		objInfoPinPadDTO.setMacPinpad(objFacCajasPinpad.getMacAddressPinpad());
		objInfoPinPadDTO.setSeriePinpad(objFacCajasPinpad.getSeriePinpad());
		objInfoPinPadDTO.setMid(objFacCajasPinpad.getMid());
		objInfoPinPadDTO.setTid(objFacCajasPinpad.getTid());
		return objInfoPinPadDTO;
	}

	public LecturaTarjetaDatafastDTO lecturaTarjeta(String strIdCaja, String strCodigoUsuario) throws BOException {
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(strIdCaja, "pin.campos.idCaja");
		GenericUtil.validarCampoRequeridoBO(strCodigoUsuario, "pin.campos.codigoUsuario");
		LecturaTarjetaDatafastDTO objLecturaTarjetaDatafastDTO = new LecturaTarjetaDatafastDTO();
		String strIpPinpad = null;
		String strPuerto = null;
		String strMID = null;
		String strTID = null;
		Integer intTO = null;
		// Obtiene información del Pinpad basado a la caja.
		FacCajasPinpad objFacCajasPinpad = objFacCajasPinpadDAOImpl.findByIdentificadorCaja(strIdCaja.trim());
		if (ObjectUtils.isEmpty(objFacCajasPinpad))
			throw new BOException("pin.warn.objetoCajasPinPad", new Object[] { strIdCaja });
		strIpPinpad = objFacCajasPinpad.getIpPinpad();
		strPuerto = objFacCajasPinpad.getPuertoPinpad();

		if (ObjectUtils.isEmpty(objFacCajasPinpad.getMid()))
			throw new BOException("pin.warn.midPinpad");
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getTid()))
			throw new BOException("pin.warn.tidPinpad");
		Optional<DafXParametrosXEmpresa> objTimeOutOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.TIME_OUT_PINPAD.getName()));
		if (!objTimeOutOp.isPresent())
			throw new BOException("pin.warn.toPinpad");
		
		strMID = objFacCajasPinpad.getMid();
		strTID = objFacCajasPinpad.getTid();
		intTO = objTimeOutOp.get().getValorNumber().intValue();
		String strIdReplace = strIdCaja.replace("-", "");
		String strId = strIdReplace.substring(0, (strIdReplace.length() > 15) ? 15 : strIdReplace.length());
		// INICIALIZACIÓN DE ARCHIVO DE CONFIGURACIÓN CON DATOS DE PINPAD A CONECTARSE.
		logger.log(Level.INFO, "--== PROCESO LECTURA TARJETA ==--");
		logger.log(Level.INFO, "CAJAID: " + strId);
		logger.log(Level.INFO, "IP: " + strIpPinpad);
		logger.log(Level.INFO, "PUERTO: " + Integer.parseInt(strPuerto));
		logger.log(Level.INFO, "TIMEOUT: " + intTO);
		logger.log(Level.INFO, "MID: " + strMID);
		logger.log(Level.INFO, "TID: " + strTID);
		DF.LANConfig config = new LANConfig(strIpPinpad, Integer.parseInt(strPuerto), intTO, strMID, strTID, strId, 2,
				2);
		DF.LAN lan = new DF.LAN(config);
		// OBTENCIÓN DE RESPUESTA.
		DF.RespuestaLecturaTarjeta resp = null;
		try {
			resp = lan.LecturaTarjeta();
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		}
		if (ObjectUtils.isEmpty(resp))
			throw new BOException("pin.warn.errorLecturaTarjeta");
		FacLogTramaPinpad objTrama = objDatafastUtil.cargaInsertObjetoTramaPinPadLT(resp, strIdCaja, strCodigoUsuario);
		if (!ObjectUtils.isEmpty(objTrama)) {
			objFacLogTramaPinpadDAOImpl.persist(objTrama);
			if (!ObjectUtils.isEmpty(objTrama.getError()))
				throw new BOException("pin.error.mensajeError", new Object[] { objTrama.getError() });
		} else
			throw new BOException("pin.warn.errorLecturaTarjeta");
		// Se carga DTO de respuesta.
		objLecturaTarjetaDatafastDTO = DatafastUtil.cargaResponseLecturaTarjeta(objTrama);
		if (ObjectUtils.isEmpty(objLecturaTarjetaDatafastDTO))
			throw new BOException("pin.warn.errorLecturaTarjeta");
		return objLecturaTarjetaDatafastDTO;
	}

	public ProcesoPagoDatafastDTO procesoPago(String strIdCaja, String strCodigoUsuario, String strTipoTransaccion,
			Integer intCodigoRed, String strCodigoDiferido, Integer intPlazoDiferido, Integer intMesesGracia,
			Double douMontoBaseConIva, Double douMontoBaseSinIva, Double douSubTotal, Double douMontoIva,
			Double douMontoTotal) throws BOException, ParseException {
		ProcesoPagoDatafastDTO objProcesoPagoDatafastDTO = new ProcesoPagoDatafastDTO();
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(strIdCaja, "pin.campos.idCaja");
		GenericUtil.validarCampoRequeridoBO(strCodigoUsuario, "pin.campos.codigoUsuario");
		GenericUtil.validarCampoRequeridoBO(strTipoTransaccion, "pin.campos.tipoTransaccion");
		GenericUtil.validarCampoRequeridoBO(intCodigoRed, "pin.campos.codigoRed");
		GenericUtil.validarCampoRequeridoBO(strCodigoDiferido, "pin.campos.codigoDiferido");
		GenericUtil.validarCampoRequeridoBO(douMontoTotal, "pin.campos.montoTotal");
		GenericUtil.validarCampoRequeridoBO(douMontoBaseConIva, "pin.campos.montoBaseConIva");
		GenericUtil.validarCampoRequeridoBO(douMontoBaseSinIva, "pin.campos.montoBaseSinIva");
		GenericUtil.validarCampoRequeridoBO(douMontoIva, "pin.campos.montoIva");
		String strIpPinpad = null;
		String strPuerto = null;
		String strMID = null;
		String strTID = null;
		Integer intTO = null;
		Date fechaActual = new Date();
		Date fecha;
		Date hora;
		DateFormat hourFormat = new SimpleDateFormat("HHmmss");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        
        if (!intCodigoRed.equals(CodigoRedAdquirente.DATAFAST.getCodigo()))
        	throw new BOException("pin.warn.redErronea", new Object[] { RedAdquirente.DATAFAST.getName() });
        // Nuevas variables por definición de Bin tarjeta.
        BigDecimal bigBinTarjeta = null;
        BigDecimal bigCodigoTarjeta = null;
        String strNombreTarjeta = null;
        String strTipoTarjeta = null;
        Integer intCodBancoEmisor = null;
        String strNombreBancoEmisor = null;
        Integer intCodBancoLiquidador = null;
        String strNombreBancoLiquidador = null;
        
        // Si la transacción es de diferidos se realiza lectura de tarjeta antes del pago.
        LecturaTarjetaDatafastDTO objLecturaTarjetaDatafastDTO = new LecturaTarjetaDatafastDTO();
        
		objLecturaTarjetaDatafastDTO = this.lecturaTarjeta(strIdCaja, strCodigoUsuario);
		if (!ObjectUtils.isEmpty(objLecturaTarjetaDatafastDTO)) {
			logger.info("Se obtiene respuesta de lectura de tarjeta.");
			if (!ObjectUtils.isEmpty(objLecturaTarjetaDatafastDTO.getBinTarjeta())) {
				logger.info("Se obtiene Bin de tarjeta: " + objLecturaTarjetaDatafastDTO.getBinTarjeta());
				bigBinTarjeta = new BigDecimal(objLecturaTarjetaDatafastDTO.getBinTarjeta());
				Optional<FacPinpadBinesMedianet> opFacPinpadBinesMedianet = objFacPinpadBinesMedianetDAOImpl.find(bigBinTarjeta);				
				if (opFacPinpadBinesMedianet.isPresent()) {
					logger.info("Bin pertenece a " + RedAdquirente.MEDIANET.getName());
					// Toda transacción de pago con tarjeta de crédito en pago diferido con bines de Medianet (tabla facilitada por Datafast actualizada) el procesador es Medianet.
					if (strTipoTransaccion.equalsIgnoreCase("02")) {
		        		logger.info("Entra por transacción de diferidos y cambia ruta a Medianet.");
		        		// Si la transacción es diferidos cambia código de red para ruteo hacia Medianet.
		        		intCodigoRed = CodigoRedAdquirente.MEDIANET.getCodigo();
					}
					if (!ObjectUtils.isEmpty(opFacPinpadBinesMedianet.get().getDafMarcasTarjetaCredito())) {
						bigCodigoTarjeta = new BigDecimal(opFacPinpadBinesMedianet.get().getDafMarcasTarjetaCredito().getCodigoMarcaTc());
						strNombreTarjeta = opFacPinpadBinesMedianet.get().getDafMarcasTarjetaCredito().getNombreMarcaTc();
//						strTipoTarjeta = !ObjectUtils.isEmpty(
//								opFacPinpadBinesMedianet.get().getDafMarcasTarjetaCredito().getDafTiposTarjeta())
//										? opFacPinpadBinesMedianet.get().getDafMarcasTarjetaCredito()
//												.getDafTiposTarjeta().getRepresentacionSimbolo()
//										: opFacPinpadBinesMedianet.get().getTipoCuenta();
						strTipoTarjeta = !ObjectUtils.isEmpty(opFacPinpadBinesMedianet.get().getTipoCuenta())
								? opFacPinpadBinesMedianet.get().getTipoCuenta().toUpperCase()
								: null;
						logger.info("Código de tarjeta Medianet: " + bigCodigoTarjeta);
						logger.info("Nombre de tarjeta Medianet: " + strNombreTarjeta);
						logger.info("Tipo de tarjeta Medianet: " + strTipoTarjeta);
					} else {
						strTipoTarjeta = opFacPinpadBinesMedianet.get().getTipoCuenta();
						logger.info("Solo se registra tipo de tarjeta Medianet: " + strTipoTarjeta);
					}
					// Información de banco emisor homologado.
					if (!ObjectUtils.isEmpty(opFacPinpadBinesMedianet.get().getDafInstituciones())) {
						intCodBancoEmisor = opFacPinpadBinesMedianet.get().getDafInstituciones().getCodigoInstitucion();
						strNombreBancoEmisor = opFacPinpadBinesMedianet.get().getDafInstituciones()
								.getNombreInstitucion();
						logger.info("Bines Banco Emisor: " + strNombreBancoEmisor);
					}
					// Información de banco liquidador homologado.
					if (!ObjectUtils.isEmpty(opFacPinpadBinesMedianet.get().getDafInstitucionLiquidador())) {
						intCodBancoLiquidador = opFacPinpadBinesMedianet.get().getDafInstitucionLiquidador()
								.getCodigoInstitucion();
						strNombreBancoLiquidador = opFacPinpadBinesMedianet.get().getDafInstitucionLiquidador()
								.getNombreInstitucion();
						logger.info("Bines Banco Liquidador: " + strNombreBancoLiquidador);
					}
				} else {
					// Se busca bin de tarjeta en listado de bines Datafast.
					Optional<FacPinpadBinTarjDatafast> opFacPinpadBinTarjDatafast = objFacPinpadBinTarjDatafastDAOImpl.find(bigBinTarjeta);
					// Si se encuentra bin de tarjeta se setea información de la tarjeta homologada.
					if (opFacPinpadBinTarjDatafast.isPresent()) {
						logger.info("Se encuentra bin de tarjeta en datafast: " + bigBinTarjeta);
						if (!ObjectUtils.isEmpty(opFacPinpadBinTarjDatafast.get().getDafMarcasTarjetaCredito())) {
							bigCodigoTarjeta = new BigDecimal(opFacPinpadBinTarjDatafast.get().getDafMarcasTarjetaCredito().getCodigoMarcaTc());
							strNombreTarjeta = opFacPinpadBinTarjDatafast.get().getDafMarcasTarjetaCredito().getNombreMarcaTc();
//							strTipoTarjeta = !ObjectUtils.isEmpty(
//									opFacPinpadBinTarjDatafast.get().getDafMarcasTarjetaCredito().getDafTiposTarjeta())
//									? opFacPinpadBinTarjDatafast.get().getDafMarcasTarjetaCredito()
//											.getDafTiposTarjeta().getRepresentacionSimbolo()
//									: opFacPinpadBinTarjDatafast.get().getTipoTarjeta();
							strTipoTarjeta = !ObjectUtils.isEmpty(opFacPinpadBinTarjDatafast.get().getTipoTarjeta())
									? opFacPinpadBinTarjDatafast.get().getTipoTarjeta().toUpperCase()
									: null;
							logger.info("Código de tarjeta Datafast: " + bigCodigoTarjeta);
							logger.info("Nombre de tarjeta Datafast: " + strNombreTarjeta);
							logger.info("Tipo de tarjeta Datafast: " + strTipoTarjeta);
						} else {
							strTipoTarjeta = opFacPinpadBinTarjDatafast.get().getTipoTarjeta();
							logger.info("Solo se registra tipo de tarjeta Datafast: " + strTipoTarjeta);
						}
						// Información de banco emisor homologado.
						if (!ObjectUtils.isEmpty(opFacPinpadBinTarjDatafast.get().getDafInstituciones())) {
							intCodBancoEmisor = opFacPinpadBinTarjDatafast.get().getDafInstituciones()
									.getCodigoInstitucion();
							strNombreBancoEmisor = opFacPinpadBinTarjDatafast.get().getDafInstituciones()
									.getNombreInstitucion();
							logger.info("Bines Banco Emisor: " + strNombreBancoEmisor);
						}
						// Información de banco liquidador homologado.
						if (!ObjectUtils.isEmpty(opFacPinpadBinTarjDatafast.get().getDafInstitucionLiquidador())) {
							intCodBancoLiquidador = opFacPinpadBinTarjDatafast.get().getDafInstitucionLiquidador()
									.getCodigoInstitucion();
							strNombreBancoLiquidador = opFacPinpadBinTarjDatafast.get().getDafInstitucionLiquidador()
									.getNombreInstitucion();
							logger.info("Bines Banco Liquidador: " + strNombreBancoLiquidador);
						}
					}					
				}
			} else
				throw new BOException("pin.warn.errorBinTarjeta");
		} else
			throw new BOException("pin.warn.errorLecturaTarjeta");		

		// Toda transacción de pago con tarjeta de crédito en pago corriente o diferido con bines de Union Pay el procesador es Medianet
		if (!ObjectUtils.isEmpty(strNombreTarjeta)) {
			if (GenericUtil.contieneValorString(strNombreTarjeta, MarcasTarjeta.UNION_PAY.getName())) {
				logger.info("Entra por transacción corriente de tarjeta " + MarcasTarjeta.UNION_PAY.getName()
						+ " y cambia ruta a Medianet.");
				intCodigoRed = CodigoRedAdquirente.MEDIANET.getCodigo();
			}
		}

		// Obtiene información del Pinpad basado a la caja.
		FacCajasPinpad objFacCajasPinpad = objFacCajasPinpadDAOImpl.findByIdentificadorCaja(strIdCaja.trim());
		if (ObjectUtils.isEmpty(objFacCajasPinpad))
			throw new BOException("pin.warn.objetoCajasPinPad", new Object[] { strIdCaja });
		strIpPinpad = objFacCajasPinpad.getIpPinpad();
		strPuerto = objFacCajasPinpad.getPuertoPinpad();
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getMid()))
			throw new BOException("pin.warn.midPinpad");
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getTid()))
			throw new BOException("pin.warn.tidPinpad");
		Optional<DafXParametrosXEmpresa> objTimeOutOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.TIME_OUT_PINPAD.getName()));
		if (!objTimeOutOp.isPresent())
			throw new BOException("pin.warn.toPinpad");
		strMID = objFacCajasPinpad.getMid();//objMidOp.get().getValorVarchar();
		strTID = objFacCajasPinpad.getTid();//objTidOp.get().getValorVarchar();
		intTO = objTimeOutOp.get().getValorNumber().intValue();
		String strIdReplace = strIdCaja.replace("-", "");
		String strId = strIdReplace.substring(0, (strIdReplace.length() > 15) ? 15 : strIdReplace.length());
		
		hora = hourFormat.parse(new SimpleDateFormat("HHmmss").format(fechaActual));
		fecha = dateFormat.parse(new SimpleDateFormat("yyyyMMdd").format(fechaActual));
		// INICIALIZACIÓN DE ARCHIVO DE CONFIGURACIÓN CON DATOS DE PINPAD A CONECTARSE.
		logger.log(Level.INFO, "--== PROCESO PAGOS ==--");
		logger.log(Level.INFO, "CAJAID: " + strId);
		logger.log(Level.INFO, "IP: " + strIpPinpad);
		logger.log(Level.INFO, "PUERTO: " + Integer.parseInt(strPuerto));
		logger.log(Level.INFO, "TIMEOUT: " + intTO);
		logger.log(Level.INFO, "MID: " + strMID);
		logger.log(Level.INFO, "TID: " + strTID);
		DF.LANConfig config = new LANConfig(strIpPinpad, Integer.parseInt(strPuerto), intTO, strMID, strTID, strId, 2,
				2);
		DF.LAN lan = new DF.LAN(config);
		// OBTENCIÓN DE RESPUESTA.
		DF.RespuestaProcesoPago resp = null;
		try {
			DF.EnvioProcesoPago pago = new DF.EnvioProcesoPago();
			pago.TipoTransaccion = Integer.valueOf(strTipoTransaccion);
			pago.RedAdquirente = intCodigoRed;
			pago.CodigoDiferido = strCodigoDiferido;
			if (!ObjectUtils.isEmpty(strCodigoDiferido) && !strTipoTransaccion.equalsIgnoreCase("01")) {
				if (!strCodigoDiferido.equalsIgnoreCase("00") && strTipoTransaccion.equalsIgnoreCase("02")) {
					if (!ObjectUtils.isEmpty(intPlazoDiferido)) {
						pago.PlazoDiferido = intPlazoDiferido < 10 ? "0" + intPlazoDiferido.toString()
								: intPlazoDiferido.toString();
					} else
						throw new BOException("pin.warn.plazoDiferidoObligatorio");
					if (!ObjectUtils.isEmpty(intMesesGracia))
						pago.MesesGracia = intMesesGracia < 10 ? "0" + intMesesGracia.toString()
								: intMesesGracia.toString();
				} else
					throw new BOException("pin.warn.codigoDiferidoIncorrecto");
			}
			pago.Base0 = GenericUtil.convertDoubleToStringFormatPP(douMontoBaseSinIva, 12);
			pago.IVA = GenericUtil.convertDoubleToStringFormatPP(douMontoIva, 12);
			pago.MontoTotal = GenericUtil.convertDoubleToStringFormatPP(douMontoTotal, 12);
			pago.Hora = hora;//hourFormat.parse(new SimpleDateFormat("HH:mm:ss").format(fechaActual));
			pago.Fecha = fecha;//dateFormat.parse(new SimpleDateFormat("yyyy/MM/dd").format(fechaActual));
			pago.TID = strTID;
			pago.MID = strMID;
			pago.CID = strId;
			resp = lan.ProcesoPago(pago);
		} catch (Exception e) {
			// TODO: handle exception
			// En caso de error al enviar petición de Pagos, se ejecuta reverso automático.			
			this.procesoReversoAutomatico(strIdCaja, strCodigoUsuario, strTipoTransaccion, intCodigoRed,
					strCodigoDiferido, intPlazoDiferido, intMesesGracia, douMontoBaseConIva, douMontoBaseSinIva,
					douSubTotal, douMontoIva, douMontoTotal, fecha, hora, null);
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException("pin.warn.errorProcesoPagos", e.getCause());
		}
		if (ObjectUtils.isEmpty(resp)) {
			// En caso de error al enviar petición de Pagos, se ejecuta reverso automático.
			this.procesoReversoAutomatico(strIdCaja, strCodigoUsuario, strTipoTransaccion, intCodigoRed,
					strCodigoDiferido, intPlazoDiferido, intMesesGracia, douMontoBaseConIva, douMontoBaseSinIva,
					douSubTotal, douMontoIva, douMontoTotal, fecha, hora, null);
			throw new BOException("pin.warn.errorProcesoPagos");
		}
		FacLogTramaPinpad objTrama = objDatafastUtil.cargaInsertObjetoTramaPinPadPP(resp, strIdCaja, strCodigoUsuario);
		if (!ObjectUtils.isEmpty(objTrama)) {
			objTrama.setCodigoDiferido(!ObjectUtils.isEmpty(strCodigoDiferido) ? strCodigoDiferido : "00");
			objTrama.setTipoTransaccion(strTipoTransaccion);
			objTrama.setPlazoDiferido(intPlazoDiferido);
			objTrama.setMid(strMID);
			objTrama.setTid(strTID);
			objTrama.setBinTarjeta(!ObjectUtils.isEmpty(bigBinTarjeta) ? bigBinTarjeta.toString() : null);
			// CALCULO DE VALORES.
			objTrama.setMontoBaseConIva(douMontoBaseConIva);
			objTrama.setMontoBaseSinIva(douMontoBaseSinIva);
			objTrama.setSubtotal(douSubTotal);
			objTrama.setValorIva(douMontoIva);
			objTrama.setSubtotalBaseImponible(douMontoTotal);
			objTrama.setValorInteres(!ObjectUtils.isEmpty(objTrama.getValorInteres()) ? objTrama.getValorInteres() : 0.00);
			objTrama.setValorTotal(objTrama.getValorInteres() + douMontoTotal);
			// Se setea nuevo valor de banco emisor
			objTrama.setCodBancoEmisor(!ObjectUtils.isEmpty(intCodBancoEmisor) ? intCodBancoEmisor : objTrama.getCodBancoEmisor());
			objTrama.setNombreBancoEmisor(!ObjectUtils.isEmpty(strNombreBancoEmisor) ? strNombreBancoEmisor : objTrama.getNombreBancoEmisor());
			// Se setea nuevo valor de banco liquidador
			objTrama.setCodBancoLiquidador(!ObjectUtils.isEmpty(intCodBancoLiquidador) ? intCodBancoLiquidador : objTrama.getCodBancoLiquidador());
			objTrama.setNombreBancoLiquidador(!ObjectUtils.isEmpty(strNombreBancoLiquidador) ? strNombreBancoLiquidador : objTrama.getNombreBancoLiquidador());
			// Se setean nuevas variables de tarjetas.
			objTrama.setCodTarjeta(!ObjectUtils.isEmpty(bigCodigoTarjeta) ? bigCodigoTarjeta : objTrama.getCodTarjeta());
			objTrama.setNombreTarjeta(!ObjectUtils.isEmpty(strNombreTarjeta) ? strNombreTarjeta :objTrama.getNombreTarjeta());
			objTrama.setTipoTarjeta(!ObjectUtils.isEmpty(strTipoTarjeta) ? strTipoTarjeta : objTrama.getTipoTarjeta());
			//
			objFacLogTramaPinpadDAOImpl.persist(objTrama);
			if (!ObjectUtils.isEmpty(objTrama.getError())) {
				// En caso de error al enviar petición de Pagos, se ejecuta reverso automático.
				this.procesoReversoAutomatico(strIdCaja, strCodigoUsuario, strTipoTransaccion, intCodigoRed,
						strCodigoDiferido, intPlazoDiferido, intMesesGracia, douMontoBaseConIva, douMontoBaseSinIva,
						douSubTotal, douMontoIva, douMontoTotal, fecha, hora, objTrama);
				throw new BOException("pin.error.mensajeError", new Object[] { objTrama.getError() });
			}
			if (ObjectUtils.isEmpty(objTrama.getNumeroAutorizacion())) {
				// En caso de no llegar autorización, se ejecuta reverso automático.
				this.procesoReversoAutomatico(strIdCaja, strCodigoUsuario, strTipoTransaccion, intCodigoRed,
						strCodigoDiferido, intPlazoDiferido, intMesesGracia, douMontoBaseConIva, douMontoBaseSinIva,
						douSubTotal, douMontoIva, douMontoTotal, fecha, hora, objTrama);
				throw new BOException("pin.warn.numeroAutorizacionEmpty");
			}
		} else
			throw new BOException("pin.warn.errorProcesoPagos");
		// Se carga DTO de respuesta.
		objProcesoPagoDatafastDTO = DatafastUtil.cargaResponseProcesoPagos(objTrama);
		if (ObjectUtils.isEmpty(objProcesoPagoDatafastDTO))
			throw new BOException("pin.warn.errorProcesoPagos");
		return objProcesoPagoDatafastDTO;
	}

	public ProcesoAnulacionPagoDatafastDTO anulacionPago(String strIdCaja, String strTipoTransaccion,
			Integer intCodigoRed, BigDecimal bigSecuencia, String strCodigoUsuario) throws BOException {
		ProcesoAnulacionPagoDatafastDTO objProcesoAnulacionPagoDatafastDTO = new ProcesoAnulacionPagoDatafastDTO();
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(strIdCaja, "pin.campos.idCaja");
		GenericUtil.validarCampoRequeridoBO(strCodigoUsuario, "pin.campos.codigoUsuario");
		GenericUtil.validarCampoRequeridoBO(strTipoTransaccion, "pin.campos.tipoTransaccion");
		GenericUtil.validarCampoRequeridoBO(intCodigoRed, "pin.campos.codigoRed");
		GenericUtil.validarCampoRequeridoBO(bigSecuencia, "pin.campos.secuenciaLog");
		String strIpPinpad = null;
		String strPuerto = null;
		String strMID = null;
		String strTID = null;
		Integer intTO = null;
		
		if (!intCodigoRed.equals(CodigoRedAdquirente.DATAFAST.getCodigo()))
        	throw new BOException("pin.warn.redErronea", new Object[] { RedAdquirente.DATAFAST.getName() });
		
		// Obtiene información del Pinpad basado a la caja.
		FacCajasPinpad objFacCajasPinpad = objFacCajasPinpadDAOImpl.findByIdentificadorCaja(strIdCaja.trim());
		if (ObjectUtils.isEmpty(objFacCajasPinpad))
			throw new BOException("pin.warn.objetoCajasPinPad", new Object[] { strIdCaja });
		strIpPinpad = objFacCajasPinpad.getIpPinpad();
		strPuerto = objFacCajasPinpad.getPuertoPinpad();
		Optional<FacLogTramaPinpad> objFacLogTramaPinpadOp = objFacLogTramaPinpadDAOImpl.find(bigSecuencia.longValue());
		if (!objFacLogTramaPinpadOp.isPresent())
			throw new BOException("pin.warn.secuenciaLogTrama");
		String strReferencia = objFacLogTramaPinpadOp.get().getSecuenciaTransaccion();
		String strNumAutorizacion = objFacLogTramaPinpadOp.get().getNumeroAutorizacion();
		
//		if (!ObjectUtils.isEmpty(objFacLogTramaPinpadOp.get().getTipoTransaccion())) {
//			if (objFacLogTramaPinpadOp.get().getTipoTransaccion().equalsIgnoreCase("02")) {
//				logger.info("Entra por transacción de diferidos y cambia ruta a Medianet.");
//				// Si la transacción es diferidos cambia código de red para ruteo hacia
//				// Medianet.
//				intCodigoRed = CodigoRedAdquirente.MEDIANET.getCodigo();
//			}
//		}

		if (ObjectUtils.isEmpty(objFacCajasPinpad.getMid()))
			throw new BOException("pin.warn.midPinpad");
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getTid()))
			throw new BOException("pin.warn.tidPinpad");
		Optional<DafXParametrosXEmpresa> objTimeOutOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.TIME_OUT_PINPAD.getName()));
		if (!objTimeOutOp.isPresent())
			throw new BOException("pin.warn.toPinpad");
		strMID = objFacCajasPinpad.getMid();//objMidOp.get().getValorVarchar();
		strTID = objFacCajasPinpad.getTid();//objTidOp.get().getValorVarchar();
		intTO = objTimeOutOp.get().getValorNumber().intValue();
		String strIdReplace = strIdCaja.replace("-", "");
		String strId = strIdReplace.substring(0, (strIdReplace.length() > 15) ? 15 : strIdReplace.length());
		// INICIALIZACIÓN DE ARCHIVO DE CONFIGURACIÓN CON DATOS DE PINPAD A CONECTARSE.
		logger.log(Level.INFO, "--== PROCESO ANULACIÓN ==--");
		logger.log(Level.INFO, "CAJAID: " + strId);
		logger.log(Level.INFO, "IP: " + strIpPinpad);
		logger.log(Level.INFO, "PUERTO: " + Integer.parseInt(strPuerto));
		logger.log(Level.INFO, "TIMEOUT: " + intTO);
		logger.log(Level.INFO, "MID: " + strMID);
		logger.log(Level.INFO, "TID: " + strTID);
		DF.LANConfig config = new LANConfig(strIpPinpad, Integer.parseInt(strPuerto), intTO, strMID, strTID, strId, 2,
				2);
		DF.LAN lan = new DF.LAN(config);
		DF.RespuestaProcesoPago resp = null;
		try {
			// OBTENCIÓN DE RESPUESTA.
			DF.EnvioProcesoPago anulacion = new DF.EnvioProcesoPago();
			anulacion.TipoTransaccion = Integer.valueOf(strTipoTransaccion);
			anulacion.RedAdquirente = !ObjectUtils.isEmpty(objFacLogTramaPinpadOp.get().getCodigoRed())
					? Integer.valueOf(objFacLogTramaPinpadOp.get().getCodigoRed())
					: intCodigoRed;
			anulacion.Referencia = strReferencia;
			anulacion.Autorizacion = strNumAutorizacion;
//			logger.log(Level.INFO, "Objeto request: " + anulacion.getClass().toString());
			resp = lan.ProcesoPago(anulacion);
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		}
		if (ObjectUtils.isEmpty(resp))
			throw new BOException("pin.warn.errorAnulacionPagos");
		FacLogTramaPinpad objTrama = objDatafastUtil.cargaInsertObjetoTramaPinPadPP(resp, strIdCaja, strCodigoUsuario);
		if (!ObjectUtils.isEmpty(objTrama)) {
			objTrama.setTipoTransaccion(strTipoTransaccion);
			objTrama.setCodTransaccionOriginal(strReferencia);
			// Nuevo cambio, se agrega secuencia de log original para presición en búsqueda de anulaciones.
			//objTrama.setFacLogTramaPinpadOriginal(objFacLogTramaPinpadOp.get());
			//
			objTrama.setFechaVctoTarjeta(objFacLogTramaPinpadOp.get().getFechaVctoTarjeta());
			objTrama.setMid(strMID);
			objTrama.setTid(strTID);
			objTrama.setNumeroAutorizacion(objFacLogTramaPinpadOp.get().getNumeroAutorizacion());
			// CALCULO DE VALORES.
			objTrama.setMontoBaseConIva(objFacLogTramaPinpadOp.get().getMontoBaseConIva());
			objTrama.setMontoBaseSinIva(objFacLogTramaPinpadOp.get().getMontoBaseSinIva());
			objTrama.setSubtotal(objFacLogTramaPinpadOp.get().getSubtotal());
			objTrama.setValorIva(objFacLogTramaPinpadOp.get().getValorIva());
			objTrama.setSubtotalBaseImponible(objFacLogTramaPinpadOp.get().getSubtotalBaseImponible());
			objTrama.setValorInteres(objFacLogTramaPinpadOp.get().getValorInteres());
			objTrama.setValorTotal(objFacLogTramaPinpadOp.get().getValorTotal());
			objFacLogTramaPinpadDAOImpl.persist(objTrama);
			if (!ObjectUtils.isEmpty(objTrama.getError()))
				throw new BOException("pin.error.mensajeError", new Object[] { objTrama.getError() });
		} else
			throw new BOException("pin.warn.errorAnulacionPagos");
		// Se carga DTO de respuesta.
		objProcesoAnulacionPagoDatafastDTO = DatafastUtil.cargaResponseAnulacionPago(objTrama);
		if (ObjectUtils.isEmpty(objProcesoAnulacionPagoDatafastDTO))
			throw new BOException("pin.warn.errorAnulacionPagos");
		return objProcesoAnulacionPagoDatafastDTO;
	}

	public ProcesoControlDatafastDTO procesoControl(String strIdCaja, Integer intCodigoRed, String strCodigoUsuario)
			throws BOException {
		ProcesoControlDatafastDTO objProcesoControlDatafastDTO = new ProcesoControlDatafastDTO();
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(strIdCaja, "pin.campos.idCaja");
		GenericUtil.validarCampoRequeridoBO(strCodigoUsuario, "pin.campos.codigoUsuario");
		GenericUtil.validarCampoRequeridoBO(intCodigoRed, "pin.campos.codigoRed");
		String strIpPinpad = null;
		String strPuerto = null;
		String strMID = null;
		String strTID = null;
		Integer intTO = null;
		// Obtiene información del Pinpad basado a la caja.
		FacCajasPinpad objFacCajasPinpad = objFacCajasPinpadDAOImpl.findByIdentificadorCaja(strIdCaja.trim());
		if (ObjectUtils.isEmpty(objFacCajasPinpad))
			throw new BOException("pin.warn.objetoCajasPinPad", new Object[] { strIdCaja });
		strIpPinpad = objFacCajasPinpad.getIpPinpad();
		strPuerto = objFacCajasPinpad.getPuertoPinpad();
//		String strLote = GenericUtil
//				.convierteNumeroLoteReferencia(Integer.parseInt(objFacLogTramaPinpadDAOImpl.obtenerMaxLote()) + 1);
		String strLote = GenericUtil
				.convierteNumeroLoteReferencia(objFacLogTramaPinpadDAOImpl.obtenerSecuenciaLote().intValue());
		logger.info("Se genera proceso de control en caja " + objFacCajasPinpad.getTid() + " por parte de usuario: " + strCodigoUsuario);
		logger.info("Se genera Lote N° " + strLote + " - Usuario: " + strCodigoUsuario);
		String strReferencia = GenericUtil.convierteNumeroLoteReferencia(1);

		if (ObjectUtils.isEmpty(objFacCajasPinpad.getMid()))
			throw new BOException("pin.warn.midPinpad");
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getTid()))
			throw new BOException("pin.warn.tidPinpad");
		Optional<DafXParametrosXEmpresa> objTimeOutOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.TIME_OUT_PINPAD.getName()));
		if (!objTimeOutOp.isPresent())
			throw new BOException("pin.warn.toPinpad");
		strMID = objFacCajasPinpad.getMid();//objMidOp.get().getValorVarchar();
		strTID = objFacCajasPinpad.getTid();//objTidOp.get().getValorVarchar();
		intTO = objTimeOutOp.get().getValorNumber().intValue();
		String strIdReplace = strIdCaja.replace("-", "");
		String strId = strIdReplace.substring(0, (strIdReplace.length() > 15) ? 15 : strIdReplace.length());
		// INICIALIZACIÓN DE ARCHIVO DE CONFIGURACIÓN CON DATOS DE PINPAD A CONECTARSE.
		DF.LANConfig config = new LANConfig(strIpPinpad, Integer.parseInt(strPuerto), intTO, strMID, strTID, strId, 2,
				2);
		DF.LAN lan = new DF.LAN(config);
		// OBTENCIÓN DE RESPUESTA.
		DF.RespuestaProcesoControl resp = null;
		try {
			DF.EnvioProcesoControl control = new DF.EnvioProcesoControl();
			if (!ObjectUtils.isEmpty(strLote))
				control.Lote = strLote;
			if (!ObjectUtils.isEmpty(strReferencia))
				control.Referencia = strReferencia;
			control.CajaID = strId;
			control.MID = strMID;
			control.TID = strTID;
			resp = lan.ProcesoControl(control);
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		}
		if (ObjectUtils.isEmpty(resp))
			throw new BOException("pin.warn.errorProcesoControl");
		FacLogTramaPinpad objTrama = objDatafastUtil.cargaInsertObjetoTramaPinPadPC(resp, strIdCaja, strCodigoUsuario);
		if (!ObjectUtils.isEmpty(objTrama)) {
			objTrama.setMid(strMID);
			objTrama.setTid(strTID);
			objFacLogTramaPinpadDAOImpl.persist(objTrama);
			if (!ObjectUtils.isEmpty(objTrama.getError()))
				throw new BOException("pin.error.mensajeError", new Object[] { objTrama.getError() });
		} else
			throw new BOException("pin.warn.errorProcesoControl");
		// Se carga DTO de respuesta.
		objProcesoControlDatafastDTO = DatafastUtil.cargaResponseProcesoControl(objTrama);
		if (ObjectUtils.isEmpty(objProcesoControlDatafastDTO))
			throw new BOException("pin.warn.errorProcesoControl");
		return objProcesoControlDatafastDTO;
	}

	public FacLogTramaPinpad obtenerLogDatafast(BigDecimal bigSecuencia) throws BOException {
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(bigSecuencia, "pin.campos.secuenciaLog");
		Optional<FacLogTramaPinpad> objFacLogTramaPinpadOp = objFacLogTramaPinpadDAOImpl.find(bigSecuencia.longValue());
		if (!objFacLogTramaPinpadOp.isPresent())
			throw new BOException("pin.warn.secuenciaLogTrama");
		return objFacLogTramaPinpadOp.get();
	}

	public ProcesoConfiguracionDatafastDTO procesoConfiguracion(String strIdCaja, String strNuevaIp, String strMascara, String strGateway,
			String strPrincipalHost, String strPrincipalPuerto, String strAlternoHost, String strAlternoPuerto,
			String strPuertoEscucha, String strCodigoUsuario) throws BOException {
		ProcesoConfiguracionDatafastDTO objProcesoConfiguracionDatafastDTO = new ProcesoConfiguracionDatafastDTO();
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(strIdCaja, "pin.campos.idCaja");
		GenericUtil.validarCampoRequeridoBO(strNuevaIp, "pin.campos.nuevaIp");
		GenericUtil.validarCampoRequeridoBO(strMascara, "pin.campos.mascara");
		GenericUtil.validarCampoRequeridoBO(strGateway, "pin.campos.gateway");
		GenericUtil.validarCampoRequeridoBO(strPrincipalHost, "pin.campos.principalHost");
		GenericUtil.validarCampoRequeridoBO(strPrincipalPuerto, "pin.campos.principalPuerto");
		//GenericUtil.validarCampoRequeridoBO(strAlternoHost, "pin.campos.alternoHost");
		//GenericUtil.validarCampoRequeridoBO(strAlternoPuerto, "pin.campos.alternoPuerto");
		GenericUtil.validarCampoRequeridoBO(strPuertoEscucha, "pin.campos.puertoEscucha");
		GenericUtil.validarCampoRequeridoBO(strCodigoUsuario, "pin.campos.codigoUsuario");
		String strIpPinpad = null;
		String strPuerto = null;
		String strMID = null;
		String strTID = null;
		Integer intTO = null;
		// Obtiene información del Pinpad basado a la caja.
		FacCajasPinpad objFacCajasPinpad = objFacCajasPinpadDAOImpl.findByIdentificadorCaja(strIdCaja.trim());
		if (ObjectUtils.isEmpty(objFacCajasPinpad))
			throw new BOException("pin.warn.objetoCajasPinPad", new Object[] { strIdCaja });
		strIpPinpad = objFacCajasPinpad.getIpPinpad();
		strPuerto = objFacCajasPinpad.getPuertoPinpad();
		
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getMid()))
			throw new BOException("pin.warn.midPinpad");
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getTid()))
			throw new BOException("pin.warn.tidPinpad");
		Optional<DafXParametrosXEmpresa> objTimeOutOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.TIME_OUT_PINPAD.getName()));
		if (!objTimeOutOp.isPresent())
			throw new BOException("pin.warn.toPinpad");
		
		strMID = objFacCajasPinpad.getMid();//objMidOp.get().getValorVarchar();
		strTID = objFacCajasPinpad.getTid();//objTidOp.get().getValorVarchar();
		intTO = objTimeOutOp.get().getValorNumber().intValue();
		String strIdReplace = strIdCaja.replace("-", "");
		String strId = strIdReplace.substring(0, (strIdReplace.length() > 15) ? 15 : strIdReplace.length());
		// INICIALIZACIÓN DE ARCHIVO DE CONFIGURACIÓN CON DATOS DE PINPAD A CONECTARSE.
		DF.LANConfig config = new LANConfig(strIpPinpad, Integer.parseInt(strPuerto), intTO, strMID, strTID, strId, 2,
				2);
		DF.LAN lan = new DF.LAN(config);
		// OBTENCIÓN DE RESPUESTA.
		DF.RespuestaProcesoConfigPinPad resp = null;
		try {
			DF.EnvioProcesoConfigPinPad configp = new DF.EnvioProcesoConfigPinPad();
			configp.DireccionIP = strNuevaIp;
			configp.Mascara = strMascara;
			configp.Gateway = strGateway;
			configp.PrincipalHost = strPrincipalHost;
			configp.PrincipalPuerto = strPrincipalPuerto;
			configp.AlternoHost = strAlternoHost;
			configp.AlternoPuerto = strAlternoPuerto;
			configp.PuertoEscucha = strPuertoEscucha;
			resp = lan.ProcesoConfigPinPad(configp);
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		}
		if (ObjectUtils.isEmpty(resp))
			throw new BOException("pin.warn.errorProcesoConfiguracion");
		FacLogTramaPinpad objTrama = objDatafastUtil.cargaInsertObjetoTramaPinPadPConfig(resp, strIdCaja,
				strCodigoUsuario);
		if (!ObjectUtils.isEmpty(objTrama)) {
			objTrama.setMid(strMID);
			objTrama.setTid(strTID);
			objFacLogTramaPinpadDAOImpl.persist(objTrama);
			if (!ObjectUtils.isEmpty(objTrama.getError()))
				throw new BOException("pin.error.mensajeError", new Object[] { objTrama.getError() });
		} else
			throw new BOException("pin.warn.errorProcesoConfiguracion");
		// Se carga DTO de respuesta.
		objProcesoConfiguracionDatafastDTO = DatafastUtil.cargaResponseProcesoConfiguracion(objTrama);
		if (ObjectUtils.isEmpty(objProcesoConfiguracionDatafastDTO))
			throw new BOException("pin.warn.errorProcesoConfiguracion");
		// Se actualiza tabla de configuración de Pinpad.
		if(!ObjectUtils.isEmpty(strNuevaIp))
			objFacCajasPinpad.setIpPinpad(strNuevaIp);
		if(!ObjectUtils.isEmpty(strPuertoEscucha))
			objFacCajasPinpad.setPuertoPinpad(strPuertoEscucha);
		objFacCajasPinpad.setUsuarioModificacion(strCodigoUsuario);
		objFacCajasPinpad.setFechaModificacion(new Date());
		objFacCajasPinpadDAOImpl.update(objFacCajasPinpad);
		return objProcesoConfiguracionDatafastDTO;
	}
	
	public ProcesoReversoDatafastDTO procesoReversoManual(BigDecimal bigSecuencia) throws BOException {
		ProcesoReversoDatafastDTO objProcesoReversoDatafastDTO = new ProcesoReversoDatafastDTO();
		// Valida campos obligatorios
		GenericUtil.validarCampoRequeridoBO(bigSecuencia, "pin.campos.secuenciaLog");
		
		String strIpPinpad = null;
		String strPuerto = null;
		String strMID = null;
		String strTID = null;
		Integer intTO = null;
		Optional<FacLogTramaPinpad> objFacLogTramaPinpadOp = objFacLogTramaPinpadDAOImpl.find(bigSecuencia.longValue());
		if (!objFacLogTramaPinpadOp.isPresent())
			throw new BOException("pin.warn.secuenciaLogTrama");
		// Solo si la transacción es Aprobada se procede a reversar.
		if (!objFacLogTramaPinpadOp.get().getCodigoRespuesta().equalsIgnoreCase("00"))
			throw new BOException("pin.warn.secuenciaLogTrama");
		
		logger.info("Código de red recibido en reverso: " + objFacLogTramaPinpadOp.get().getCodigoRed());
		// Obtiene información del Pinpad basado a la caja.
		FacCajasPinpad objFacCajasPinpad = objFacCajasPinpadDAOImpl.findByIdentificadorCaja(objFacLogTramaPinpadOp.get().getCid().trim());
		if (ObjectUtils.isEmpty(objFacCajasPinpad))
			throw new BOException("pin.warn.objetoCajasPinPad", new Object[] { objFacLogTramaPinpadOp.get().getCid() });
		strIpPinpad = objFacCajasPinpad.getIpPinpad();
		strPuerto = objFacCajasPinpad.getPuertoPinpad();

		if (ObjectUtils.isEmpty(objFacCajasPinpad.getMid()))
			throw new BOException("pin.warn.midPinpad");
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getTid()))
			throw new BOException("pin.warn.tidPinpad");
		Optional<DafXParametrosXEmpresa> objTimeOutOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.TIME_OUT_PINPAD.getName()));
		if (!objTimeOutOp.isPresent())
			throw new BOException("pin.warn.toPinpad");
		
		strMID = objFacCajasPinpad.getMid();
		strTID = objFacCajasPinpad.getTid();
		intTO = objTimeOutOp.get().getValorNumber().intValue();
		
		String strIdReplace = objFacLogTramaPinpadOp.get().getCid().replace("-", "");
		String strId = strIdReplace.substring(0, (strIdReplace.length() > 15) ? 15 : strIdReplace.length());
		// INICIALIZACIÓN DE ARCHIVO DE CONFIGURACIÓN CON DATOS DE PINPAD A CONECTARSE.
		DF.LANConfig config = new LANConfig(strIpPinpad, Integer.parseInt(strPuerto), intTO, strMID, strTID, strId, 2,
				2);
		DF.LAN lan = new DF.LAN(config);
		// OBTENCIÓN DE RESPUESTA.
		DF.RespuestaProcesoPago resp = null;
		try {
			DF.EnvioProcesoPago pago = new DF.EnvioProcesoPago();
			pago.TipoTransaccion = Integer.valueOf("04");
			pago.RedAdquirente = Integer.valueOf(objFacLogTramaPinpadOp.get().getCodigoRed());
			pago.CodigoDiferido = objFacLogTramaPinpadOp.get().getCodigoDiferido();
			if (!ObjectUtils.isEmpty(objFacLogTramaPinpadOp.get().getPlazoDiferido()))
				pago.PlazoDiferido = objFacLogTramaPinpadOp.get().getPlazoDiferido() < 10 ? "0" + objFacLogTramaPinpadOp.get().getPlazoDiferido().toString()
						: objFacLogTramaPinpadOp.get().getPlazoDiferido().toString();
//			if (!ObjectUtils.isEmpty(intMesesGracia))
//				pago.MesesGracia = intMesesGracia < 10 ? "0" + intMesesGracia.toString() : intMesesGracia.toString();
			pago.Base0 = GenericUtil.convertDoubleToStringFormatPP(objFacLogTramaPinpadOp.get().getMontoBaseSinIva(), 12);
			pago.IVA = GenericUtil.convertDoubleToStringFormatPP(objFacLogTramaPinpadOp.get().getValorIva(), 12);
			pago.MontoTotal = GenericUtil.convertDoubleToStringFormatPP(objFacLogTramaPinpadOp.get().getSubtotalBaseImponible(), 12);
			pago.Hora = new SimpleDateFormat("HHmmss").parse(objFacLogTramaPinpadOp.get().getHora());
			pago.Fecha = new SimpleDateFormat("yyyyMMdd").parse(objFacLogTramaPinpadOp.get().getFecha());
			pago.TID = strTID;
			pago.MID = strMID;
			pago.CID = strId;
			resp = lan.ProcesoPago(pago);
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		}
		if (ObjectUtils.isEmpty(resp))
			throw new BOException("pin.warn.errorProcesoPagos");
		FacLogTramaPinpad objTrama = objDatafastUtil.cargaInsertObjetoTramaPinPadPP(resp, objFacLogTramaPinpadOp.get().getCid(), objFacLogTramaPinpadOp.get().getUsuarioIngreso());
		if (!ObjectUtils.isEmpty(objTrama)) {
			objTrama.setCodigoDiferido(!ObjectUtils.isEmpty(objFacLogTramaPinpadOp.get().getCodigoDiferido()) ? objFacLogTramaPinpadOp.get().getCodigoDiferido() : "00");
			objTrama.setTipoTransaccion("04");
			objTrama.setCodTransaccionOriginal(objFacLogTramaPinpadOp.get().getSecuenciaTransaccion());
			// Nuevo cambio, se agrega secuencia de log original para presición en búsqueda de anulaciones.
			//objTrama.setFacLogTramaPinpadOriginal(objFacLogTramaPinpadOp.get());
			//
			objTrama.setPlazoDiferido(objFacLogTramaPinpadOp.get().getPlazoDiferido());
			objTrama.setMid(strMID);
			objTrama.setTid(strTID);
			// CALCULO DE VALORES.
			objTrama.setMontoBaseConIva(objFacLogTramaPinpadOp.get().getMontoBaseConIva());
			objTrama.setMontoBaseSinIva(objFacLogTramaPinpadOp.get().getMontoBaseSinIva());
			objTrama.setSubtotal(objFacLogTramaPinpadOp.get().getSubtotal());
			objTrama.setValorIva(objFacLogTramaPinpadOp.get().getValorIva());
			objTrama.setSubtotalBaseImponible(objFacLogTramaPinpadOp.get().getSubtotalBaseImponible());
			objTrama.setValorInteres(!ObjectUtils.isEmpty(objFacLogTramaPinpadOp.get().getValorInteres()) ? objFacLogTramaPinpadOp.get().getValorInteres() : 0.00);
			objTrama.setValorTotal(objTrama.getValorInteres() + objFacLogTramaPinpadOp.get().getSubtotalBaseImponible());
			objFacLogTramaPinpadDAOImpl.persist(objTrama);
			if (!ObjectUtils.isEmpty(objTrama.getError()))
				throw new BOException("pin.error.mensajeError", new Object[] { objTrama.getError() });
		} else
			throw new BOException("pin.warn.errorProcesoPagos");
		// Se carga DTO de respuesta.
		objProcesoReversoDatafastDTO = DatafastUtil.cargaResponseProcesoReverso(objTrama);
		if (ObjectUtils.isEmpty(objProcesoReversoDatafastDTO))
			throw new BOException("pin.warn.errorProcesoPagos");
		return objProcesoReversoDatafastDTO;
	}
	
	public Path obtenerArchivoCaptura(String strFechaInicio, String strFechaFin) throws BOException, ParseException {
		List<String> lsLineasArchivo;
		StringBuilder sb = new StringBuilder();
		ByteArrayInputStream is = null;
		Path file = null;
		Date fechaActual = new Date();
		// Obtiene MID
//		Optional<FacParametrosGenerales> objMidOp = objFacParametrosGeneralesDAOImpl
//				.find(new FacParametrosGeneralesCPK(ParametrosGenerales.MID_PINPAD.getName(), (short) 1));
//		if (!objMidOp.isPresent())
//			throw new BOException("pin.warn.midPinpad");
//		String strMID = objMidOp.get().getValorVarchar();
		if (!ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)) {
			if (!GenericUtil.validarFormatoFecha(strFechaInicio))
				throw new BOException("pin.warn.formatoFecha");
			if (!GenericUtil.validarFormatoFecha(strFechaFin))
				throw new BOException("pin.warn.formatoFecha");
			if (GenericUtil.esRangoFechaFutura(new SimpleDateFormat("dd/MM/yyyy").parse(strFechaFin),
					new SimpleDateFormat("dd/MM/yyyy").parse(strFechaInicio)))
				throw new BOException("pin.warn.fechaInicioFutura");
		} else if (!ObjectUtils.isEmpty(strFechaInicio) && ObjectUtils.isEmpty(strFechaFin)) {
			if (!GenericUtil.validarFormatoFecha(strFechaInicio))
				throw new BOException("pin.warn.formatoFecha");
		} else if (ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)) {
			if (!GenericUtil.validarFormatoFecha(strFechaFin))
				throw new BOException("pin.warn.formatoFecha");
		}
		Integer intCountDetalle = 0;
		List<String> lsMidAprobados = objFacLogTramaPinpadDAOImpl.obtenerMidAprobados(strFechaInicio, strFechaFin);
		for (String mid : lsMidAprobados) {
			intCountDetalle++;
			// Se agrega cabecera de archivo
			ImpresionTextoUtil.addDetalle("1" + mid 
											+ new SimpleDateFormat("yyMMdd").format(fechaActual)
											+ ImpresionTextoUtil.darEspacioBlanco(183), 1);
			List<DetalleArchivoCapturaDTO> lsDetalleArchivoCapturaDTO = objFacLogTramaPinpadDAOImpl
					.findTransaccionesAprobadasMid(mid, strFechaInicio, strFechaFin);
			// Se agrega detalle de archivo
			for (DetalleArchivoCapturaDTO detalleArchivoCapturaDTO : lsDetalleArchivoCapturaDTO) {
				intCountDetalle++;
				ImpresionTextoUtil.addDetalle("2" + detalleArchivoCapturaDTO.getTid() 
												+ detalleArchivoCapturaDTO.getFecha()
												+ detalleArchivoCapturaDTO.getHora() 
												+ detalleArchivoCapturaDTO.getSecuenciaTransaccion()
												+ detalleArchivoCapturaDTO.getNumeroAutorizacion() 
												+ detalleArchivoCapturaDTO.getLote()
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getValorTotal(), 13)
												+ "1"
												+ detalleArchivoCapturaDTO.getTipoCredito() 
												+ GenericUtil.convertIntegerToStringFormat(detalleArchivoCapturaDTO.getPlazoDiferido(), 2)
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getValorIva(), 13)
												+ detalleArchivoCapturaDTO.getValorServicio() 
												+ detalleArchivoCapturaDTO.getValorPropina()
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getValorInteres(), 13)
												+ detalleArchivoCapturaDTO.getMontoFijo() 
												+ detalleArchivoCapturaDTO.getValorIce()
												+ detalleArchivoCapturaDTO.getOtrosImpuestos() 
												+ detalleArchivoCapturaDTO.getCashOver()
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getMontoBaseSinIva(), 13)
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getMontoBaseConIva(), 13)
												+ ImpresionTextoUtil.darEspacioBlanco(13), 1);
//				ImpresionTextoUtil.addDetalle("2" + detalleArchivoCapturaDTO.getTid() 
//												+ detalleArchivoCapturaDTO.getFecha()
//												+ detalleArchivoCapturaDTO.getHora() 
//												+ detalleArchivoCapturaDTO.getSecuenciaTransaccion()
//												+ detalleArchivoCapturaDTO.getNumeroAutorizacion() 
//												+ detalleArchivoCapturaDTO.getLote()
//												+ GenericUtil.convertIntegerToStringFormat((int)(detalleArchivoCapturaDTO.getValorTotal() * 100), 13)
//												+ "1"
//												+ detalleArchivoCapturaDTO.getTipoCredito() 
//												+ GenericUtil.convertIntegerToStringFormat(detalleArchivoCapturaDTO.getPlazoDiferido(), 2)
//												+ GenericUtil.convertIntegerToStringFormat((int)(detalleArchivoCapturaDTO.getValorIva() * 100), 13)
//												+ detalleArchivoCapturaDTO.getValorServicio() 
//												+ detalleArchivoCapturaDTO.getValorPropina()
//												+ GenericUtil.convertIntegerToStringFormat((int)(detalleArchivoCapturaDTO.getValorInteres() * 100), 13)
//												+ detalleArchivoCapturaDTO.getMontoFijo() 
//												+ detalleArchivoCapturaDTO.getValorIce()
//												+ detalleArchivoCapturaDTO.getOtrosImpuestos() 
//												+ detalleArchivoCapturaDTO.getCashOver()
//												+ GenericUtil.convertIntegerToStringFormat((int)(detalleArchivoCapturaDTO.getMontoBaseSinIva() * 100), 13)
//												+ GenericUtil.convertIntegerToStringFormat((int)(detalleArchivoCapturaDTO.getMontoBaseConIva() * 100), 13)
//												+ ImpresionTextoUtil.darEspacioBlanco(13), 1);
			}
		}
		
		if (intCountDetalle <= 0)
			throw new BOException("pin.warn.sinRegistros");
		
		// Se agrega pie de archivo
		ImpresionTextoUtil.addPieLinea("9" + new SimpleDateFormat("yyMMdd").format(fechaActual)
										+ GenericUtil.convertIntegerToStringFormat(intCountDetalle, 6)
										+ ImpresionTextoUtil.darEspacioBlanco(187), 0);		

		// Se genera archivo
		lsLineasArchivo = ImpresionTextoUtil.generarDocumento();
		// Convierte StringBuilder
		for (String linea : lsLineasArchivo) {
			sb.append(linea);
		}
		try {
			//GENERA INPUT STREAM A PARTIR DE STRING BUILDER
			is = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
			//CREA ARCHIVO TEMPORAL
			file = Files.createTempFile("archivoCaptura", ".tmp");
			//AÑADE EL inputSteam del archivo temporal
			Files.write(file, IOUtils.toByteArray(is));
			// Retorna archivo temporal
			return file;
		} catch (Exception e) {
			throw new BOException("pin.warn.tempFile", new Object[] { e.getMessage() });
		} finally {
			if (ObjectUtils.isNotEmpty(is))
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.log(Level.SEVERE, "Exception: " + e);
				}
		}
	}
	
	public Path obtenerArchivoCapturaPreviaCarga(String strFechaInicio, String strFechaFin) throws BOExceptionUpt, ParseException {
		List<String> lsLineasArchivo;
		StringBuilder sb = new StringBuilder();
		ByteArrayInputStream is = null;
		Path file = null;
		Date fechaActual = new Date();
		if (!ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)) {
			if (!GenericUtil.validarFormatoFecha(strFechaInicio))
				throw new BOExceptionUpt("pin.warn.formatoFecha");
			if (!GenericUtil.validarFormatoFecha(strFechaFin))
				throw new BOExceptionUpt("pin.warn.formatoFecha");
			if (GenericUtil.esRangoFechaFutura(new SimpleDateFormat("dd/MM/yyyy").parse(strFechaFin),
					new SimpleDateFormat("dd/MM/yyyy").parse(strFechaInicio)))
				throw new BOExceptionUpt("pin.warn.fechaInicioFutura");
		} else if (!ObjectUtils.isEmpty(strFechaInicio) && ObjectUtils.isEmpty(strFechaFin)) {
			if (!GenericUtil.validarFormatoFecha(strFechaInicio))
				throw new BOExceptionUpt("pin.warn.formatoFecha");
		} else if (ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)) {
			if (!GenericUtil.validarFormatoFecha(strFechaFin))
				throw new BOExceptionUpt("pin.warn.formatoFecha");
		}
		Integer intCountDetalle = 0;
		List<String> lsMidAprobados = objFacLogTramaPinpadDAOImpl.obtenerMidAprobados(strFechaInicio, strFechaFin);
		for (String mid : lsMidAprobados) {
			intCountDetalle++;
			// Se agrega cabecera de archivo
			ImpresionTextoUtil.addDetalle("1" + mid 
											+ new SimpleDateFormat("yyMMdd").format(fechaActual)
											+ ImpresionTextoUtil.darEspacioBlanco(183), 1);
			List<DetalleArchivoCapturaDTO> lsDetalleArchivoCapturaDTO = objFacLogTramaPinpadDAOImpl
					.findTransaccionesAprobadasMid(mid, strFechaInicio, strFechaFin);
			// Se agrega detalle de archivo
			for (DetalleArchivoCapturaDTO detalleArchivoCapturaDTO : lsDetalleArchivoCapturaDTO) {
				intCountDetalle++;
				ImpresionTextoUtil.addDetalle("2" + detalleArchivoCapturaDTO.getTid() 
												+ detalleArchivoCapturaDTO.getFecha()
												+ detalleArchivoCapturaDTO.getHora() 
												+ detalleArchivoCapturaDTO.getSecuenciaTransaccion()
												+ detalleArchivoCapturaDTO.getNumeroAutorizacion() 
												+ detalleArchivoCapturaDTO.getLote()
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getValorTotal(), 13)
												+ "1"
												+ detalleArchivoCapturaDTO.getTipoCredito() 
												+ GenericUtil.convertIntegerToStringFormat(detalleArchivoCapturaDTO.getPlazoDiferido(), 2)
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getValorIva(), 13)
												+ detalleArchivoCapturaDTO.getValorServicio() 
												+ detalleArchivoCapturaDTO.getValorPropina()
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getValorInteres(), 13)
												+ detalleArchivoCapturaDTO.getMontoFijo() 
												+ detalleArchivoCapturaDTO.getValorIce()
												+ detalleArchivoCapturaDTO.getOtrosImpuestos() 
												+ detalleArchivoCapturaDTO.getCashOver()
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getMontoBaseSinIva(), 13)
												+ GenericUtil.convertDoubleToStringFormat(detalleArchivoCapturaDTO.getMontoBaseConIva(), 13)
												+ ImpresionTextoUtil.darEspacioBlanco(13), 1);
				
				// Actualización de nuevos campos de carga de cada transacción a datafast.
				if (ObjectUtils.isEmpty(detalleArchivoCapturaDTO.getSecuencia()))
					throw new BOExceptionUpt("pin.warn.noSecuenciaEnLista");
				
				Optional<FacLogTramaPinpad> opFacLogTramaPinpad = objFacLogTramaPinpadDAOImpl
						.find(detalleArchivoCapturaDTO.getSecuencia());
				
				if (ObjectUtils.isEmpty(opFacLogTramaPinpad))
					throw new BOExceptionUpt("pin.warn.secuenciaLogTrama");
				
				opFacLogTramaPinpad.get().setCargadoDatafast("S");
				opFacLogTramaPinpad.get().setFechaCargaDatafast(fechaActual);
				objFacLogTramaPinpadDAOImpl.update(opFacLogTramaPinpad.get());
			}
		}
		
		if (intCountDetalle <= 0)
			throw new BOExceptionUpt("pin.warn.sinRegistros");
		
		// Se agrega pie de archivo
		ImpresionTextoUtil.addPieLinea("9" + new SimpleDateFormat("yyMMdd").format(fechaActual)
										+ GenericUtil.convertIntegerToStringFormat(intCountDetalle, 6)
										+ ImpresionTextoUtil.darEspacioBlanco(187), 0);		

		// Se genera archivo
		lsLineasArchivo = ImpresionTextoUtil.generarDocumento();
		// Convierte StringBuilder
		for (String linea : lsLineasArchivo) {
			sb.append(linea);
		}
		try {
			//GENERA INPUT STREAM A PARTIR DE STRING BUILDER
			is = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
			//CREA ARCHIVO TEMPORAL
			file = Files.createTempFile("archivoCaptura", ".tmp");
			//AÑADE EL inputSteam del archivo temporal
			Files.write(file, IOUtils.toByteArray(is));
			
			cargaArchivoSftp(file);
			
			// Retorna archivo temporal
			return file;
		} catch (Exception e) {
			throw new BOExceptionUpt("pin.warn.tempFile", new Object[] { e.getMessage() });
		} finally {
			if (ObjectUtils.isNotEmpty(is))
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.log(Level.SEVERE, "Exception: " + e);
				}
		}
	}
	
	public String cargaArchivoSftp(Path file) throws BOExceptionUpt {
		String strFileName = new SimpleDateFormat("MMddyyyy").format(new Date()) + "."
				+ TipoArchivoEnum.VER.getExtension();

		Optional<DafXParametrosXEmpresa> objDafXParametrosXEmpresaOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.ROUTE_SFTP_DATAFAST.getName()));
		if (!objDafXParametrosXEmpresaOp.isPresent())
			throw new BOExceptionUpt("pin.warn.parametroNoEncontrado",
					new Object[] { ParametrosXEmpresa.ROUTE_SFTP_DATAFAST.getName() });
		String strRutaSftp = objDafXParametrosXEmpresaOp.get().getValorString();

		objDafXParametrosXEmpresaOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.DIR_SFTP_DATAFAST.getName()));
		if (!objDafXParametrosXEmpresaOp.isPresent())
			throw new BOExceptionUpt("pin.warn.parametroNoEncontrado",
					new Object[] { ParametrosXEmpresa.DIR_SFTP_DATAFAST.getName() });
		String strIpServer = objDafXParametrosXEmpresaOp.get().getValorString();

		objDafXParametrosXEmpresaOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.PORT_SFTP_DATAFAST.getName()));
		if (!objDafXParametrosXEmpresaOp.isPresent())
			throw new BOExceptionUpt("pin.warn.parametroNoEncontrado",
					new Object[] { ParametrosXEmpresa.PORT_SFTP_DATAFAST.getName() });
		Integer intPuerto = objDafXParametrosXEmpresaOp.get().getValorNumber().intValue();

		objDafXParametrosXEmpresaOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.USER_SFTP_DATAFAST.getName()));
		if (!objDafXParametrosXEmpresaOp.isPresent())
			throw new BOExceptionUpt("pin.warn.parametroNoEncontrado",
					new Object[] { ParametrosXEmpresa.USER_SFTP_DATAFAST.getName() });
		String strUser = objDafXParametrosXEmpresaOp.get().getValorString();

		objDafXParametrosXEmpresaOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.PASSWORD_SFTP_DATAFAST.getName()));
		if (!objDafXParametrosXEmpresaOp.isPresent())
			throw new BOExceptionUpt("pin.warn.parametroNoEncontrado",
					new Object[] { ParametrosXEmpresa.PASSWORD_SFTP_DATAFAST.getName() });
		String strPassword = objDafXParametrosXEmpresaOp.get().getValorString();

		try {
			SFTPUtil.uploadFile(file, strFileName, strRutaSftp, strIpServer,
					 intPuerto, strUser, strPassword);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			try {
				Files.deleteIfExists(file);
				logger.info("Eliminando archivo temporal debido a excepción...");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "Exception: " + e1.getMessage());
			}
			logger.log(Level.SEVERE, "Exception: " + e.getMessage());
			throw new BOExceptionUpt(e.getMessage(), e.getCause());
		}
		return strFileName;
	}
	
	public ProcesoReversoDatafastDTO procesoReversoAutomatico(String strIdCaja, String strCodigoUsuario, String strTipoTransaccion,
			Integer intCodigoRed, String strCodigoDiferido, Integer intPlazoDiferido, Integer intMesesGracia,
			Double douMontoBaseConIva, Double douMontoBaseSinIva, Double douSubTotal, Double douMontoIva,
			Double douMontoTotal, Date fecha, Date hora, FacLogTramaPinpad objFacLogTramaPinpad) throws BOException {
		ProcesoReversoDatafastDTO objProcesoReversoDatafastDTO = new ProcesoReversoDatafastDTO();
		// Valida campos obligatorios		
		GenericUtil.validarCampoRequeridoBO(strIdCaja, "pin.campos.idCaja");
		GenericUtil.validarCampoRequeridoBO(strCodigoUsuario, "pin.campos.codigoUsuario");
		GenericUtil.validarCampoRequeridoBO(intCodigoRed, "pin.campos.codigoRed");
		GenericUtil.validarCampoRequeridoBO(strCodigoDiferido, "pin.campos.codigoDiferido");
		GenericUtil.validarCampoRequeridoBO(douMontoTotal, "pin.campos.montoTotal");
		GenericUtil.validarCampoRequeridoBO(douMontoBaseSinIva, "pin.campos.montoBaseSinIva");
		String strIpPinpad = null;
		String strPuerto = null;
		String strMID = null;
		String strTID = null;
		Integer intTO = null;

		logger.info("Código de red recibido en reverso: " + intCodigoRed);
		FacCajasPinpad objFacCajasPinpad = objFacCajasPinpadDAOImpl.findByIdentificadorCaja(strIdCaja.trim());
		if (ObjectUtils.isEmpty(objFacCajasPinpad))
			throw new BOException("pin.warn.objetoCajasPinPad", new Object[] { strIdCaja });
		strIpPinpad = objFacCajasPinpad.getIpPinpad();
		strPuerto = objFacCajasPinpad.getPuertoPinpad();
		
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getMid()))
			throw new BOException("pin.warn.midPinpad");
		if (ObjectUtils.isEmpty(objFacCajasPinpad.getTid()))
			throw new BOException("pin.warn.tidPinpad");
		Optional<DafXParametrosXEmpresa> objTimeOutOp = objDafXParametrosXEmpresaDAOImpl
				.find(new DafXParametrosXEmpresaCPK((short) 1, ParametrosXEmpresa.TIME_OUT_PINPAD.getName()));
		if (!objTimeOutOp.isPresent())
			throw new BOException("pin.warn.toPinpad");
		
		strMID = objFacCajasPinpad.getMid();
		strTID = objFacCajasPinpad.getTid();
		intTO = objTimeOutOp.get().getValorNumber().intValue();
		String strIdReplace = strIdCaja;
		String strId = strIdReplace.substring(0, (strIdReplace.length() > 15) ? 15 : strIdReplace.length());
		// INICIALIZACIÓN DE ARCHIVO DE CONFIGURACIÓN CON DATOS DE PINPAD A CONECTARSE.
		DF.LANConfig config = new LANConfig(strIpPinpad, Integer.parseInt(strPuerto), intTO, strMID, strTID, strId, 2,
				2);
		DF.LAN lan = new DF.LAN(config);
		// OBTENCIÓN DE RESPUESTA.
		DF.RespuestaProcesoPago resp = null;
		try {
			DF.EnvioProcesoPago pago = new DF.EnvioProcesoPago();
			pago.TipoTransaccion = Integer.valueOf("04");
			pago.RedAdquirente = intCodigoRed;
			pago.CodigoDiferido = strCodigoDiferido;
			if (!ObjectUtils.isEmpty(intPlazoDiferido))
				pago.PlazoDiferido = intPlazoDiferido < 10 ? "0" + intPlazoDiferido.toString()
						: intPlazoDiferido.toString();
			pago.Base0 = GenericUtil.convertDoubleToStringFormatPP(douMontoBaseSinIva, 12);
			pago.IVA = GenericUtil.convertDoubleToStringFormatPP(douMontoIva, 12);
			pago.MontoTotal = GenericUtil.convertDoubleToStringFormatPP(douMontoTotal, 12);
			pago.Hora = hora;
			pago.Fecha = fecha;
			pago.TID = strTID;
			pago.MID = strMID;
			pago.CID = strId;
			resp = lan.ProcesoPago(pago);
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		}
		if (ObjectUtils.isEmpty(resp))
			throw new BOException("pin.warn.errorProcesoPagos");
		FacLogTramaPinpad objTrama = objDatafastUtil.cargaInsertObjetoTramaPinPadPP(resp, strIdCaja, strCodigoUsuario);
		if (!ObjectUtils.isEmpty(objTrama)) {
			objTrama.setCodigoDiferido(!ObjectUtils.isEmpty(strCodigoDiferido) ? strCodigoDiferido : "00");
			objTrama.setTipoTransaccion("04");
			objTrama.setCodTransaccionOriginal(!ObjectUtils.isEmpty(objFacLogTramaPinpad) ? objFacLogTramaPinpad.getSecuenciaTransaccion() : null);
			// Nuevo cambio, se agrega secuencia de log original para presición en búsqueda de anulaciones.
//			if (!ObjectUtils.isEmpty(objFacLogTramaPinpad))
//				objTrama.setFacLogTramaPinpadOriginal(objFacLogTramaPinpad);
			//
			objTrama.setPlazoDiferido(intPlazoDiferido);
			objTrama.setMid(strMID);
			objTrama.setTid(strTID);
			// CALCULO DE VALORES.
			objTrama.setMontoBaseConIva(douMontoBaseConIva);
			objTrama.setMontoBaseSinIva(douMontoBaseSinIva);
			objTrama.setSubtotal(douSubTotal);
			objTrama.setValorIva(douMontoIva);
			objTrama.setSubtotalBaseImponible(douMontoTotal);
			objTrama.setValorInteres(!ObjectUtils.isEmpty(objTrama.getValorInteres()) ? objTrama.getValorInteres() : 0.00);
			objTrama.setValorTotal(objTrama.getValorInteres() + douMontoTotal);
			objFacLogTramaPinpadDAOImpl.persist(objTrama);
			if (!ObjectUtils.isEmpty(objTrama.getError()))
				throw new BOException("pin.error.mensajeError", new Object[] { objTrama.getError() });
		} else
			throw new BOException("pin.warn.errorProcesoPagos");
		// Se carga DTO de respuesta.
		objProcesoReversoDatafastDTO = DatafastUtil.cargaResponseProcesoReverso(objTrama);
		if (ObjectUtils.isEmpty(objProcesoReversoDatafastDTO))
			throw new BOException("pin.warn.errorProcesoPagos");
		return objProcesoReversoDatafastDTO;
	}
	
}
