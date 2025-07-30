package com.ufersa.OFIZE.model.dao;

import java.time.LocalDate;
import java.util.List;

import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Orcamento;

import javax.persistence.TypedQuery;


public class OrcamentoDAO extends DAOGenerico<Orcamento, Long> {

    public OrcamentoDAO() {
        super(Orcamento.class);
    }


    public void salvar(Orcamento orcamento) {
        super.persist(orcamento);
    }

    public Orcamento buscarPorId(Long id) {
        return super.findById(id);
    }

    public List<Orcamento> buscarTodos() {
        return super.findAll();
    }

    public void atualizar(Orcamento orcamento) {
        super.merge(orcamento);
    }

    public void deletar(Long id) {
        Orcamento orc = super.findById(id);
        if (orc != null) {
            super.remove(orc);
        }
    }


    public List<Orcamento> buscarPorVeiculoClienteOuPeriodo(String veiculo, Clientes cliente, LocalDate inicio, LocalDate fim) {
        String jpql = "SELECT o FROM Orcamento o WHERE " +
                "(:veiculo IS NOT NULL AND LOWER(o.veiculo) LIKE LOWER(CONCAT('%', :veiculo, '%'))) OR " +
                "(:cliente IS NOT NULL AND o.cliente = :cliente) OR " +
                "(:inicio IS NOT NULL AND o.data >= :inicio) OR " +
                "(:fim IS NOT NULL AND o.data <= :fim)";

        TypedQuery<Orcamento> query = em.createQuery(jpql, Orcamento.class); // 'em' é acessível via protected da superclasse
        query.setParameter("veiculo", veiculo);
        query.setParameter("cliente", cliente);
        query.setParameter("inicio", inicio);
        query.setParameter("fim", fim);

        return query.getResultList();
    }

    public List<Orcamento> buscarPorPeriodoEStatus(LocalDate inicio, LocalDate fim, Boolean statusConcluido, Boolean statusPago) {
        StringBuilder jpql = new StringBuilder("SELECT o FROM Orcamento o WHERE 1=1");

        if (inicio != null) {
            jpql.append(" AND o.data >= :inicio");
        }
        if (fim != null) {
            jpql.append(" AND o.data <= :fim");
        }
        if (statusConcluido != null) {
            jpql.append(" AND o.status = :statusConcluido");
        }
        if (statusPago != null) {
            jpql.append(" AND o.pago = :statusPago");
        }

        TypedQuery<Orcamento> query = em.createQuery(jpql.toString(), Orcamento.class); // 'em' é acessível via protected da superclasse

        if (inicio != null) {
            query.setParameter("inicio", inicio);
        }
        if (fim != null) {
            query.setParameter("fim", fim);
        }
        if (statusConcluido != null) {
            query.setParameter("statusConcluido", statusConcluido);
        }
        if (statusPago != null) {
            query.setParameter("statusPago", statusPago);
        }

        return query.getResultList();
    }

    public List<Orcamento> findByClienteId(Long clienteId) {
        return em.createQuery("SELECT o FROM Orcamento o WHERE o.cliente.id = :clienteId", Orcamento.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
    }

    @Override
    public void close() {
        super.close();
    }
}