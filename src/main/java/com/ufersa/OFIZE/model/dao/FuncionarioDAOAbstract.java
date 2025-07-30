package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Funcionarios;
import com.ufersa.OFIZE.utils.PersistenceManager;
import javax.persistence.EntityManager;
import java.util.List;

public abstract class FuncionarioDAOAbstract {

    protected final EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public void persist(Funcionarios funcionario) {
        em.getTransaction().begin();
        em.persist(funcionario);
        em.getTransaction().commit();
    }

    public List<Funcionarios> findAll() {
        return em.createQuery("FROM Funcionarios", Funcionarios.class).getResultList();
    }
}