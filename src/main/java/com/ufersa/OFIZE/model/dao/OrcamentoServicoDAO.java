package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.OrcamentoServico;
import java.util.List;

public class OrcamentoServicoDAO extends DAOGenerico<OrcamentoServico, Long> {

    public OrcamentoServicoDAO() {
        super(OrcamentoServico.class); // Chama o construtor do DAOGenerico, passando a classe da entidade
    }

    /**
     * Busca todos os itens de orçamento de serviço associados a um determinado ID de orçamento.
     * @param orcamentoId O ID do orçamento.
     * @return Uma lista de OrcamentoServico para o orçamento especificado.
     */
    public List<OrcamentoServico> findByOrcamentoId(Long orcamentoId) {
        // 'em' é acessível por ser 'protected' na classe DAOGenerico
        return em.createQuery("SELECT os FROM OrcamentoServico os WHERE os.orcamento.id = :orcamentoId", OrcamentoServico.class)
                .setParameter("orcamentoId", orcamentoId)
                .getResultList();
    }
}