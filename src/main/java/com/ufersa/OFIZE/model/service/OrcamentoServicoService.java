package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.OrcamentoServicoDAO;
import com.ufersa.OFIZE.model.entitie.OrcamentoServico;
import java.util.List;

public class OrcamentoServicoService {

    private final OrcamentoServicoDAO orcamentoServicoDAO;

    public OrcamentoServicoService() {
        this.orcamentoServicoDAO = new OrcamentoServicoDAO();
    }

    /**
     * Salva ou atualiza um item de serviço de orçamento.
     * @param orcamentoServico O item de serviço de orçamento a ser salvo/atualizado.
     */
    public void saveOrUpdate(OrcamentoServico orcamentoServico) {
        // Exemplo de validação:
        if (orcamentoServico.getServico() == null || orcamentoServico.getOrcamento() == null) {
            throw new IllegalArgumentException("Serviço e Orçamento devem ser associados ao item.");
        }
        if (orcamentoServico.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade do serviço deve ser maior que zero.");
        }

        if (orcamentoServico.getId() != null) {
            orcamentoServicoDAO.merge(orcamentoServico);
        } else {
            orcamentoServicoDAO.persist(orcamentoServico);
        }
    }

    /**
     * Remove um item de serviço de orçamento.
     * @param orcamentoServico O item de serviço de orçamento a ser removido.
     */
    public void remove(OrcamentoServico orcamentoServico) {
        if (orcamentoServico == null || orcamentoServico.getId() == null) {
            throw new IllegalArgumentException("Item de serviço de orçamento inválido para remoção.");
        }
        orcamentoServicoDAO.remove(orcamentoServico);
    }

    /**
     * Busca um item de serviço de orçamento pelo ID.
     * @param id O ID do item de serviço de orçamento.
     * @return O OrcamentoServico encontrado ou null se não existir.
     */
    public OrcamentoServico findById(Long id) {
        return orcamentoServicoDAO.findById(id);
    }

    /**
     * Busca todos os itens de serviço de orçamento.
     * @return Uma lista de todos os OrcamentoServico.
     */
    public List<OrcamentoServico> findAll() {
        return orcamentoServicoDAO.findAll();
    }

    /**
     * Busca todos os itens de serviço associados a um orçamento específico.
     * @param orcamentoId O ID do orçamento.
     * @return Uma lista de OrcamentoServico para o orçamento especificado.
     */
    public List<OrcamentoServico> findByOrcamentoId(Long orcamentoId) {
        if (orcamentoId == null) {
            throw new IllegalArgumentException("O ID do orçamento não pode ser nulo.");
        }
        return orcamentoServicoDAO.findByOrcamentoId(orcamentoId);
    }

    /**
     * Fecha o EntityManager associado a este serviço.
     * Deve ser chamado ao finalizar o uso do serviço para liberar recursos.
     */
    public void close() {
        orcamentoServicoDAO.close();
    }
}