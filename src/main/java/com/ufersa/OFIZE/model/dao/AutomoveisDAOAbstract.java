package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.utils.PersistenceManager;
import javax.persistence.EntityManager;
import java.util.List;

public abstract class AutomoveisDAOAbstract {

    protected final EntityManager em = PersistenceManager.getInstance().getEntityManager();

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