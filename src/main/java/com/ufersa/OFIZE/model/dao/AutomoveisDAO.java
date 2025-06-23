package com.ufersa.OFIZE.model.dao;

import java.util.List;

import com.ufersa.OFIZE.model.entitie.Automoveis;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class AutomoveisDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    private final EntityManager em = emf.createEntityManager();

    public AutomoveisDAO() {}

    // Salva novo automóvel
    public void persist(Automoveis auto) {
        em.getTransaction().begin();
        em.persist(auto);
        em.getTransaction().commit();
    }

    // Atualiza um automóvel existente
    public void merge(Automoveis auto) {
        em.getTransaction().begin();
        em.merge(auto);
        em.getTransaction().commit();
    }

    // Remove um automóvel
    public void remove(Automoveis auto) {
        em.getTransaction().begin();
        em.remove(em.contains(auto) ? auto : em.merge(auto));
        em.getTransaction().commit();
    }

    // Busca por ID
    //Nao sei se será necessario no projeto final
    public Automoveis findById(Long id) {
        try {
            return em.find(Automoveis.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    // Lista todos os automóveis
    public List<Automoveis> findAll() {
        return em.createQuery("SELECT a FROM Automoveis a", Automoveis.class).getResultList();
    }

    // Busca por marca ou proprietário (uso do like para pesquisa parcial)
    public List<Automoveis> buscarPorMarcaOuProprietario(String marca, String nomeProprietario) {
        String jpql = "SELECT a FROM Automoveis a WHERE " +
                "(:marca IS NULL OR LOWER(a.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) " +
                "OR (:nomeProprietario IS NULL OR LOWER(a.proprietario.nome) LIKE LOWER(CONCAT('%', :nomeProprietario, '%')))";

        return em.createQuery(jpql, Automoveis.class)
                .setParameter("marca", marca)
                .setParameter("nomeProprietario", nomeProprietario)
                .getResultList();
    }
}
