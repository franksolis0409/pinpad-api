/**
 * 
 */
package com.pinpad.ejb.dao.impl;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.pinpad.ejb.model.DafXParametrosXEmpresa;
import com.pinpad.ejb.model.DafXParametrosXEmpresaCPK;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class DafXParametrosXEmpresaDAOImpl extends BaseDAO<DafXParametrosXEmpresa,DafXParametrosXEmpresaCPK>{
	
	protected DafXParametrosXEmpresaDAOImpl() {
		super(DafXParametrosXEmpresa.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(DafXParametrosXEmpresa t) throws PersistenceException {
		// TODO Auto-generated method stub
		super.persist(t);
	}

	@Override
	public void update(DafXParametrosXEmpresa t) throws PersistenceException {
		// TODO Auto-generated method stub
		super.update(t);
	}

	@Override
	public Optional<DafXParametrosXEmpresa> find(@NonNull DafXParametrosXEmpresaCPK id) {
		// TODO Auto-generated method stub
		return super.find(id);
	}
}
