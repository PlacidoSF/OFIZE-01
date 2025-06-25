package com.ufersa.OFIZE.model.dao;

import java.util.List;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.EntityManagerFactory;
import com.ufersa.OFIZE.model.entitie.Gerentes;



public class GerentesDAO{
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    private final EntityManager em = emf.createEntityManager();

    public GerentesDAO() {
    }

    //Cria um novo gerente no banco de dados
    public void persist(Gerentes gerente) {
        em.getTransaction().begin();
        em.persist(gerente);
        em.getTransaction().commit();
    }

    //Atualiza um gerente que já existe no banco de dados
    public void merge(Gerentes gerente) {
        em.getTransaction().begin();
        em.merge(gerente);
        em.getTransaction().commit();
    }

    //Remove um gerente do banco de dados
    public void remove(Gerentes gerente) {
        em.getTransaction().begin();
        em.remove(gerente);
        em.getTransaction().commit();
    }

    //Busca o gerente pelo seu id
    public Gerentes findById(Long id) {
            return em.find(Gerentes.class, id);
    }

    //Seleciona todos os gerentes, mas acredito que não seja necessário
    public List<Gerentes> findAll() {
        return em.createQuery("SELECT g FROM Gerentes g", Gerentes.class).getResultList();
    }

}

