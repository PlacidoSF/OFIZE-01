package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Gerentes;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class GerentesDAO extends GerenteDAOAbstract {

    public Gerentes findByUsuario(String usuario) {
        try {
            TypedQuery<Gerentes> query = em.createQuery("SELECT g FROM Gerentes g WHERE g.usuario = :usuario", Gerentes.class);
            query.setParameter("usuario", usuario);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}