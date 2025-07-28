// src/main/java/com/ufersa/OFIZE.model.dao/DAOGenerico.java
package com.ufersa.OFIZE.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;

public abstract class DAOGenerico<T, ID> { // T para o tipo da entidade, ID para o tipo do ID

    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    protected final EntityManager em = emf.createEntityManager();

    private Class<T> entityClass; // Para saber qual entidade está sendo gerenciada

    public DAOGenerico(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // Salva uma nova entidade
    public void persist(T entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    // Atualiza uma entidade existente
    public void merge(T entity) {
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
    }

    // Remove uma entidade
    public void remove(T entity) {
        em.getTransaction().begin();
        em.remove(em.contains(entity) ? entity : em.merge(entity)); // Garante que a entidade esteja no contexto gerenciado
        em.getTransaction().commit();
    }

    // Busca por ID
    public T findById(ID id) {
        try {
            return em.find(entityClass, id);
        } catch (NoResultException e) {
            return null; // Retorna null se não encontrar
        }
    }

    // Lista todas as entidades do tipo T
    public List<T> findAll() {
        // Usa o nome da classe da entidade para a consulta JPQL
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

    // O método close() é importante para liberar recursos
    public void close() {
        if (em.isOpen()) {
            em.close();
        }
        if (emf.isOpen()) {
            emf.close();
        }
    }
}