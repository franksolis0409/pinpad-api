/**
 * 
 */
package com.pinpad.ejb.dao.impl;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.pinpad.ejb.model.FacPinpadBinTarjDatafast;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class FacPinpadBinTarjDatafastDAOImpl extends BaseDAO<FacPinpadBinTarjDatafast, BigDecimal> {

	protected FacPinpadBinTarjDatafastDAOImpl() {
		super(FacPinpadBinTarjDatafast.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(FacPinpadBinTarjDatafast t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(FacPinpadBinTarjDatafast t) throws PersistenceException {

		super.update(t);
	}

	@Override
	public Optional<FacPinpadBinTarjDatafast> find(@NonNull BigDecimal id) {

		return super.find(id);
	}
}
