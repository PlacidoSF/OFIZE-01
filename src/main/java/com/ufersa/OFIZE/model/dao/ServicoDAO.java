package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Servico;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class ServicoDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    private final EntityManager em = emf.createEntityManager();

    public ServicoDAO() {
    }

    public void persist(Servico servico) {
        em.getTransaction().begin();
        em.persist(servico);
        em.getTransaction().commit();
    }

    public Servico findById(Long id) {
        return em.find(Servico.class, id);
    }

    public List<Servico> findAll() {
        TypedQuery<Servico> query = em.createQuery("SELECT s FROM Servico s", Servico.class);
        return query.getResultList();
    }

    /*
     * Busca serviços associados ao CPF do proprietário do automóvel
     */
    public List<Servico> findByProprietarioCpf(String cpf) {
        TypedQuery<Servico> query = em.createQuery(
                "SELECT s FROM Servico s WHERE s.automovel.proprietario.cpf = :cpf", Servico.class);
        query.setParameter("cpf", cpf);
        return query.getResultList();
    }

    public void merge(Servico servico) {
        em.getTransaction().begin();
        em.merge(servico);
        em.getTransaction().commit();
    }

    public void remove(Servico servico) {
        em.getTransaction().begin();
        em.remove(servico);
        em.getTransaction().commit();
    }
}
