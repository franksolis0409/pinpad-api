/**
 * 
 */
package com.pinpad.ejb.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.pinpad.ejb.dao.impl.FacLiquidadorPinpadDAOImpl;
import com.pinpad.ejb.dao.impl.FacMarcasTarjetaPinpadDAOImpl;
import com.pinpad.ejb.dto.LecturaTarjetaDatafastDTO;
import com.pinpad.ejb.dto.ProcesoAnulacionPagoDatafastDTO;
import com.pinpad.ejb.dto.ProcesoConfiguracionDatafastDTO;
import com.pinpad.ejb.dto.ProcesoControlDatafastDTO;
import com.pinpad.ejb.dto.ProcesoPagoDatafastDTO;
import com.pinpad.ejb.dto.ProcesoReversoDatafastDTO;
import com.pinpad.ejb.enums.ModoLectura;
import com.pinpad.ejb.enums.TipoTarjeta;
import com.pinpad.ejb.exceptions.BOException;
import com.pinpad.ejb.model.FacLiquidadorPinpad;
import com.pinpad.ejb.model.FacLogTramaPinpad;
import com.pinpad.ejb.model.FacMarcasTarjetaPinpad;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author H P
 *
 */
@Stateless
public class DatafastUtil {

	//private static final Logger logger = LogManager.getLogger(DatafastUtil.class);
	private static final Logger logger = Logger.getLogger(DatafastUtil.class.getName());

	@EJB
	private FacLiquidadorPinpadDAOImpl objFacLiquidadorPinpadDAOImpl;

	@EJB
	private FacMarcasTarjetaPinpadDAOImpl objFacMarcasTarjetaPinpadDAOImpl;

	/**
	 * Carga y procesa objeto de tipo log trama pinpad para su posterior insert en
	 * Logs.
	 * 
	 * @author Frank Solis
	 * @param resp
	 * @param strIdCaja
	 * @param strCodUsuario
	 * @return BOException
	 * @throws BOException
	 */
	public FacLogTramaPinpad cargaInsertObjetoTramaPinPadLT(DF.RespuestaLecturaTarjeta resp, String strIdCaja,
			String strCodUsuario) throws BOException {
		FacLogTramaPinpad objTrama = new FacLogTramaPinpad();
		try {
			if (!ObjectUtils.isEmpty(resp)) {
				String strTipoMensaje = resp.TipoMensaje;
				String strCodResponse = resp.CodigoRespuesta;
				objTrama.setCid(strIdCaja);
				objTrama.setCodigoRespuesta(strCodResponse);
				objTrama.setTipoMensaje(strTipoMensaje);
				objTrama.setUsuarioIngreso(strCodUsuario);
				switch (strCodResponse.trim()) {
				case "00":
					switch (strTipoMensaje.trim()) {
					case "LT":
						objTrama.setRedCorriente(resp.RedCorriente);
						objTrama.setRedDiferido(resp.RedDiferido);
						objTrama.setNumTarjetaTruncado(resp.TarjetaTruncada);
						objTrama.setFechaVctoTarjeta(resp.FechaVencimiento);
						objTrama.setNumTarjetaEncriptado(resp.NumeroTarjeta);
						objTrama.setRespuestaPinPad(resp.MensajeRespuesta.trim());
						objTrama.setBinTarjeta(resp.BinTarjeta);
						break;
					default:
						break;
					}
					break;
				case "01":
					objTrama.setError("Respuesta de PINPAD: Error en trama.");
					break;
				case "02":
					objTrama.setError("Respuesta de PINPAD: Error conexión PinPad.");
					break;
				case "20":
					switch (strTipoMensaje.trim()) {
					case "LT":
						objTrama.setRespuestaPinPad(resp.MensajeRespuesta);
						objTrama.setError(
								"Respuesta de PINPAD: Error durante proceso - " + resp.MensajeRespuesta.trim());
						break;
					default:
						break;
					}
					break;
				default:
					objTrama.setRespuestaPinPad(resp.MensajeRespuesta);
					objTrama.setError("Respuesta de PINPAD: Error durante proceso - " + resp.MensajeRespuesta.trim());
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		}
		return objTrama;
	}

	public FacLogTramaPinpad cargaInsertObjetoTramaPinPadPP(DF.RespuestaProcesoPago resp, String strIdCaja,
			String strCodUsuario) throws BOException {
		FacLogTramaPinpad objTrama = new FacLogTramaPinpad();
		try {
			if (!ObjectUtils.isEmpty(resp)) {
				String strTipoMensaje = resp.TipoMensaje;
				String strCodResponse = resp.CodigoRespuesta;
				objTrama.setCid(strIdCaja);
				objTrama.setCodigoRespuesta(strCodResponse);
				objTrama.setTipoMensaje(strTipoMensaje);
				objTrama.setUsuarioIngreso(strCodUsuario);
				objTrama.setTramaPos(resp.ObtenerTrama());
				switch (strCodResponse.trim()) {
				case "00":
					switch (strTipoMensaje.trim()) {
					case "PP":
						if (!ObjectUtils.isEmpty(resp.CodigoRespuestaAut)) {
							if (resp.CodigoRespuestaAut.trim().equals("00")) {
								objTrama.setCodigoRed(resp.RedAdquirente.trim());
								objTrama.setCodRespuestaAutorizador(resp.CodigoRespuestaAut.trim());
								objTrama.setRespuestaPinPad(resp.MensajeRespuestaAut.trim());
								objTrama.setSecuenciaTransaccion(resp.Referencia.trim());
								objTrama.setLote(resp.Lote.trim());
								objTrama.setHora(resp.Hora.trim());
								objTrama.setFecha(resp.Fecha.trim());
								objTrama.setNumeroAutorizacion(resp.Autorizacion.trim());
								objTrama.setTerminalId(resp.TID.trim());
								String strInteres = resp.Interes.trim();
								Double douInteres = null;
								if (!ObjectUtils.isEmpty(strInteres))
									douInteres = Double.parseDouble(
											strInteres.substring(1, 10) + '.' + strInteres.substring(10, 12));
								objTrama.setValorInteres(ObjectUtils.isEmpty(douInteres) ? 0.00 : douInteres);
								objTrama.setCodBancoAdquirente(resp.CodigoAdquirente.trim());
								objTrama.setCodModoLectura(resp.ModoLectura.trim());
								objTrama.setTarjetaHabiente(resp.TarjetaHabiente.trim());
								objTrama.setAplicacionLabel(resp.AplicacionEMV.trim());
								objTrama.setAidEmv(resp.AID.trim());
								objTrama.setTipoCriptograma(resp.Criptograma.trim());
								objTrama.setVerificacionPIN(resp.PIN.trim());
								objTrama.setArqc(resp.ARQC.trim());
								objTrama.setTvr(resp.TVR.trim());
								objTrama.setTsi(resp.TSI.trim());
								objTrama.setNumTarjetaTruncado(resp.NumeroTajeta.trim());
								objTrama.setFechaVctoTarjeta(resp.FechaVencimiento.trim());
								objTrama.setNumTarjetaEncriptado(resp.NumerTarjetaEncriptado.trim());
								objTrama.setGrupoTarjeta(resp.AplicacionEMV);// Momentáneo porque no nos devuelven grupo
																				// tarjeta.
								// =============== HOMOLOGACION DE DATOS DE PAGO ==============//
								// Obtiene Banco Liquidador Homologado.
								if (!ObjectUtils.isEmpty(objTrama.getCodBancoAdquirente())) {
									FacLiquidadorPinpad objFacLiquidadorPinpad = objFacLiquidadorPinpadDAOImpl
											.findByCodigAdquirente(objTrama.getCodBancoAdquirente().trim());
									if (ObjectUtils.isEmpty(objFacLiquidadorPinpad))
										objTrama.setErrorInterno(MensajesUtil.getMensaje("pin.warn.bancoAdquirente",
												MensajesUtil.Locale));
									else if (ObjectUtils.isEmpty(objFacLiquidadorPinpad.getDafInstituciones()))
										objTrama.setErrorInterno(MensajesUtil.getMensaje("pin.warn.codigoInstitucion",
												new Object[] { objFacLiquidadorPinpad.getCodigoLiquidador() },
												MensajesUtil.Locale));
									else {
										objTrama.setCodBancoLiquidador(objFacLiquidadorPinpad.getDafInstituciones()
												.getDafInstitucionesCPK().getCodigoInstitucion());
										objTrama.setNombreBancoLiquidador(
												objFacLiquidadorPinpad.getDafInstituciones().getNombreInstitucion());
									}
								}
								// Obtiene Banco Emisor Homologado.
								if (!ObjectUtils.isEmpty(objTrama.getBancoEmisor())) {
									FacLiquidadorPinpad objFacLiquidadorPinpad = objFacLiquidadorPinpadDAOImpl
											.findByCodigAdquirente(objTrama.getBancoEmisor().trim());
									if (ObjectUtils.isEmpty(objFacLiquidadorPinpad))
										objTrama.setErrorInterno(
												MensajesUtil.getMensaje("pin.warn.bancoEmisor", MensajesUtil.Locale));
									else if (ObjectUtils.isEmpty(objFacLiquidadorPinpad.getDafInstituciones()))
										objTrama.setErrorInterno(MensajesUtil.getMensaje("pin.warn.codigoInstitucionEmisor",
												new Object[] { objFacLiquidadorPinpad.getCodigoLiquidador() },
												MensajesUtil.Locale));
									else {
										objTrama.setCodBancoEmisor(
												objFacLiquidadorPinpad.getDafInstituciones().getDafInstitucionesCPK()
														.getCodigoInstitucion());
										objTrama.setNombreBancoEmisor(
												objFacLiquidadorPinpad.getDafInstituciones().getNombreInstitucion());
									}
								}
								// Obtiene el grupo de tarjeta homologado.
								if (!ObjectUtils.isEmpty(objTrama.getGrupoTarjeta())) {
									FacMarcasTarjetaPinpad objFacMarcasTarjetaPinpad = objFacMarcasTarjetaPinpadDAOImpl
											.findByGrupoTarjeta(objTrama.getGrupoTarjeta().trim());
									if (ObjectUtils.isEmpty(objFacMarcasTarjetaPinpad))
										objTrama.setErrorInterno(
												MensajesUtil.getMensaje("pin.warn.grupoTarjeta", MensajesUtil.Locale));
									else if (ObjectUtils
											.isEmpty(objFacMarcasTarjetaPinpad.getDafMarcasTarjetaCredito()))
										objTrama.setErrorInterno(MensajesUtil.getMensaje("pin.warn.codigoTarjetaGrupo",
												new Object[] { objFacMarcasTarjetaPinpad.getGrupoTarjeta() },
												MensajesUtil.Locale));
									else {
										objTrama.setCodTarjeta(
												new BigDecimal(objFacMarcasTarjetaPinpad.getDafMarcasTarjetaCredito()
														.getDafMarcasTarjetaCreditoCPK().getCodigoMarcaTc()));
										objTrama.setNombreTarjeta(objFacMarcasTarjetaPinpad.getDafMarcasTarjetaCredito()
												.getNombreMarcaTc());
									}
								}
								// LÓGICA PARA OBTENCIÓN DE TIPO DE TARJETA.
								if (!ObjectUtils.isEmpty(objTrama.getAplicacionLabel())) {
									if (GenericUtil.contieneValorString(objTrama.getAplicacionLabel(),
											TipoTarjeta.CREDIT.getName()))
										objTrama.setTipoTarjeta(TipoTarjeta.CREDITO.getName());
									else if (GenericUtil.contieneValorString(objTrama.getAplicacionLabel(),
											TipoTarjeta.DEBIT.getName()))
										objTrama.setTipoTarjeta(TipoTarjeta.DEBITO.getName());
								}
								// Obtiene el modo de lectura.
								if (!ObjectUtils.isEmpty(objTrama.getCodModoLectura())) {
									switch (objTrama.getCodModoLectura().trim()) {
									case "01":
										objTrama.setModoLectura(ModoLectura.MANUAL.getName());
										break;
									case "02":
										objTrama.setModoLectura(ModoLectura.BANDA.getName());
										break;
									case "03":
										objTrama.setModoLectura(ModoLectura.CHIP.getName());
										break;
									case "04":
										objTrama.setModoLectura(ModoLectura.FALLBACK_MANUAL.getName());
										break;
									case "05":
										objTrama.setModoLectura(ModoLectura.FALLBACK_BANDA.getName());
										break;
									case "06":
										objTrama.setModoLectura(ModoLectura.CONTACTLESS.getName());
										break;
									default:
										break;
									}
								}
							} else {
								objTrama.setRespuestaPinPad(resp.MensajeRespuestaAut);
								objTrama.setError("Respuesta de PINPAD: Error durante proceso - "
										+ resp.MensajeRespuestaAut.trim());
							}
						} else {
							objTrama.setRespuestaPinPad(resp.MensajeRespuestaAut);
							objTrama.setCodigoRed(resp.RedAdquirente);
						}
						break;
					default:
						break;
					}
					break;
				case "01":
					objTrama.setError("Respuesta de PINPAD: Error en trama.");
					break;
				case "02":
					objTrama.setError("Respuesta de PINPAD: Error conexión PinPad.");
					break;
				case "20":
					switch (strTipoMensaje.trim()) {
					case "PP":
						objTrama.setRespuestaPinPad(resp.MensajeRespuestaAut);
						objTrama.setError(
								"Respuesta de PINPAD: Error durante proceso - " + resp.MensajeRespuestaAut.trim());
						break;
					default:
						break;
					}
					break;
				default:
					objTrama.setRespuestaPinPad(resp.MensajeRespuestaAut);
					objTrama.setError(
							"Respuesta de PINPAD: Error durante proceso - " + resp.MensajeRespuestaAut.trim());
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		}
		return objTrama;
	}

	public FacLogTramaPinpad cargaInsertObjetoTramaPinPadPC(DF.RespuestaProcesoControl resp, String strIdCaja,
			String strCodUsuario) {
		FacLogTramaPinpad objTrama = new FacLogTramaPinpad();
		try {
			if (!ObjectUtils.isEmpty(resp)) {
				String strTipoMensaje = resp.TipoMensaje;
				String strCodResponse = resp.CodigoRespuesta;
				objTrama.setCid(strIdCaja);
				objTrama.setCodigoRespuesta(strCodResponse);
				objTrama.setTipoMensaje(strTipoMensaje);
				objTrama.setUsuarioIngreso(strCodUsuario);
				switch (strCodResponse.trim()) {
				case "00":
					switch (strTipoMensaje.trim()) {
					case "PC":
						objTrama.setRespuestaPinPad(resp.MensajeRespuesta.trim());
						break;
					default:
						break;
					}
					break;
				case "01":
					objTrama.setError("Respuesta de PINPAD: Error en trama.");
					break;
				case "02":
					objTrama.setError("Respuesta de PINPAD: Error conexión PinPad.");
					break;
				case "20":
					switch (strTipoMensaje.trim()) {
					case "PC":
						objTrama.setRespuestaPinPad(resp.MensajeRespuesta);
						objTrama.setError(
								"Respuesta de PINPAD: Error durante proceso - " + resp.MensajeRespuesta.trim());
						break;
					default:
						break;
					}
					break;
				case "TO":
					objTrama.setError("Respuesta de PINPAD: Error de Timeout.");
					break;
				case "ER":
					objTrama.setError("Respuesta de PINPAD: Error conexión PinPad.");
					break;
				default:
					objTrama.setRespuestaPinPad(resp.MensajeRespuesta);
					objTrama.setError("Respuesta de PINPAD: Error durante proceso - " + resp.MensajeRespuesta.trim());
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
		}
		return objTrama;
	}

	public static ProcesoPagoDatafastDTO cargaResponseProcesoPagos(FacLogTramaPinpad objTrama) {
		ProcesoPagoDatafastDTO objProcesoPagoDatafastDTO = new ProcesoPagoDatafastDTO();
		objProcesoPagoDatafastDTO.setSecuencia(new BigDecimal(objTrama.getSecuencia()));
		objProcesoPagoDatafastDTO.setCid(objTrama.getCid());
		objProcesoPagoDatafastDTO.setCodigoRespuesta(objTrama.getCodigoRespuesta());
		objProcesoPagoDatafastDTO.setTipoMensaje(objTrama.getTipoMensaje());
		objProcesoPagoDatafastDTO.setUsuarioIngreso(objTrama.getUsuarioIngreso());
		objProcesoPagoDatafastDTO.setTramaPos(objTrama.getTramaPos());
		objProcesoPagoDatafastDTO.setCodigoRed(objTrama.getCodigoRed());
		objProcesoPagoDatafastDTO.setCodRespuestaAutorizador(objTrama.getCodRespuestaAutorizador());
		objProcesoPagoDatafastDTO.setRespuestaPinPad(objTrama.getRespuestaPinPad());
		objProcesoPagoDatafastDTO.setSecuenciaTransaccion(objTrama.getSecuenciaTransaccion());
		objProcesoPagoDatafastDTO.setLote(objTrama.getLote());
		objProcesoPagoDatafastDTO.setHora(objTrama.getHora());
		objProcesoPagoDatafastDTO.setFecha(objTrama.getFecha());
		objProcesoPagoDatafastDTO.setNumeroAutorizacion(objTrama.getNumeroAutorizacion());
		objProcesoPagoDatafastDTO.setTerminalId(objTrama.getTerminalId());
		objProcesoPagoDatafastDTO.setCodBancoAdquirente(objTrama.getCodBancoAdquirente());
		objProcesoPagoDatafastDTO.setCodModoLectura(objTrama.getCodModoLectura());
		objProcesoPagoDatafastDTO.setTarjetaHabiente(objTrama.getTarjetaHabiente());
		objProcesoPagoDatafastDTO.setAplicacionLabel(objTrama.getAplicacionLabel());
		objProcesoPagoDatafastDTO.setAidEmv(objTrama.getAidEmv());
		objProcesoPagoDatafastDTO.setTipoCriptograma(objTrama.getTipoCriptograma());
		objProcesoPagoDatafastDTO.setVerificacionPIN(objTrama.getVerificacionPIN());
		objProcesoPagoDatafastDTO.setArqc(objTrama.getArqc());
		objProcesoPagoDatafastDTO.setTvr(objTrama.getTvr());
		objProcesoPagoDatafastDTO.setTsi(objTrama.getTsi());
		objProcesoPagoDatafastDTO.setNumTarjetaTruncado(objTrama.getNumTarjetaTruncado());
		objProcesoPagoDatafastDTO.setFechaVctoTarjeta(objTrama.getFechaVctoTarjeta());
		objProcesoPagoDatafastDTO.setNumTarjetaEncriptado(objTrama.getNumTarjetaEncriptado());
		objProcesoPagoDatafastDTO.setCodBancoLiquidador(objTrama.getCodBancoLiquidador());
		objProcesoPagoDatafastDTO.setNombreBancoLiquidador(objTrama.getNombreBancoLiquidador());
		objProcesoPagoDatafastDTO.setCodBancoEmisor(objTrama.getCodBancoEmisor());
		objProcesoPagoDatafastDTO.setNombreBancoEmisor(objTrama.getNombreBancoEmisor());
		objProcesoPagoDatafastDTO.setCodTarjeta(objTrama.getCodTarjeta());
		objProcesoPagoDatafastDTO.setNombreTarjeta(objTrama.getNombreTarjeta());
		objProcesoPagoDatafastDTO.setTipoTarjeta(objTrama.getTipoTarjeta());
		objProcesoPagoDatafastDTO.setModoLectura(objTrama.getModoLectura());
		objProcesoPagoDatafastDTO.setTipoTransaccion(objTrama.getTipoTransaccion());
		objProcesoPagoDatafastDTO.setPlazoDiferido(objTrama.getPlazoDiferido());
		objProcesoPagoDatafastDTO.setMontoBaseConIva(objTrama.getMontoBaseConIva());
		objProcesoPagoDatafastDTO.setMontoBaseSinIva(objTrama.getMontoBaseSinIva());
		objProcesoPagoDatafastDTO.setSubtotal(objTrama.getSubtotal());
		objProcesoPagoDatafastDTO.setValorIva(objTrama.getValorIva());
		objProcesoPagoDatafastDTO.setSubtotalBaseImponible(objTrama.getSubtotalBaseImponible());
		objProcesoPagoDatafastDTO.setValorInteres(objTrama.getValorInteres());
		objProcesoPagoDatafastDTO.setValorTotal(objTrama.getValorTotal());
		return objProcesoPagoDatafastDTO;
	}

	public static ProcesoAnulacionPagoDatafastDTO cargaResponseAnulacionPago(FacLogTramaPinpad objTrama) {
		ProcesoAnulacionPagoDatafastDTO objProcesoAnulacionPagoDatafastDTO = new ProcesoAnulacionPagoDatafastDTO();
		objProcesoAnulacionPagoDatafastDTO.setSecuenciaTransaccionOriginal(objTrama.getCodTransaccionOriginal());
		objProcesoAnulacionPagoDatafastDTO.setSecuencia(new BigDecimal(objTrama.getSecuencia()));
		objProcesoAnulacionPagoDatafastDTO.setCid(objTrama.getCid());
		objProcesoAnulacionPagoDatafastDTO.setCodigoRespuesta(objTrama.getCodigoRespuesta());
		objProcesoAnulacionPagoDatafastDTO.setTipoMensaje(objTrama.getTipoMensaje());
		objProcesoAnulacionPagoDatafastDTO.setUsuarioIngreso(objTrama.getUsuarioIngreso());
		objProcesoAnulacionPagoDatafastDTO.setTramaPos(objTrama.getTramaPos());
		objProcesoAnulacionPagoDatafastDTO.setCodigoRed(objTrama.getCodigoRed());
		objProcesoAnulacionPagoDatafastDTO.setCodRespuestaAutorizador(objTrama.getCodRespuestaAutorizador());
		objProcesoAnulacionPagoDatafastDTO.setRespuestaPinPad(objTrama.getRespuestaPinPad());
		objProcesoAnulacionPagoDatafastDTO.setSecuenciaTransaccion(objTrama.getSecuenciaTransaccion());
		objProcesoAnulacionPagoDatafastDTO.setLote(objTrama.getLote());
		objProcesoAnulacionPagoDatafastDTO.setHora(objTrama.getHora());
		objProcesoAnulacionPagoDatafastDTO.setFecha(objTrama.getFecha());
		objProcesoAnulacionPagoDatafastDTO.setNumeroAutorizacion(objTrama.getNumeroAutorizacion());
		objProcesoAnulacionPagoDatafastDTO.setTerminalId(objTrama.getTerminalId());
		objProcesoAnulacionPagoDatafastDTO.setCodBancoAdquirente(objTrama.getCodBancoAdquirente());
		objProcesoAnulacionPagoDatafastDTO.setCodModoLectura(objTrama.getCodModoLectura());
		objProcesoAnulacionPagoDatafastDTO.setTarjetaHabiente(objTrama.getTarjetaHabiente());
		objProcesoAnulacionPagoDatafastDTO.setAplicacionLabel(objTrama.getAplicacionLabel());
		objProcesoAnulacionPagoDatafastDTO.setAidEmv(objTrama.getAidEmv());
		objProcesoAnulacionPagoDatafastDTO.setTipoCriptograma(objTrama.getTipoCriptograma());
		objProcesoAnulacionPagoDatafastDTO.setVerificacionPIN(objTrama.getVerificacionPIN());
		objProcesoAnulacionPagoDatafastDTO.setArqc(objTrama.getArqc());
		objProcesoAnulacionPagoDatafastDTO.setTvr(objTrama.getTvr());
		objProcesoAnulacionPagoDatafastDTO.setTsi(objTrama.getTsi());
		objProcesoAnulacionPagoDatafastDTO.setNumTarjetaTruncado(objTrama.getNumTarjetaTruncado());
		objProcesoAnulacionPagoDatafastDTO.setFechaVctoTarjeta(objTrama.getFechaVctoTarjeta());
		objProcesoAnulacionPagoDatafastDTO.setNumTarjetaEncriptado(objTrama.getNumTarjetaEncriptado());
		objProcesoAnulacionPagoDatafastDTO.setCodBancoLiquidador(objTrama.getCodBancoLiquidador());
		objProcesoAnulacionPagoDatafastDTO.setNombreBancoLiquidador(objTrama.getNombreBancoLiquidador());
		objProcesoAnulacionPagoDatafastDTO.setCodBancoEmisor(objTrama.getCodBancoEmisor());
		objProcesoAnulacionPagoDatafastDTO.setNombreBancoEmisor(objTrama.getNombreBancoEmisor());
		objProcesoAnulacionPagoDatafastDTO.setCodTarjeta(objTrama.getCodTarjeta());
		objProcesoAnulacionPagoDatafastDTO.setNombreTarjeta(objTrama.getNombreTarjeta());
		objProcesoAnulacionPagoDatafastDTO.setTipoTarjeta(objTrama.getTipoTarjeta());
		objProcesoAnulacionPagoDatafastDTO.setModoLectura(objTrama.getModoLectura());
		objProcesoAnulacionPagoDatafastDTO.setTipoTransaccion(objTrama.getTipoTransaccion());
		objProcesoAnulacionPagoDatafastDTO.setPlazoDiferido(objTrama.getPlazoDiferido());
		objProcesoAnulacionPagoDatafastDTO.setMontoBaseConIva(objTrama.getMontoBaseConIva());
		objProcesoAnulacionPagoDatafastDTO.setMontoBaseSinIva(objTrama.getMontoBaseSinIva());
		objProcesoAnulacionPagoDatafastDTO.setSubtotal(objTrama.getSubtotal());
		objProcesoAnulacionPagoDatafastDTO.setValorIva(objTrama.getValorIva());
		objProcesoAnulacionPagoDatafastDTO.setSubtotalBaseImponible(objTrama.getSubtotalBaseImponible());
		objProcesoAnulacionPagoDatafastDTO.setValorInteres(objTrama.getValorInteres());
		objProcesoAnulacionPagoDatafastDTO.setValorTotal(objTrama.getValorTotal());
		return objProcesoAnulacionPagoDatafastDTO;
	}

	public static ProcesoControlDatafastDTO cargaResponseProcesoControl(FacLogTramaPinpad objTrama) {
		ProcesoControlDatafastDTO objProcesoControlDatafastDTO = new ProcesoControlDatafastDTO();
		objProcesoControlDatafastDTO.setSecuencia(new BigDecimal(objTrama.getSecuencia()));
		objProcesoControlDatafastDTO.setCid(objTrama.getCid().trim());
		objProcesoControlDatafastDTO.setCodigoRespuesta(objTrama.getCodigoRespuesta().trim());
		objProcesoControlDatafastDTO.setTipoMensaje(objTrama.getTipoMensaje().trim());
		objProcesoControlDatafastDTO.setMensajeRespuesta(objTrama.getRespuestaPinPad().trim());
		return objProcesoControlDatafastDTO;
	}

	public static LecturaTarjetaDatafastDTO cargaResponseLecturaTarjeta(FacLogTramaPinpad objTrama) {
		LecturaTarjetaDatafastDTO objLecturaTarjetaDatafastDTO = new LecturaTarjetaDatafastDTO();
		objLecturaTarjetaDatafastDTO.setSecuencia(new BigDecimal(objTrama.getSecuencia()));
		objLecturaTarjetaDatafastDTO.setCid(objTrama.getCid());
		objLecturaTarjetaDatafastDTO.setCodigoRespuesta(objTrama.getCodigoRespuesta().trim());
		objLecturaTarjetaDatafastDTO.setTipoMensaje(objTrama.getTipoMensaje().trim());
		objLecturaTarjetaDatafastDTO.setRedCorriente(objTrama.getRedCorriente().trim());
		objLecturaTarjetaDatafastDTO.setRedDiferido(objTrama.getRedDiferido().trim());
		objLecturaTarjetaDatafastDTO.setNumeroTarjeta(objTrama.getNumTarjetaTruncado().trim());
		objLecturaTarjetaDatafastDTO.setFechaVencimiento(objTrama.getFechaVctoTarjeta().trim());
		objLecturaTarjetaDatafastDTO.setRespuestaPinPad(objTrama.getRespuestaPinPad().trim());
		objLecturaTarjetaDatafastDTO.setBinTarjeta(objTrama.getBinTarjeta().trim());
		objLecturaTarjetaDatafastDTO.setNumTarjetaEncriptado(objTrama.getNumTarjetaEncriptado().trim());
		return objLecturaTarjetaDatafastDTO;
	}

	public FacLogTramaPinpad cargaInsertObjetoTramaPinPadPConfig(DF.RespuestaProcesoConfigPinPad resp, String strIdCaja,
			String strCodUsuario) {
		FacLogTramaPinpad objTrama = new FacLogTramaPinpad();
		try {
			if (!ObjectUtils.isEmpty(resp)) {
				String strTipoMensaje = resp.TipoMensaje;
				String strCodResponse = resp.CodigoRespuesta;
				objTrama.setCid(strIdCaja);
				objTrama.setCodigoRespuesta(strCodResponse);
				objTrama.setTipoMensaje(strTipoMensaje);
				objTrama.setUsuarioIngreso(strCodUsuario);
				switch (strCodResponse.trim()) {
				case "00":
					switch (strTipoMensaje.trim()) {
					case "CB":
						objTrama.setRespuestaPinPad(resp.MensajeRespuesta.trim());
						break;
					default:
						break;
					}
					break;
				case "01":
					objTrama.setError("Respuesta de PINPAD: Error en trama.");
					break;
				case "02":
					objTrama.setError("Respuesta de PINPAD: Error conexión PinPad.");
					break;
				case "20":
					switch (strTipoMensaje.trim()) {
					case "CB":
						objTrama.setRespuestaPinPad(resp.MensajeRespuesta);
						objTrama.setError(
								"Respuesta de PINPAD: Error durante proceso - " + resp.MensajeRespuesta.trim());
						break;
					default:
						break;
					}
					break;
				case "TO":
					objTrama.setError("Respuesta de PINPAD: Error de Timeout.");
					break;
				case "ER":
					objTrama.setError("Respuesta de PINPAD: Error conexión PinPad.");
					break;
				default:
					objTrama.setRespuestaPinPad(resp.MensajeRespuesta);
					objTrama.setError("Respuesta de PINPAD: Error durante proceso - " + resp.MensajeRespuesta.trim());
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, "Exception: " + e);
		}
		return objTrama;
	}

	public static ProcesoConfiguracionDatafastDTO cargaResponseProcesoConfiguracion(FacLogTramaPinpad objTrama) {
		ProcesoConfiguracionDatafastDTO objProcesoConfiguracionDatafastDTO = new ProcesoConfiguracionDatafastDTO();
		objProcesoConfiguracionDatafastDTO.setSecuencia(new BigDecimal(objTrama.getSecuencia()));
		objProcesoConfiguracionDatafastDTO.setCid(objTrama.getCid().trim());
		objProcesoConfiguracionDatafastDTO.setCodigoRespuesta(objTrama.getCodigoRespuesta().trim());
		objProcesoConfiguracionDatafastDTO.setTipoMensaje(objTrama.getTipoMensaje().trim());
		objProcesoConfiguracionDatafastDTO.setMensajeRespuesta(
				!ObjectUtils.isEmpty(objTrama.getRespuestaPinPad()) ? objTrama.getRespuestaPinPad().trim() : null);
		return objProcesoConfiguracionDatafastDTO;
	}

	public static ProcesoReversoDatafastDTO cargaResponseProcesoReverso(FacLogTramaPinpad objTrama) {
		ProcesoReversoDatafastDTO objProcesoReversoDatafastDTO = new ProcesoReversoDatafastDTO();
		objProcesoReversoDatafastDTO.setSecuencia(new BigDecimal(objTrama.getSecuencia()));
		objProcesoReversoDatafastDTO.setCid(objTrama.getCid());
		objProcesoReversoDatafastDTO.setCodigoRespuesta(objTrama.getCodigoRespuesta());
		objProcesoReversoDatafastDTO.setTipoMensaje(objTrama.getTipoMensaje());
		objProcesoReversoDatafastDTO.setUsuarioIngreso(objTrama.getUsuarioIngreso());
		objProcesoReversoDatafastDTO.setTramaPos(objTrama.getTramaPos());
		objProcesoReversoDatafastDTO.setCodigoRed(objTrama.getCodigoRed());
		objProcesoReversoDatafastDTO.setCodRespuestaAutorizador(objTrama.getCodRespuestaAutorizador());
		objProcesoReversoDatafastDTO.setRespuestaPinPad(objTrama.getRespuestaPinPad());
		objProcesoReversoDatafastDTO.setSecuenciaTransaccion(objTrama.getSecuenciaTransaccion());
		objProcesoReversoDatafastDTO.setLote(objTrama.getLote());
		objProcesoReversoDatafastDTO.setHora(objTrama.getHora());
		objProcesoReversoDatafastDTO.setFecha(objTrama.getFecha());
		objProcesoReversoDatafastDTO.setNumeroAutorizacion(objTrama.getNumeroAutorizacion());
		objProcesoReversoDatafastDTO.setTerminalId(objTrama.getTerminalId());
		objProcesoReversoDatafastDTO.setCodBancoAdquirente(objTrama.getCodBancoAdquirente());
		objProcesoReversoDatafastDTO.setCodModoLectura(objTrama.getCodModoLectura());
		objProcesoReversoDatafastDTO.setTarjetaHabiente(objTrama.getTarjetaHabiente());
		objProcesoReversoDatafastDTO.setAplicacionLabel(objTrama.getAplicacionLabel());
		objProcesoReversoDatafastDTO.setAidEmv(objTrama.getAidEmv());
		objProcesoReversoDatafastDTO.setTipoCriptograma(objTrama.getTipoCriptograma());
		objProcesoReversoDatafastDTO.setVerificacionPIN(objTrama.getVerificacionPIN());
		objProcesoReversoDatafastDTO.setArqc(objTrama.getArqc());
		objProcesoReversoDatafastDTO.setTvr(objTrama.getTvr());
		objProcesoReversoDatafastDTO.setTsi(objTrama.getTsi());
		objProcesoReversoDatafastDTO.setNumTarjetaTruncado(objTrama.getNumTarjetaTruncado());
		objProcesoReversoDatafastDTO.setFechaVctoTarjeta(objTrama.getFechaVctoTarjeta());
		objProcesoReversoDatafastDTO.setNumTarjetaEncriptado(objTrama.getNumTarjetaEncriptado());
		objProcesoReversoDatafastDTO.setCodBancoLiquidador(objTrama.getCodBancoLiquidador());
		objProcesoReversoDatafastDTO.setNombreBancoLiquidador(objTrama.getNombreBancoLiquidador());
		objProcesoReversoDatafastDTO.setCodBancoEmisor(objTrama.getCodBancoEmisor());
		objProcesoReversoDatafastDTO.setNombreBancoEmisor(objTrama.getNombreBancoEmisor());
		objProcesoReversoDatafastDTO.setCodTarjeta(objTrama.getCodTarjeta());
		objProcesoReversoDatafastDTO.setNombreTarjeta(objTrama.getNombreTarjeta());
		objProcesoReversoDatafastDTO.setTipoTarjeta(objTrama.getTipoTarjeta());
		objProcesoReversoDatafastDTO.setModoLectura(objTrama.getModoLectura());
		objProcesoReversoDatafastDTO.setTipoTransaccion(objTrama.getTipoTransaccion());
		objProcesoReversoDatafastDTO.setPlazoDiferido(objTrama.getPlazoDiferido());
		objProcesoReversoDatafastDTO.setMontoBaseConIva(objTrama.getMontoBaseConIva());
		objProcesoReversoDatafastDTO.setMontoBaseSinIva(objTrama.getMontoBaseSinIva());
		objProcesoReversoDatafastDTO.setSubtotal(objTrama.getSubtotal());
		objProcesoReversoDatafastDTO.setValorIva(objTrama.getValorIva());
		objProcesoReversoDatafastDTO.setSubtotalBaseImponible(objTrama.getSubtotalBaseImponible());
		objProcesoReversoDatafastDTO.setValorInteres(objTrama.getValorInteres());
		objProcesoReversoDatafastDTO.setValorTotal(objTrama.getValorTotal());
		return objProcesoReversoDatafastDTO;
	}

	public static void uploadFileSftp(Path file, String strFileName, String strRutaSftp, String strIpServer,
			Integer intPuerto, String strUser, String strPassword) throws BOException, IOException {
		FileInputStream fis = new FileInputStream(file.toFile());
		FTPClient client = new FTPClient();
		try {
			// client.setBufferSize(512);
			client.connect(strIpServer, intPuerto);
			client.login(strUser, strPassword);
			client.enterLocalPassiveMode();
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.changeWorkingDirectory(strRutaSftp);
			boolean uploadFile = client.storeFile(strFileName, fis);
			client.logout();
			client.disconnect();
			if (uploadFile == false)
				throw new BOException("pin.warn.subirArchivo");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new BOException(e.getMessage(), e.getCause());
		} finally {
			fis.close();
		}
	}

}
