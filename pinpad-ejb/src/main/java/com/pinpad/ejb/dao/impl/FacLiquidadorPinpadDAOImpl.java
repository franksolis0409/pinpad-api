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

import com.pinpad.ejb.model.FacLiquidadorPinpad;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class FacLiquidadorPinpadDAOImpl extends BaseDAO<FacLiquidadorPinpad, Long> {

	protected FacLiquidadorPinpadDAOImpl() {
		super(FacLiquidadorPinpad.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(FacLiquidadorPinpad t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(FacLiquidadorPinpad t) throws PersistenceException {

		super.update(t);
	}

	@Override
	public Optional<FacLiquidadorPinpad> find(@NonNull Long id) {

		return super.find(id);
	}

	public FacLiquidadorPinpad findByCodigAdquirente(String strCodigoAdquirente) {
		try {
			return (em.createQuery(
					"SELECT a FROM FacLiquidadorPinpad a WHERE a.esActivo = 'S' AND UPPER(a.codigoLiquidador) = UPPER(:codigoAdquirente) ",
					FacLiquidadorPinpad.class).setParameter("codigoAdquirente", strCodigoAdquirente).getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}
}
