/**
 * 
 */
package com.pinpad.ejb.bo;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.ParseException;

import javax.ejb.Local;

import com.pinpad.ejb.dto.InfoPinPadDTO;
import com.pinpad.ejb.dto.LecturaTarjetaDatafastDTO;
import com.pinpad.ejb.dto.ProcesoAnulacionPagoDatafastDTO;
import com.pinpad.ejb.dto.ProcesoConfiguracionDatafastDTO;
import com.pinpad.ejb.dto.ProcesoControlDatafastDTO;
import com.pinpad.ejb.dto.ProcesoPagoDatafastDTO;
import com.pinpad.ejb.dto.ProcesoReversoDatafastDTO;
import com.pinpad.ejb.exceptions.BOException;
import com.pinpad.ejb.exceptions.BOExceptionUpt;
import com.pinpad.ejb.model.FacLogTramaPinpad;

/**
 * @author H P
 *
 */
@Local
public interface IDatafastBO {

	/**
	 * Obtiene información del pinpad de la caja.
	 * 
	 * @author Frank Solis
	 * @param strIdCaja
	 * @return
	 * @throws BOException
	 */
	InfoPinPadDTO infoPinpad(String strIdCaja) throws BOException;

	/**
	 * Proceso de Lectura de Tarjeta Red Datafast.
	 * 
	 * @author Frank Solis
	 * @param strIdCaja
	 * @param strCodigoUsuario
	 * @param typeEnumDatabase
	 * @return
	 * @throws BOException
	 */
	LecturaTarjetaDatafastDTO lecturaTarjeta(String strIdCaja, String strCodigoUsuario) throws BOException;

	/**
	 * Proceso de Pago Red Datafast.
	 * 
	 * @author Frank Solis
	 * @param strIdCaja
	 * @param strCodigoUsuario
	 * @param strTipoTransaccion
	 * @param intCodigoRed
	 * @param strCodigoDiferido
	 * @param intPlazoDiferido
	 * @param intMesesGracia
	 * @param douMontoBaseConIva
	 * @param douMontoBaseSinIva
	 * @param douSubTotal
	 * @param douMontoIva
	 * @param douMontoTotal
	 * @return
	 * @throws BOException
	 */
	ProcesoPagoDatafastDTO procesoPago(String strIdCaja, String strCodigoUsuario, String strTipoTransaccion,
			Integer intCodigoRed, String strCodigoDiferido, Integer intPlazoDiferido, Integer intMesesGracia,
			Double douMontoBaseConIva, Double douMontoBaseSinIva, Double douSubTotal, Double douMontoIva,
			Double douMontoTotal) throws BOException, ParseException;

	/**
	 * Proceso de Anulación Red Datafast.
	 * 
	 * @author Frank Solis
	 * @param strIdCaja
	 * @param strCodigoUsuario
	 * @param strTipoTransaccion
	 * @param intCodigoRed
	 * @param bigSecuencia
	 * @return
	 * @throws BOException
	 */
	ProcesoAnulacionPagoDatafastDTO anulacionPago(String strIdCaja, String strTipoTransaccion, Integer intCodigoRed,
			BigDecimal bigSecuencia, String strCodUsuario) throws BOException;

	/**
	 * Proceso de Control Red Datafast.
	 * 
	 * @author Frank Solis
	 * @param strIdCaja
	 * @param strCodigoUsuario
	 * @param intCodigoRed
	 * @return
	 * @throws BOException
	 */
	ProcesoControlDatafastDTO procesoControl(String strIdCaja, Integer intCodigoRed, String strCodUsuario)
			throws BOException;

	/**
	 * Consulta de Log Datafast.
	 * 
	 * @author Frank Solis
	 * @param intSecuencia
	 * @return
	 * @throws BOException
	 */
	FacLogTramaPinpad obtenerLogDatafast(BigDecimal bigSecuencia) throws BOException;

	/**
	 * Proceso de Configuración de Pinpad Datafast.
	 * 
	 * @author Frank Solis
	 * @param strIdCaja
	 * @param strCodigoUsuario
	 * @return
	 * @throws BOException
	 */
	ProcesoConfiguracionDatafastDTO procesoConfiguracion(String strIdCaja, String strNuevaIp, String strMascara, String strGateway,
			String strPrincipalHost, String strPrincipalPuerto, String strAlternoHost, String strAlternoPuerto,
			String strPuertoEscucha, String strCodigoUsuario) throws BOException;
	
	/**
	 * Proceso de Reverso manual de Pago Red Datafast.
	 * 
	 * @author Frank Solis
	 * @param bigSecuencia
	 * @return ProcesoReversoDatafastDTO
	 * @throws BOException
	 */
	ProcesoReversoDatafastDTO procesoReversoManual(BigDecimal bigSecuencia) throws BOException;
	
	/**
	 * Proceso para generación de archivo de captura con transacciones aprobadas Red Datafast.
	 * 
	 * @author Frank Solis
	 * @param strFechaInicio
	 * @param strFechaFin
	 * @return file
	 * @throws BOException
	 */
	Path obtenerArchivoCaptura(String strFechaInicio, String strFechaFin) throws BOException, ParseException;
	
	/**
	 * Proceso para generación de archivo de captura con transacciones aprobadas Red Datafast.
	 * 
	 * @author Frank Solis
	 * @param strFechaInicio
	 * @param strFechaFin
	 * @return file
	 * @throws BOException
	 */
	Path obtenerArchivoCapturaPreviaCarga(String strFechaInicio, String strFechaFin) throws BOExceptionUpt, ParseException;
	
	/**
	 * Proceso para cargar archivo de captura a server SFTP.
	 * 
	 * @author Frank Solis
	 * @param file
	 * @return String
	 * @throws BOException
	 */
	String cargaArchivoSftp (Path file) throws BOExceptionUpt;
	
	/**
	 * Proceso de Reverso automático de Pago Red Datafast.
	 * 
	 * @author Frank Solis
	 * @param strIdCaja
	 * @param strCodigoUsuario
	 * @param strTipoTransaccion
	 * @param intCodigoRed
	 * @param strCodigoDiferido
	 * @param intPlazoDiferido
	 * @param intMesesGracia
	 * @param douMontoBaseConIva
	 * @param douMontoBaseSinIva
	 * @param douSubTotal
	 * @param douMontoIva
	 * @param douMontoTotal
	 * @return ProcesoReversoDatafastDTO
	 * @throws BOException
	 */
//	ProcesoReversoDatafastDTO procesoReversoAutomático(String strIdCaja, String strCodigoUsuario, String strTipoTransaccion,
//			Integer intCodigoRed, String strCodigoDiferido, Integer intPlazoDiferido, Integer intMesesGracia,
//			Double douMontoBaseConIva, Double douMontoBaseSinIva, Double douSubTotal, Double douMontoIva,
//			Double douMontoTotal, Date fecha, Date hora) throws BOException;
}
