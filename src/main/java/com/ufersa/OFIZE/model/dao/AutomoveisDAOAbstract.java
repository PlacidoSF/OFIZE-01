package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public abstract class AutomoveisDAOAbstract {

    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    protected final EntityManager em = emf.createEntityManager();

    public void persist(Automoveis automovel) {
        em.getTransaction().begin();
        em.persist(automovel);
        em.getTransaction().commit();
    }

    public Automoveis findById(Long id) {
        return em.find(Automoveis.class, id);
    }

    public List<Automoveis> findAll() {
        return em.createQuery("FROM Automoveis", Automoveis.class).getResultList();
    }

    public void merge(Automoveis automovel) {
        em.getTransaction().begin();
        em.merge(automovel);
        em.getTransaction().commit();
    }

    public void remove(Automoveis automovel) {
        em.getTransaction().begin();
        if (!em.contains(automovel)) {
            automovel = em.merge(automovel);
        }
        em.remove(automovel);
        em.getTransaction().commit();
    }
}