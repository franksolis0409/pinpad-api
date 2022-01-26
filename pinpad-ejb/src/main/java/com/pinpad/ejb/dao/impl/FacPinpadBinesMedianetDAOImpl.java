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

import com.pinpad.ejb.model.FacPinpadBinesMedianet;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class FacPinpadBinesMedianetDAOImpl extends BaseDAO<FacPinpadBinesMedianet, BigDecimal> {

	protected FacPinpadBinesMedianetDAOImpl() {
		super(FacPinpadBinesMedianet.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(FacPinpadBinesMedianet t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(FacPinpadBinesMedianet t) throws PersistenceException {

		super.update(t);
	}

	@Override
	public Optional<FacPinpadBinesMedianet> find(@NonNull BigDecimal id) {

		return super.find(id);
	}
}
