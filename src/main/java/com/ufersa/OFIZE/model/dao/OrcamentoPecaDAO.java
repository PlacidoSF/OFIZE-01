package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.OrcamentoPeca;
import java.util.List;

public class OrcamentoPecaDAO extends DAOGenerico<OrcamentoPeca, Long> {

    public OrcamentoPecaDAO() {
        super(OrcamentoPeca.class); // Chama o construtor do DAOGenerico, passando a classe da entidade
    }


    public List<OrcamentoPeca> findByOrcamentoId(Long orcamentoId) {
        // 'em' é acessível por ser 'protected' na classe DAOGenerico
        return em.createQuery("SELECT op FROM OrcamentoPeca op WHERE op.orcamento.id = :orcamentoId", OrcamentoPeca.class)
                .setParameter("orcamentoId", orcamentoId)
                .getResultList();
    }
}