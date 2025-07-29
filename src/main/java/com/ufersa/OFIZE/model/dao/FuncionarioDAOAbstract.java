package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Funcionarios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public abstract class FuncionarioDAOAbstract {
    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    protected final EntityManager em = emf.createEntityManager();

    public void persist(Funcionarios funcionario) {
        em.getTransaction().begin();
        em.persist(funcionario);
        em.getTransaction().commit();
    }

    public List<Funcionarios> findAll() {
        return em.createQuery("FROM Funcionarios", Funcionarios.class).getResultList();
    }

    // Outros métodos genéricos como merge, remove, etc. podem ser adicionados aqui.
}