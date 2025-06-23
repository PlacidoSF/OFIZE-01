package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Clientes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;

public class ClientesDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    private final EntityManager em = emf.createEntityManager();;

    public ClientesDAO() {
    }

    /**
     * Persiste um novo cliente no banco de dados
     * @param cliente Objeto Cliente a ser persistido
     */
    public void persist(Clientes cliente) {
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
    }

    /**
     * Busca um cliente pelo CPF (chave primária)
     * @param cpf CPF do cliente
     * @return Cliente encontrado ou null se não existir
     */
    public Clientes findById(String cpf) {
        try {
            return em.find(Clientes.class, cpf);
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retorna todos os clientes cadastrados
     * @return Lista de todos os clientes
     */
    public List<Clientes> findAll() {
        return em.createQuery("SELECT c FROM Clientes c", Clientes.class).getResultList();
    }

    /**
     * Atualiza um cliente existente no banco de dados
     * @param cliente Objeto Cliente com dados atualizados
     */
    public void merge(Clientes cliente) {
        em.getTransaction().begin();
        em.merge(cliente);
        em.getTransaction().commit();
    }

    /**
     * Remove um cliente do banco de dados
     * @param cliente Objeto Cliente a ser removido
     */
    public void remove(Clientes cliente) {
        em.getTransaction().begin();
        em.remove(cliente);
        em.getTransaction().commit();
    }
}