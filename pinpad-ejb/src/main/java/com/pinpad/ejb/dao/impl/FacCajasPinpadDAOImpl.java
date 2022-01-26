/**
 * 
 */
package com.pinpad.ejb.dao.impl;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.pinpad.ejb.model.FacCajasPinpad;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class FacCajasPinpadDAOImpl extends BaseDAO<FacCajasPinpad, Long> {

	protected FacCajasPinpadDAOImpl() {
		super(FacCajasPinpad.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(FacCajasPinpad t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(FacCajasPinpad t) throws PersistenceException {

		super.update(t);
	}

	@Override
	public Optional<FacCajasPinpad> find(@NonNull Long id) {

		return super.find(id);
	}

	public FacCajasPinpad findByIdentificadorCaja(String strIdCaja) {
		try {
			return (em.createQuery(
					"SELECT a FROM FacCajasPinpad a WHERE a.esActivo = 'S' AND a.identificadorCaja = :idCaja ",
					FacCajasPinpad.class).setParameter("idCaja", strIdCaja).getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}
}
