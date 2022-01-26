/**
 * 
 */
package com.pinpad.ejb.dao.impl;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.pinpad.ejb.model.FacParametrosGenerales;
import com.pinpad.ejb.model.FacParametrosGeneralesCPK;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class FacParametrosGeneralesDAOImpl extends BaseDAO<FacParametrosGenerales, FacParametrosGeneralesCPK> {

	protected FacParametrosGeneralesDAOImpl() {
		super(FacParametrosGenerales.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(FacParametrosGenerales t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(FacParametrosGenerales t) throws PersistenceException {

		super.update(t);
	}

	@Override
	public Optional<FacParametrosGenerales> find(@NonNull FacParametrosGeneralesCPK id) {

		return super.find(id);
	}
}
