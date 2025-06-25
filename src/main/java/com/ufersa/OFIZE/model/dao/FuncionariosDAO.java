package com.ufersa.OFIZE.model.dao;

import java.util.List;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.EntityManagerFactory;
import com.ufersa.OFIZE.model.entitie.Funcionarios;



public class FuncionariosDAO{
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    private final EntityManager em = emf.createEntityManager();

    public FuncionariosDAO() {
    }

    //Cria um novo funcionario no banco de dados
    public void persist(Funcionarios funcn) {
        em.getTransaction().begin();
        em.persist(funcn);
        em.getTransaction().commit();
    }

    //Atualiza um funcionario que já existe no banco de dados
    public void merge(Funcionarios funcn) {
        em.getTransaction().begin();
        em.merge(funcn);
        em.getTransaction().commit();
    }

    //Remove um funcionario do banco de dados
    public void remove(Funcionarios funcn) {
        em.getTransaction().begin();
        em.remove(funcn);
        em.getTransaction().commit();
    }

    //Busca o funcionario pelo seu id
    public Funcionarios findById(Long id) {
        try {
            return em.find(Funcionarios.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    //Seleciona todos os funcionarios
    public List<Funcionarios> findAll() {
        return em.createQuery("SELECT f FROM Funcionarios f", Funcionarios.class).getResultList();
    }

}

