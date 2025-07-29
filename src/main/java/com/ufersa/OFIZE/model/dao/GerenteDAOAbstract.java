package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Gerentes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class GerenteDAOAbstract {
    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    protected final EntityManager em = emf.createEntityManager();

    public void persist(Gerentes gerente) {
        em.getTransaction().begin();
        em.persist(gerente);
        em.getTransaction().commit();
    }
}