/**
 * 
 */
package com.pinpad.ejb.dao.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.ObjectUtils;

import com.pinpad.ejb.dto.DetalleArchivoCapturaDTO;
import com.pinpad.ejb.model.FacLogTramaPinpad;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class FacLogTramaPinpadDAOImpl extends BaseDAO<FacLogTramaPinpad, Long> {

	protected FacLogTramaPinpadDAOImpl() {
		super(FacLogTramaPinpad.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(FacLogTramaPinpad t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(FacLogTramaPinpad t) throws PersistenceException {

		super.update(t);
	}

	@Override
	public Optional<FacLogTramaPinpad> find(@NonNull Long id) {

		return super.find(id);
	}
	
	public String obtenerMaxLote() {
		try {
			return em.createQuery("SELECT nvl(max(a.lote),'0') FROM FacLogTramaPinpad a ", String.class)
					.getSingleResult();
		} catch (NoResultException e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public BigDecimal obtenerSecuenciaLote() {
		StringBuilder strSQL = new StringBuilder();
		try {
			strSQL.append("SELECT latino_owner.FAC_SEQ_LOTE_PINPAD.nextval FROM dual ");
			Query query = em.createNativeQuery(strSQL.toString());
			return new BigDecimal(query.getSingleResult().toString());
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<DetalleArchivoCapturaDTO> findTransaccionesApr(String strFechaInicio, String strFechaFin) {
		//List<Tuple> objResult = null;
		StringBuilder strJPQLBase = new StringBuilder();
		strJPQLBase.append("select lt.tid, ");
		strJPQLBase.append(" 	   substr(lt.fecha,3) as fecha, ");
		strJPQLBase.append("	   lt.hora, ");
		strJPQLBase.append("	   lt.secuencia_transaccion, ");
		strJPQLBase.append("	   lt.numero_autorizacion, ");
		strJPQLBase.append("	   lt.lote, ");
		strJPQLBase.append("	   lt.valor_total, ");//format
		strJPQLBase.append("	   nvl(lt.codigo_diferido, '00') as tipo_credito, ");
		strJPQLBase.append("	   nvl(lt.plazo_diferido, '00') as plazo_diferido, ");
		strJPQLBase.append("	   lt.valor_iva, ");//format
		strJPQLBase.append("	   '0000000000000' as valor_servicio, ");
		strJPQLBase.append("	   '0000000000000' as valor_propina, ");
		strJPQLBase.append("	   lt.valor_interes, ");//format
		strJPQLBase.append("	   '0000000000000' as monto_fijo, ");
		strJPQLBase.append("	   '0000000000000' as valor_ice, ");
		strJPQLBase.append("	   '0000000000000' as otros_impuestos, ");
		strJPQLBase.append("	   '0000000000000' as cash_over,");
		strJPQLBase.append("	   lt.monto_base_sin_iva, ");//format
		strJPQLBase.append("	   lt.monto_base_con_iva ");//format
		strJPQLBase.append("       		from fac_log_trama_pinpad lt ");
		strJPQLBase.append("       		where lt.tipo_mensaje = 'PP' ");
		strJPQLBase.append("         	  and lt.codigo_respuesta = '00' ");
		//strJPQLBase.append("         	  and lt.codigo_red = '01' ");
		strJPQLBase.append("        	  and upper(lt.respuesta_pinpad) like :respuesta ");
		strJPQLBase.append("         	  and lt.cod_tipo_transaccion in ('01','02') ");
		strJPQLBase.append("              and lt.secuencia_Transaccion not in (");
		strJPQLBase.append("        									select lt2.cod_Transaccion_Original from fac_log_trama_pinpad lt2");
		strJPQLBase.append("        										where lt2.lote = lt.lote ");
		strJPQLBase.append("        										      and lt2.cod_tipo_Transaccion in ('03','04') ");
		strJPQLBase.append("        											  and lt2.codigo_Respuesta = '00'");
		strJPQLBase.append("        											  and upper(lt2.respuesta_PinPad) like :respuesta ");
		strJPQLBase.append("        									)");
		if (!ObjectUtils.isEmpty(strFechaInicio) || !ObjectUtils.isEmpty(strFechaFin))
			strJPQLBase.append("     and trunc(lt.fecha_ingreso) between to_date(:fechaInicio, 'dd/mm/yyyy') and to_date(:fechaFin, 'dd/mm/yyyy') ");
		else
			strJPQLBase.append("     and trunc(lt.fecha_ingreso) = trunc(sysdate) ");
		Query query = em.createNativeQuery(strJPQLBase.toString());
		query.setParameter("respuesta", "%APROBADA TRANS.%");
		if (!ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)) {
			query.setParameter("fechaInicio", strFechaInicio);
			query.setParameter("fechaFin", strFechaFin);
		} else if (!ObjectUtils.isEmpty(strFechaInicio) && ObjectUtils.isEmpty(strFechaFin)){
			query.setParameter("fechaInicio", strFechaInicio);
			query.setParameter("fechaFin", strFechaInicio);
		} else if (ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)){
			query.setParameter("fechaInicio", strFechaFin);
			query.setParameter("fechaFin", strFechaFin);
		}
		List<Object[]> lsResult = query.getResultList();
		List<DetalleArchivoCapturaDTO> lsDetalleArchivoCapturaDTO = new ArrayList<DetalleArchivoCapturaDTO>();
		for (Object[] objects : lsResult) {
			DetalleArchivoCapturaDTO objDetalleArchivoCapturaDTO = new DetalleArchivoCapturaDTO();
			objDetalleArchivoCapturaDTO.setTid(!ObjectUtils.isEmpty(objects[0]) ? objects[0].toString() : null);
			objDetalleArchivoCapturaDTO.setFecha(!ObjectUtils.isEmpty(objects[1]) ? objects[1].toString() : null);
			objDetalleArchivoCapturaDTO.setHora(!ObjectUtils.isEmpty(objects[2]) ? objects[2].toString() : null);
			objDetalleArchivoCapturaDTO.setSecuenciaTransaccion(!ObjectUtils.isEmpty(objects[3]) ? objects[3].toString() : null);
			objDetalleArchivoCapturaDTO.setNumeroAutorizacion(!ObjectUtils.isEmpty(objects[4]) ? objects[4].toString() : null);
			objDetalleArchivoCapturaDTO.setLote(!ObjectUtils.isEmpty(objects[5]) ? objects[5].toString() : null);
			objDetalleArchivoCapturaDTO.setValorTotal(!ObjectUtils.isEmpty(objects[6]) ? Double.parseDouble(objects[6].toString()) : null);
			objDetalleArchivoCapturaDTO.setTipoCredito(!ObjectUtils.isEmpty(objects[7]) ? objects[7].toString() : null);
			objDetalleArchivoCapturaDTO.setPlazoDiferido(!ObjectUtils.isEmpty(objects[8]) ? Integer.valueOf(objects[8].toString()) : null);
			objDetalleArchivoCapturaDTO.setValorIva(!ObjectUtils.isEmpty(objects[9]) ? Double.parseDouble(objects[9].toString()) : null);
			objDetalleArchivoCapturaDTO.setValorServicio(!ObjectUtils.isEmpty(objects[10]) ? objects[10].toString() : null);
			objDetalleArchivoCapturaDTO.setValorPropina(!ObjectUtils.isEmpty(objects[11]) ? objects[11].toString() : null);
			objDetalleArchivoCapturaDTO.setValorInteres(!ObjectUtils.isEmpty(objects[12]) ? Double.parseDouble(objects[12].toString()) : null);
			objDetalleArchivoCapturaDTO.setMontoFijo(!ObjectUtils.isEmpty(objects[13]) ? objects[13].toString() : null);
			objDetalleArchivoCapturaDTO.setValorIce(!ObjectUtils.isEmpty(objects[14]) ? objects[14].toString() : null);
			objDetalleArchivoCapturaDTO.setOtrosImpuestos(!ObjectUtils.isEmpty(objects[15]) ? objects[15].toString() : null);
			objDetalleArchivoCapturaDTO.setCashOver(!ObjectUtils.isEmpty(objects[16]) ? objects[16].toString() : null);
			objDetalleArchivoCapturaDTO.setMontoBaseSinIva(!ObjectUtils.isEmpty(objects[17]) ? Double.parseDouble(objects[17].toString()) : null);
			objDetalleArchivoCapturaDTO.setMontoBaseConIva(!ObjectUtils.isEmpty(objects[18]) ? Double.parseDouble(objects[18].toString()) : null);
			lsDetalleArchivoCapturaDTO.add(objDetalleArchivoCapturaDTO);
		}
		return lsDetalleArchivoCapturaDTO;
	}
	
	public Long countTransaccionesAprobadas(String strFechaInicio, String strFechaFin) throws ParseException {	
		Date fechaInicio = null;
		Date fechaFin = null;
		StringBuilder strJPQLBase = new StringBuilder();
		TypedQuery<Long> query = null;
		try {
			strJPQLBase.append("select count(1) ");
			strJPQLBase.append("      from FacLogTramaPinpad lt ");
			strJPQLBase.append("      where lt.tipoMensaje = 'PP' ");
			strJPQLBase.append("        and lt.codigoRespuesta = '00' ");
			//strJPQLBase.append("        and lt.codigoRed = '01' ");
			strJPQLBase.append("        and upper(lt.respuestaPinPad) like :respuesta ");
			strJPQLBase.append("        and (lt.tipoTransaccion = '01' or lt.tipoTransaccion = '02') ");
			strJPQLBase.append("        and lt.codTransaccionOriginal is null ");
			strJPQLBase.append("        and lt.secuenciaTransaccion not in (");
			strJPQLBase.append("        									select lt2.codTransaccionOriginal from FacLogTramaPinpad lt2");
			strJPQLBase.append("        										where lt2.lote = lt.lote ");
			strJPQLBase.append("        										      and lt2.tipoTransaccion in ('03','04') ");
			strJPQLBase.append("        											  and lt2.codigoRespuesta = '00'");
			strJPQLBase.append("        											  and upper(lt2.respuestaPinPad) like :respuesta ");
			strJPQLBase.append("        									)");
			if (!ObjectUtils.isEmpty(strFechaInicio) || !ObjectUtils.isEmpty(strFechaFin))
				strJPQLBase.append("     and trunc(lt.fechaIngreso) between trunc(:fechaInicio) and trunc(:fechaFin) ");
			else
				strJPQLBase.append("     and trunc(lt.fechaIngreso) = trunc(:fechaActual) ");
			query = em.createQuery(strJPQLBase.toString(), Long.class);
			query.setParameter("respuesta", "%APROBADA TRANS.%");
			if (!ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)) {
				fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaInicio);
				fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaFin);
				query.setParameter("fechaInicio", fechaInicio);
				query.setParameter("fechaFin", fechaFin);
			} else if (!ObjectUtils.isEmpty(strFechaInicio) && ObjectUtils.isEmpty(strFechaFin)){
				fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaInicio);
				query.setParameter("fechaInicio", fechaInicio);
				query.setParameter("fechaFin", fechaInicio);
			} else if (ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)){
				fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaFin);
				query.setParameter("fechaInicio", fechaFin);
				query.setParameter("fechaFin", fechaFin);
			} else
				query.setParameter("fechaActual", new Date());
			return query.getSingleResult().longValue();
		} catch (NoResultException e) {
			// TODO: handle exception
			return null;
		}	 
	}
	
	@SuppressWarnings("unchecked")
	public List<String> obtenerMidAprobados(String strFechaInicio, String strFechaFin) throws ParseException {	
		Date fechaInicio = null;
		Date fechaFin = null;
		StringBuilder strJPQLBase = new StringBuilder();
		try {
			strJPQLBase.append("select distinct lt.mid ");
			strJPQLBase.append("      from FacLogTramaPinpad lt ");
			strJPQLBase.append("      where lt.tipoMensaje = 'PP' ");
			strJPQLBase.append("        and lt.codigoRespuesta = '00' ");
			//strJPQLBase.append("        and lt.codigoRed = '01' ");
			strJPQLBase.append("        and upper(lt.respuestaPinPad) like :respuesta ");
			strJPQLBase.append("        and (lt.tipoTransaccion = '01' or lt.tipoTransaccion = '02') ");
			strJPQLBase.append("        and lt.codTransaccionOriginal is null ");
			strJPQLBase.append("        and lt.secuenciaTransaccion not in (");
			strJPQLBase.append("        									select lt2.codTransaccionOriginal from FacLogTramaPinpad lt2");
			strJPQLBase.append("        										where lt2.lote = lt.lote ");
			strJPQLBase.append("        										      and lt2.tid = lt.tid ");
			strJPQLBase.append("        										      and lt2.tipoTransaccion in ('03','04') ");
			strJPQLBase.append("        											  and lt2.codigoRespuesta = '00'");
			strJPQLBase.append("        											  and upper(lt2.respuestaPinPad) like :respuesta ");
			strJPQLBase.append("        									)");
			if (!ObjectUtils.isEmpty(strFechaInicio) || !ObjectUtils.isEmpty(strFechaFin))
				strJPQLBase.append("     and trunc(lt.fechaIngreso) between trunc(:fechaInicio) and trunc(:fechaFin) ");
			else
				strJPQLBase.append("     and trunc(lt.fechaIngreso) = trunc(:fechaActual) ");
			Query query = em.createQuery(strJPQLBase.toString());
			query.setParameter("respuesta", "%APROBADA TRANS.%");
			if (!ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)) {
				fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaInicio);
				fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaFin);
				query.setParameter("fechaInicio", fechaInicio);
				query.setParameter("fechaFin", fechaFin);
			} else if (!ObjectUtils.isEmpty(strFechaInicio) && ObjectUtils.isEmpty(strFechaFin)){
				fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaInicio);
				query.setParameter("fechaInicio", fechaInicio);
				query.setParameter("fechaFin", fechaInicio);
			} else if (ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)){
				fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaFin);
				query.setParameter("fechaInicio", fechaFin);
				query.setParameter("fechaFin", fechaFin);
			} else
				query.setParameter("fechaActual", new Date());
			List<String> lsResult = query.getResultList();
			List<String> lsMidAprobados = new ArrayList<String>();
			lsMidAprobados.addAll(lsResult);
//			for (String objects : lsResult) {
//				lsMidAprobados.add(!ObjectUtils.isEmpty(objects[0]) ? objects[0].toString() : null);
//			}
			return lsMidAprobados;
		} catch (NoResultException e) {
			// TODO: handle exception
			return null;
		}	 
	}
	
	@SuppressWarnings("unchecked")
	public List<DetalleArchivoCapturaDTO> findTransaccionesAprobadasMid(String strMid, String strFechaInicio, String strFechaFin) {
		StringBuilder strJPQLBase = new StringBuilder();
		strJPQLBase.append("select lt.tid, ");
		strJPQLBase.append(" 	   substr(lt.fecha,3) as fecha, ");
		strJPQLBase.append("	   lt.hora, ");
		strJPQLBase.append("	   lt.secuencia_transaccion, ");
		strJPQLBase.append("	   lt.numero_autorizacion, ");
		strJPQLBase.append("	   lt.lote, ");
		strJPQLBase.append("	   lt.valor_total, ");
		strJPQLBase.append("	   nvl(lt.codigo_diferido, '00') as tipo_credito, ");
		strJPQLBase.append("	   nvl(lt.plazo_diferido, '00') as plazo_diferido, ");
		strJPQLBase.append("	   lt.valor_iva, ");
		strJPQLBase.append("	   '0000000000000' as valor_servicio, ");
		strJPQLBase.append("	   '0000000000000' as valor_propina, ");
		strJPQLBase.append("	   lt.valor_interes, ");
		strJPQLBase.append("	   '0000000000000' as monto_fijo, ");
		strJPQLBase.append("	   '0000000000000' as valor_ice, ");
		strJPQLBase.append("	   '0000000000000' as otros_impuestos, ");
		strJPQLBase.append("	   '0000000000000' as cash_over,");
		strJPQLBase.append("	   lt.monto_base_sin_iva, ");
		strJPQLBase.append("	   lt.monto_base_con_iva, ");
		strJPQLBase.append("	   lt.secuencia ");
		strJPQLBase.append("       		from fac_log_trama_pinpad lt ");
		strJPQLBase.append("       		where lt.tipo_mensaje = 'PP' ");
		strJPQLBase.append("         	  and lt.codigo_respuesta = '00' ");
		//strJPQLBase.append("         	  and lt.codigo_red = '01' ");
		strJPQLBase.append("        	  and upper(lt.respuesta_pinpad) like :respuesta ");
		strJPQLBase.append("        	  and lt.mid = :mid ");
		strJPQLBase.append("         	  and lt.cod_tipo_transaccion in ('01','02') ");
		strJPQLBase.append("        	  and lt.cod_Transaccion_Original is null ");
		strJPQLBase.append("              and lt.secuencia_Transaccion not in (");
		strJPQLBase.append("        									select lt2.cod_Transaccion_Original from fac_log_trama_pinpad lt2");
		strJPQLBase.append("        										where lt2.lote = lt.lote ");
		strJPQLBase.append("        										      and lt2.tid = lt.tid ");
		strJPQLBase.append("        										      and lt2.cod_tipo_Transaccion in ('03','04') ");
		strJPQLBase.append("        											  and lt2.codigo_Respuesta = '00'");
		strJPQLBase.append("        											  and upper(lt2.respuesta_PinPad) like :respuesta ");
		strJPQLBase.append("        									)");
		if (!ObjectUtils.isEmpty(strFechaInicio) || !ObjectUtils.isEmpty(strFechaFin))
			strJPQLBase.append("     and trunc(lt.fecha_ingreso) between to_date(:fechaInicio, 'dd/mm/yyyy') and to_date(:fechaFin, 'dd/mm/yyyy') order by lt.lote asc ");
		else
			strJPQLBase.append("     and trunc(lt.fecha_ingreso) = trunc(sysdate) order by lt.lote asc ");
		Query query = em.createNativeQuery(strJPQLBase.toString());
		query.setParameter("respuesta", "%APROBADA TRANS.%");
		query.setParameter("mid", strMid);
		if (!ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)) {
			query.setParameter("fechaInicio", strFechaInicio);
			query.setParameter("fechaFin", strFechaFin);
		} else if (!ObjectUtils.isEmpty(strFechaInicio) && ObjectUtils.isEmpty(strFechaFin)){
			query.setParameter("fechaInicio", strFechaInicio);
			query.setParameter("fechaFin", strFechaInicio);
		} else if (ObjectUtils.isEmpty(strFechaInicio) && !ObjectUtils.isEmpty(strFechaFin)){
			query.setParameter("fechaInicio", strFechaFin);
			query.setParameter("fechaFin", strFechaFin);
		}
		List<Object[]> lsResult = query.getResultList();
		List<DetalleArchivoCapturaDTO> lsDetalleArchivoCapturaDTO = new ArrayList<DetalleArchivoCapturaDTO>();
		for (Object[] objects : lsResult) {
			DetalleArchivoCapturaDTO objDetalleArchivoCapturaDTO = new DetalleArchivoCapturaDTO();
			objDetalleArchivoCapturaDTO.setTid(!ObjectUtils.isEmpty(objects[0]) ? objects[0].toString() : null);
			objDetalleArchivoCapturaDTO.setFecha(!ObjectUtils.isEmpty(objects[1]) ? objects[1].toString() : null);
			objDetalleArchivoCapturaDTO.setHora(!ObjectUtils.isEmpty(objects[2]) ? objects[2].toString() : null);
			objDetalleArchivoCapturaDTO.setSecuenciaTransaccion(!ObjectUtils.isEmpty(objects[3]) ? objects[3].toString() : null);
			objDetalleArchivoCapturaDTO.setNumeroAutorizacion(!ObjectUtils.isEmpty(objects[4]) ? objects[4].toString() : null);
			objDetalleArchivoCapturaDTO.setLote(!ObjectUtils.isEmpty(objects[5]) ? objects[5].toString() : null);
			objDetalleArchivoCapturaDTO.setValorTotal(!ObjectUtils.isEmpty(objects[6]) ? Double.parseDouble(objects[6].toString()) : null);
			objDetalleArchivoCapturaDTO.setTipoCredito(!ObjectUtils.isEmpty(objects[7]) ? objects[7].toString() : null);
			objDetalleArchivoCapturaDTO.setPlazoDiferido(!ObjectUtils.isEmpty(objects[8]) ? Integer.valueOf(objects[8].toString()) : null);
			objDetalleArchivoCapturaDTO.setValorIva(!ObjectUtils.isEmpty(objects[9]) ? Double.parseDouble(objects[9].toString()) : null);
			objDetalleArchivoCapturaDTO.setValorServicio(!ObjectUtils.isEmpty(objects[10]) ? objects[10].toString() : null);
			objDetalleArchivoCapturaDTO.setValorPropina(!ObjectUtils.isEmpty(objects[11]) ? objects[11].toString() : null);
			objDetalleArchivoCapturaDTO.setValorInteres(!ObjectUtils.isEmpty(objects[12]) ? Double.parseDouble(objects[12].toString()) : null);
			objDetalleArchivoCapturaDTO.setMontoFijo(!ObjectUtils.isEmpty(objects[13]) ? objects[13].toString() : null);
			objDetalleArchivoCapturaDTO.setValorIce(!ObjectUtils.isEmpty(objects[14]) ? objects[14].toString() : null);
			objDetalleArchivoCapturaDTO.setOtrosImpuestos(!ObjectUtils.isEmpty(objects[15]) ? objects[15].toString() : null);
			objDetalleArchivoCapturaDTO.setCashOver(!ObjectUtils.isEmpty(objects[16]) ? objects[16].toString() : null);
			objDetalleArchivoCapturaDTO.setMontoBaseSinIva(!ObjectUtils.isEmpty(objects[17]) ? Double.parseDouble(objects[17].toString()) : null);
			objDetalleArchivoCapturaDTO.setMontoBaseConIva(!ObjectUtils.isEmpty(objects[18]) ? Double.parseDouble(objects[18].toString()) : null);
			objDetalleArchivoCapturaDTO.setSecuencia(ObjectUtils.isNotEmpty(objects[19]) ? new Long(objects[19].toString()) : null);
			lsDetalleArchivoCapturaDTO.add(objDetalleArchivoCapturaDTO);
		}
		return lsDetalleArchivoCapturaDTO;
	}
}
