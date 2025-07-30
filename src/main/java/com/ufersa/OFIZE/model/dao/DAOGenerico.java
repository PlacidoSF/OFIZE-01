// src/main/java/com.ufersa/OFIZE.model.dao/DAOGenerico.java
package com.ufersa.OFIZE.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;

public abstract class DAOGenerico<T, ID> {

    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    protected final EntityManager em = emf.createEntityManager();

    private Class<T> entityClass;

    public DAOGenerico(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // Salva uma nova entidade
    public void persist(T entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    // ATENÇÃO AQUI: O método merge agora retorna 'T'
    public T merge(T entity) { // MUDANÇA AQUI: de 'void' para 'T'
        em.getTransaction().begin();
        T mergedEntity = em.merge(entity); // Captura o resultado do merge
        em.getTransaction().commit();
        return mergedEntity; // Retorna a entidade gerenciada
    }

    // Remove uma entidade
    public void remove(T entity) {
        em.getTransaction().begin();
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        em.getTransaction().commit();
    }

    // Busca por ID
    public T findById(ID id) {
        try {
            return em.find(entityClass, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    // Lista todas as entidades do tipo T
    public List<T> findAll() {
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