package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.utils.PersistenceManager;
import javax.persistence.EntityManager;
import java.util.List;

public abstract class ClienteDAOAbstract {

    protected final EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public void persist(Clientes cliente) {
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
    }

    public Clientes findById(Long id) {
        return em.find(Clientes.class, id);
    }

    public List<Clientes> findAll() {
        return em.createQuery("FROM Clientes", Clientes.class).getResultList();
    }

    public void merge(Clientes cliente) {
        em.getTransaction().begin();
        em.merge(cliente);
        em.getTransaction().commit();
    }

    public void remove(Clientes cliente) {
        em.getTransaction().begin();
        if (!em.contains(cliente)) {
            cliente = em.merge(cliente);
        }
        em.remove(cliente);
        em.getTransaction().commit();
    }
}