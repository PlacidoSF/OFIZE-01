package com.ufersa.OFIZE.model.dao;

import java.util.List;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.EntityManagerFactory;
import com.ufersa.OFIZE.model.entitie.Pecas;



public class PecasDAO{
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    private final EntityManager em = emf.createEntityManager();

    public PecasDAO() {
    }

    //Cria uma nova peça no banco de dados
    public void persist(Pecas peca) {
        em.getTransaction().begin();
        em.persist(peca);
        em.getTransaction().commit();
    }

    //Atualiza uma peça que já existe no banco de dados
    public void merge(Pecas peca) {
        em.getTransaction().begin();
        em.merge(peca);
        em.getTransaction().commit();
    }

    //Remove uma peça do banco de dados
    public void remove(Pecas peca) {
        em.getTransaction().begin();
        em.remove(peca);
        em.getTransaction().commit();
    }

    //Busca peça pelo seu id, coloquei esse como um adicional
    public Pecas findById(Long id) {
        try {
            return em.find(Pecas.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    //Seleciona todos as peças do banco de dados
    public List<Pecas> findAll() {
        return em.createQuery("SELECT p FROM Pecas p", Pecas.class).getResultList();
           }

    public List<Pecas> buscarPorNomeOufabricante(String nome, String fabricante) {
        String consulta = "SELECT p FROM Pecas p WHERE " +
                "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
                "OR (:fabricante IS NULL OR LOWER(p.fabricante) LIKE LOWER(CONCAT('%', :fabricante, '%')))";

        return em.createQuery(consulta, Pecas.class)
                .setParameter("nome", nome)
                .setParameter("fabricante", fabricante)
                .getResultList();
    }

}

