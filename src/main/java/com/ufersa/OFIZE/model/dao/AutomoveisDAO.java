package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Clientes; // Certifique-se de importar Clientes
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class AutomoveisDAO extends AutomoveisDAOAbstract {

    // O EntityManager 'em' já está disponível através da classe abstrata
    // protected final EntityManager em = PersistenceManager.getInstance().getEntityManager();

    // Construtor padrão (não precisa mais de EntityManager aqui)
    public AutomoveisDAO() {
        // Nada a fazer aqui, o 'em' é inicializado na classe abstrata
    }

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
        String jpql = "SELECT a FROM Automoveis a WHERE a.proprietario.id = :clienteId";
        TypedQuery<Automoveis> query = em.createQuery(jpql, Automoveis.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    // Método para buscar por placa
    public Automoveis buscarPorPlaca(String placa) {
        // O EntityManager 'em' já está acessível da classe abstrata
        try {
            String jpql = "SELECT a FROM Automoveis a WHERE a.placa = :placa";
            TypedQuery<Automoveis> query = em.createQuery(jpql, Automoveis.class);
            query.setParameter("placa", placa);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}