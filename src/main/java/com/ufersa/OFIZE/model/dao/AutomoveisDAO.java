package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class AutomoveisDAO extends AutomoveisDAOAbstract {

    public List<Automoveis> buscarPorMarcaOuProprietario(String textoBusca) {
        String jpql = "SELECT a FROM Automoveis a WHERE " +
                "LOWER(a.marca) LIKE LOWER(:textoBusca) OR " +
                "LOWER(a.proprietario.nome) LIKE LOWER(:textoBusca)";

        TypedQuery<Automoveis> query = em.createQuery(jpql, Automoveis.class);
        query.setParameter("textoBusca", "%" + textoBusca + "%");
        return query.getResultList();
    }

    public Automoveis findByPlaca(String placa) {
        try {
            TypedQuery<Automoveis> query = em.createQuery("SELECT a FROM Automoveis a WHERE a.placa = :placa", Automoveis.class);
            query.setParameter("placa", placa);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean clientePossuiAutomoveis(Long clienteId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT count(a) FROM Automoveis a WHERE a.proprietario.id = :clienteId", Long.class
        );
        query.setParameter("clienteId", clienteId);
        return query.getSingleResult() > 0;
    }

    public List<Automoveis> findByClienteId(Long clienteId) {
        TypedQuery<Automoveis> query = em.createQuery(
                "SELECT a FROM Automoveis a WHERE a.proprietario.id = :clienteId", Automoveis.class
        );
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }
}