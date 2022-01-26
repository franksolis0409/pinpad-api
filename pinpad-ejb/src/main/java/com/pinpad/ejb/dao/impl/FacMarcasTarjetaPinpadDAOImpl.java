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

import com.pinpad.ejb.model.FacMarcasTarjetaPinpad;

import lombok.NonNull;

/**
 * @author H P
 *
 */
@Stateless
public class FacMarcasTarjetaPinpadDAOImpl extends BaseDAO<FacMarcasTarjetaPinpad, Long> {

	protected FacMarcasTarjetaPinpadDAOImpl() {
		super(FacMarcasTarjetaPinpad.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(FacMarcasTarjetaPinpad t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(FacMarcasTarjetaPinpad t) throws PersistenceException {

		super.update(t);
	}

	@Override
	public Optional<FacMarcasTarjetaPinpad> find(@NonNull Long id) {

		return super.find(id);
	}

	public FacMarcasTarjetaPinpad findByGrupoTarjeta(String strGrupoTarjeta) {
		try {
			return (em.createQuery(
					"SELECT a FROM FacMarcasTarjetaPinpad a WHERE a.esActivo = 'S' AND UPPER(a.grupoTarjeta) = UPPER(:grupoTarjeta) ",
					FacMarcasTarjetaPinpad.class).setParameter("grupoTarjeta", strGrupoTarjeta).getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}
}
