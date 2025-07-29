package com.ufersa.OFIZE.model.dao;

import java.time.LocalDate;
import java.util.List;

import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Orcamento;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class OrcamentoDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    private final EntityManager em = emf.createEntityManager();

    public OrcamentoDAO() {
    }

    public void salvar(Orcamento orcamento) {
        em.getTransaction().begin();
        em.persist(orcamento);
        em.getTransaction().commit();
    }

    public Orcamento buscarPorId(Long id) {
        return em.find(Orcamento.class, id);
    }

    // NOVO MÉTODO: buscarTodos()
    public List<Orcamento> buscarTodos() {
        TypedQuery<Orcamento> query = em.createQuery("SELECT o FROM Orcamento o", Orcamento.class);
        return query.getResultList();
    }

    public void atualizar(Orcamento orcamento) {
        em.getTransaction().begin();
        em.merge(orcamento);
        em.getTransaction().commit();
    }

    public void deletar(Long id) {
        Orcamento orc = em.find(Orcamento.class, id);
        if (orc != null) {
            em.getTransaction().begin();
            em.remove(orc);
            em.getTransaction().commit();
        }
    }

    public List<Orcamento> buscarPorVeiculoClienteOuPeriodo(String veiculo, Clientes cliente, LocalDate inicio, LocalDate fim) {
        // Note: This JPQL query with multiple ORs might not behave as expected if you want to combine filters with AND logic.
        // For "search by vehicle OR client OR period", this is fine.
        // If you intend to allow flexible combinations (e.g., vehicle AND client, or client AND period),
        // you'll need a more dynamic query builder or separate methods.
        String jpql = "SELECT o FROM Orcamento o WHERE " +
                "(:veiculo IS NOT NULL AND LOWER(o.veiculo) LIKE LOWER(CONCAT('%', :veiculo, '%'))) OR " +
                "(:cliente IS NOT NULL AND o.cliente = :cliente) OR " +
                "(:inicio IS NOT NULL AND o.data >= :inicio) OR " +
                "(:fim IS NOT NULL AND o.data <= :fim)";

        TypedQuery<Orcamento> query = em.createQuery(jpql, Orcamento.class);
        query.setParameter("veiculo", veiculo);
        query.setParameter("cliente", cliente);
        query.setParameter("inicio", inicio);
        query.setParameter("fim", fim);

        return query.getResultList();
    }

    public void fechar() {
        if (em.isOpen()) {
            em.close();
        }
        if (emf.isOpen()) {
            emf.close();
        }
    }
}