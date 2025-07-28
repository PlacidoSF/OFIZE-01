package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Servico;
import java.util.List;
import javax.persistence.TypedQuery;

// DAO concreto para Serviço, herda de ServicoDAOAbstract.
public class ServicoDAO extends ServicoDAOAbstract {

    // Busca serviços pelo CPF do proprietário (método específico).
    public List<Servico> findByProprietarioCpf(String cpf) {
        TypedQuery<Servico> query = em.createQuery(
                "SELECT s FROM Servico s WHERE s.automovel.proprietario.cpf = :cpf", Servico.class);
        query.setParameter("cpf", cpf);
        return query.getResultList();
    }
}