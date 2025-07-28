package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.OrcamentoPecaDAO;
import com.ufersa.OFIZE.model.entitie.OrcamentoPeca;
import java.util.List;

public class OrcamentoPecaService {

    private final OrcamentoPecaDAO orcamentoPecaDAO;

    public OrcamentoPecaService() {
        this.orcamentoPecaDAO = new OrcamentoPecaDAO();
    }

    /**
     * Salva ou atualiza um item de peça de orçamento.
     * @param orcamentoPeca O item de peça de orçamento a ser salvo/atualizado.
     */
    public void saveOrUpdate(OrcamentoPeca orcamentoPeca) {
        // Exemplo de validação:
        if (orcamentoPeca.getPeca() == null || orcamentoPeca.getOrcamento() == null) {
            throw new IllegalArgumentException("Peça e Orçamento devem ser associados ao item.");
        }
        if (orcamentoPeca.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade da peça deve ser maior que zero.");
        }

        // Se o item já tem um ID, ele já existe e deve ser mergeado
        if (orcamentoPeca.getId() != null) {
            orcamentoPecaDAO.merge(orcamentoPeca);
        } else {
            // Caso contrário, é um novo item e deve ser persistido
            orcamentoPecaDAO.persist(orcamentoPeca);
        }
    }

    /**
     * Remove um item de peça de orçamento.
     * @param orcamentoPeca O item de peça de orçamento a ser removido.
     */
    public void remove(OrcamentoPeca orcamentoPeca) {
        if (orcamentoPeca == null || orcamentoPeca.getId() == null) {
            throw new IllegalArgumentException("Item de peça de orçamento inválido para remoção.");
        }
        orcamentoPecaDAO.remove(orcamentoPeca);
    }

    /**
     * Busca um item de peça de orçamento pelo ID.
     * @param id O ID do item de peça de orçamento.
     * @return O OrcamentoPeca encontrado ou null se não existir.
     */
    public OrcamentoPeca findById(Long id) {
        return orcamentoPecaDAO.findById(id);
    }

    /**
     * Busca todos os itens de peça de orçamento.
     * @return Uma lista de todos os OrcamentoPeca.
     */
    public List<OrcamentoPeca> findAll() {
        return orcamentoPecaDAO.findAll();
    }

    /**
     * Busca todos os itens de peça associados a um orçamento específico.
     * @param orcamentoId O ID do orçamento.
     * @return Uma lista de OrcamentoPeca para o orçamento especificado.
     */
    public List<OrcamentoPeca> findByOrcamentoId(Long orcamentoId) {
        if (orcamentoId == null) {
            throw new IllegalArgumentException("O ID do orçamento não pode ser nulo.");
        }
        return orcamentoPecaDAO.findByOrcamentoId(orcamentoId);
    }

    /**
     * Fecha o EntityManager associado a este serviço.
     * Deve ser chamado ao finalizar o uso do serviço para liberar recursos.
     */
    public void close() {
        orcamentoPecaDAO.close();
    }
}