package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Pecas;
import javax.persistence.TypedQuery;
import java.util.List;

// Estende DAOGenerico com a entidade Pecas e o tipo de ID Long
public class PecasDao extends DAOGenerico<Pecas, Long> {


    // Construtor
    public PecasDao() {
        super(Pecas.class);
    }


    public void persist(Pecas pecas) {
        super.persist(pecas);
    }

    public Pecas merge(Pecas pecas) {
        super.merge(pecas);
        return pecas;
    }

    public void remove(Pecas pecas) {

        Pecas pecasManaged = super.findById(pecas.getId());
        if (pecasManaged != null) {
            super.remove(pecasManaged);
        } else {
            throw new IllegalArgumentException("Peça não encontrada para remoção.");
        }
    }

    public Pecas findById(Long id) {
        return super.findById(id);
    }

    public List<Pecas> findAll() {
        return super.findAll();
    }


    public List<Pecas> buscarPorNomeOufabricante(String nome, String fabricante) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM Pecas p WHERE ");
        boolean firstCondition = true;

        String trimmedNome = (nome != null) ? nome.trim().toLowerCase() : "";
        String trimmedFabricante = (fabricante != null) ? fabricante.trim().toLowerCase() : "";

        if (!trimmedNome.isEmpty()) {
            jpql.append("LOWER(p.nome) LIKE :nome");
            firstCondition = false;
        }

        if (!trimmedFabricante.isEmpty()) {
            if (!firstCondition) {
                jpql.append(" OR ");
            }
            jpql.append("LOWER(p.fabricante) LIKE :fabricante");
            firstCondition = false;
        }

        if (firstCondition) {
            jpql.append("1=1");
        }

        TypedQuery<Pecas> query = em.createQuery(jpql.toString(), Pecas.class); // 'em' é acessível via protected da superclasse

        if (!trimmedNome.isEmpty()) {
            query.setParameter("nome", "%" + trimmedNome + "%");
        }
        if (!trimmedFabricante.isEmpty()) {
            query.setParameter("fabricante", "%" + trimmedFabricante + "%");
        }

        return query.getResultList();
    }


    @Override
    public void close() {
        super.close();
    }
}