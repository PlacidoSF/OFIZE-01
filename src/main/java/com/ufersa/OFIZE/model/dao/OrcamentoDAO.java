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

    //Busca todos os orçamentos
    public Orcamento buscarPorId(Long id) {
        return em.find(Orcamento.class, id);
    }


    //Atualiza um orçamento existente
    public void atualizar(Orcamento orcamento) {
        em.getTransaction().begin();
        em.merge(orcamento);
        em.getTransaction().commit();
    }

    //Deleta um orçamento pelo ID
    public void deletar(Long id) {
        Orcamento orc = em.find(Orcamento.class, id);
        if (orc != null) {
            em.getTransaction().begin();
            em.remove(orc);
            em.getTransaction().commit();
        }
    }

    //Pesquisa por veículo, cliente ou período

   public List<Orcamento> buscarPorVeiculoClienteOuPeriodo(String veiculo, Clientes cliente, LocalDate inicio, LocalDate fim) {
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
        em.close();
        emf.close();
    }
}
