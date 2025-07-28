package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Servico;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

// Classe-base abstrata para o DAO de Serviço.
public abstract class ServicoDAOAbstract {

    // Gerenciador de entidades do JPA.
    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    protected final EntityManager em = emf.createEntityManager();

    // Salva um novo serviço.
    public void persist(Servico servico) {
        em.getTransaction().begin();
        em.persist(servico);
        em.getTransaction().commit();
    }

    // Busca um serviço pelo ID.
    public Servico findById(Long id) {
        return em.find(Servico.class, id);
    }

    // Lista todos os serviços.
    public List<Servico> findAll() {
        return em.createQuery("SELECT s FROM Servico s", Servico.class).getResultList();
    }

    // Atualiza um serviço existente.
    public void merge(Servico servico) {
        em.getTransaction().begin();
        em.merge(servico);
        em.getTransaction().commit();
    }

    // Remove um serviço.
    public void remove(Servico servico) {
        em.getTransaction().begin();
        if (!em.contains(servico)) {
            servico = em.merge(servico);
        }
        em.remove(servico);
        em.getTransaction().commit();
    }
}
