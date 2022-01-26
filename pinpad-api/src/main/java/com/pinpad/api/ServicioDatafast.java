/**
 * 
 */
package com.pinpad.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.pinpad.dto.ResponseOk;
import com.pinpad.ejb.dto.InfoPinPadDTO;
import com.pinpad.ejb.dto.LecturaTarjetaDatafastDTO;
import com.pinpad.ejb.dto.ProcesoAnulacionPagoDatafastDTO;
import com.pinpad.ejb.dto.ProcesoConfiguracionDatafastDTO;
import com.pinpad.ejb.dto.ProcesoControlDatafastDTO;
import com.pinpad.ejb.dto.ProcesoPagoDatafastDTO;
import com.pinpad.ejb.dto.ProcesoReversoDatafastDTO;
import com.pinpad.ejb.enums.TipoArchivoEnum;
import com.pinpad.ejb.exceptions.BOException;
import com.pinpad.ejb.model.FacLogTramaPinpad;
import com.pinpad.ejb.util.MensajesUtil;
import com.pinpad.exceptions.CustomExceptionHandler;
import com.pinpad.security.Secure;
import com.pinpad.util.ServiciosEjb;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author H P
 *
 */
@Path("/v1/pagosDatafast")
public class ServicioDatafast {

	private static final Logger logger = Logger.getLogger(ServicioDatafast.class.getName());

	@GET
	@Path("/infoPinpad")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response getInfoPinPad(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argIdCaja") String strIdCaja) {
		try {
			InfoPinPadDTO objInfoPinPadDTO = ServiciosEjb.getDatafastEjb().infoPinpad(strIdCaja);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objInfoPinPadDTO)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@GET
	@Path("/lecturaTarjeta")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response lecturaTarjeta(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argIdCaja") String strIdCaja, @QueryParam("argCodigoUsuario") String strCodigoUsuario) {
		try {
			LecturaTarjetaDatafastDTO objLecturaTarjetaDatafastDTO = ServiciosEjb.getDatafastEjb()
					.lecturaTarjeta(strIdCaja, strCodigoUsuario);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objLecturaTarjetaDatafastDTO)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@POST
	@Path("/procesoPago")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response procesoPago(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argIdCaja") String strIdCaja, @QueryParam("argCodigoUsuario") String strCodigoUsuario,
			@QueryParam("argTipoTransaccion") String strTipoTransaccion,
			@QueryParam("argCodigoRed") Integer intCodigoRed, @QueryParam("argCodigoDiferido") String strCodigoDiferido,
			@QueryParam("argPlazoDiferido") Integer intPlazoDiferido,
			@QueryParam("argMesesGracia") Integer intMesesGracia,
			@QueryParam("argMontoBaseConIva") Double douMontoBaseConIva,
			@QueryParam("argMontoBaseSinIva") Double douMontoBaseSinIva, @QueryParam("argSubTotal") Double douSubTotal,
			@QueryParam("argMontoIva") Double douMontoIva, @QueryParam("argMontoTotal") Double douMontoTotal) {
		try {
			ProcesoPagoDatafastDTO objProcesoPagoDatafastDTO = ServiciosEjb.getDatafastEjb().procesoPago(strIdCaja,
					strCodigoUsuario, strTipoTransaccion, intCodigoRed, strCodigoDiferido, intPlazoDiferido,
					intMesesGracia, douMontoBaseConIva, douMontoBaseSinIva, douSubTotal, douMontoIva, douMontoTotal);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objProcesoPagoDatafastDTO)).build();
		} catch (BOException | ParseException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(((BOException) e).getTranslatedMessage(strLanguage), ((BOException) e).getData());
		}
	}

	@POST
	@Path("/anulacionPago")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response anulacionPago(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argIdCaja") String strIdCaja, @QueryParam("argTipoTransaccion") String strTipoTransaccion,
			@QueryParam("argCodigoRed") Integer intCodigoRed, @QueryParam("argSecuencia") BigDecimal bigSecuencia,
			@QueryParam("argCodigoUsuario") String strCodigoUsuario) {
		try {
			ProcesoAnulacionPagoDatafastDTO objProcesoPagoDatafastDTO = ServiciosEjb.getDatafastEjb()
					.anulacionPago(strIdCaja, strTipoTransaccion, intCodigoRed, bigSecuencia, strCodigoUsuario);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objProcesoPagoDatafastDTO)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@POST
	@Path("/procesoControl")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response procesoControl(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argIdCaja") String strIdCaja, @QueryParam("argCodigored") Integer intCodigoRed,
			@QueryParam("argCodigoUsuario") String strCodigoUsuario) {
		try {
			ProcesoControlDatafastDTO objProcesoControlDatafastDTO = ServiciosEjb.getDatafastEjb()
					.procesoControl(strIdCaja, intCodigoRed, strCodigoUsuario);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objProcesoControlDatafastDTO)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@GET
	@Path("/obtenerLogDatafast")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response obtenerLogDatafast(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argSecuencia") BigDecimal bigSecuencia) {
		try {
			FacLogTramaPinpad objFacLogTramaPinpad = ServiciosEjb.getDatafastEjb().obtenerLogDatafast(bigSecuencia);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objFacLogTramaPinpad)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@POST
	@Path("/procesoConfiguracion")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response procesoConfiguracion(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argIdCaja") String strIdCaja, @QueryParam("argNuevaIp") String strNuevaIp,
			@QueryParam("argMascara") String strMascara, @QueryParam("argGateway") String strGateway,
			@QueryParam("argPrincipalHost") String strPrincipalHost,
			@QueryParam("argPrincipalPuerto") String strPrincipalPuerto,
			@QueryParam("argAlternoHost") String strAlternoHost,
			@QueryParam("argAlternoPuerto") String strAlternoPuerto,
			@QueryParam("argPuertoEscucha") String strPuertoEscucha,
			@QueryParam("argCodigoUsuario") String strCodigoUsuario) {
		try {
			ProcesoConfiguracionDatafastDTO objProcesoControlDatafastDTO = ServiciosEjb.getDatafastEjb()
					.procesoConfiguracion(strIdCaja, strNuevaIp, strMascara, strGateway, strPrincipalHost,
							strPrincipalPuerto, strAlternoHost, strAlternoPuerto, strPuertoEscucha, strCodigoUsuario);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objProcesoControlDatafastDTO)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@POST
	@Path("/procesoReverso")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response procesoReverso(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argSecuencia") BigDecimal bigSecuencia) {
		try {
			ProcesoReversoDatafastDTO objProcesoPagoDatafastDTO = ServiciosEjb.getDatafastEjb()
					.procesoReversoManual(bigSecuencia);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objProcesoPagoDatafastDTO)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@GET
	@Path("/obtenerArchivoCaptura")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response obtenerArchivoCaptura(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argFechaInicio") String strFechaInicio, @QueryParam("argFechaFin") String strFechaFin)
			throws ParseException {
		try {
			String strFileName = new SimpleDateFormat("MMddyyyy").format(new Date()) + "."
					+ TipoArchivoEnum.VER.getExtension();
			java.nio.file.Path file = ServiciosEjb.getDatafastEjb().obtenerArchivoCaptura(strFechaInicio, strFechaFin);
			return new ResponseOk().responseOkTempFile(file, strFileName, TipoArchivoEnum.VER.getContentType());
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@POST
	@Path("/subirArchivoCaptura")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure
	public Response subirArchivoCaptura(@HeaderParam("Accept-Language") String strLanguage,
			@QueryParam("argFechaInicio") String strFechaInicio, @QueryParam("argFechaFin") String strFechaFin)
			throws ParseException {
		try {
			java.nio.file.Path file = ServiciosEjb.getDatafastEjb().obtenerArchivoCaptura(strFechaInicio, strFechaFin);
//			String strFileName = ServiciosEjb.getDatafastEjb().cargaArchivoSftp(file);
//			return new ResponseOk().responseOkTempFile(file, strFileName, TipoArchivoEnum.VER.getContentType());
			ServiciosEjb.getDatafastEjb().cargaArchivoSftp(file);
			return Response.status(Status.OK).entity(new ResponseOk(
					MensajesUtil.getMensaje("pin.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					null)).build();
		} catch (BOException e) {
			logger.log(Level.SEVERE, "Exception: " + e);
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

}
