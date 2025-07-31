package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.OrcamentoServico;
import java.util.List;

public class OrcamentoServicoDAO extends DAOGenerico<OrcamentoServico, Long> {

    public OrcamentoServicoDAO() {
        super(OrcamentoServico.class);
    }


    public List<OrcamentoServico> findByOrcamentoId(Long orcamentoId) {
        return em.createQuery("SELECT os FROM OrcamentoServico os WHERE os.orcamento.id = :orcamentoId", OrcamentoServico.class)
                .setParameter("orcamentoId", orcamentoId)
                .getResultList();
    }
}