package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Clientes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

// Classe-base abstrata para o DAO de Clientes.
public abstract class ClienteDAOAbstract {

    // Gerenciador de entidades do JPA.
    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    protected final EntityManager em = emf.createEntityManager();

    // Salva um novo cliente.
    public void persist(Clientes cliente) {
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
    }

    // Busca um cliente pelo ID.
    public Clientes findById(Long id) {
        return em.find(Clientes.class, id);
    }

    // Lista todos os clientes.
    public List<Clientes> findAll() {
        return em.createQuery("FROM Clientes", Clientes.class).getResultList();
    }

    // Atualiza um cliente existente.
    public void merge(Clientes cliente) {
        em.getTransaction().begin();
        em.merge(cliente);
        em.getTransaction().commit();
    }

    // Remove um cliente.
    public void remove(Clientes cliente) {
        em.getTransaction().begin();
        if (!em.contains(cliente)) {
            cliente = em.merge(cliente);
        }
        em.remove(cliente);
        em.getTransaction().commit();
    }
}