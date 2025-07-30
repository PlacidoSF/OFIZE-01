package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Gerentes;
import com.ufersa.OFIZE.utils.PersistenceManager;
import javax.persistence.EntityManager;

public abstract class GerenteDAOAbstract {

    protected final EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public void persist(Gerentes gerente) {
        em.getTransaction().begin();
        em.persist(gerente);
        em.getTransaction().commit();
    }
}