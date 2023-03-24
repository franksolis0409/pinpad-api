/**
 * 
 */
package com.pinpad.ejb.dao.impl;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.pinpad.ejb.model.GrlParametrosPinpad;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class GrlParametrosPinpadDAOImpl extends BaseDAO<GrlParametrosPinpad, String>{
	
	protected GrlParametrosPinpadDAOImpl() {
		super(GrlParametrosPinpad.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(GrlParametrosPinpad t) throws PersistenceException {
		// TODO Auto-generated method stub
		super.persist(t);
	}

	@Override
	public void update(GrlParametrosPinpad t) throws PersistenceException {
		// TODO Auto-generated method stub
		super.update(t);
	}

	@Override
	public Optional<GrlParametrosPinpad> find(@NonNull String id) {
		// TODO Auto-generated method stub
		return super.find(id);
	}
}
