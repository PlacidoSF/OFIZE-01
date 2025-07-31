package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.exceptions.EntidadeNaoEncontradaException;
import com.ufersa.OFIZE.model.dao.OrcamentoServicoDAO;
import com.ufersa.OFIZE.model.entitie.OrcamentoServico;
import java.util.List;

public class OrcamentoServicoService {

    private final OrcamentoServicoDAO orcamentoServicoDAO;

    public OrcamentoServicoService() {
        this.orcamentoServicoDAO = new OrcamentoServicoDAO();
    }

    public void saveOrUpdate(OrcamentoServico orcamentoServico) {
        try {
            if (orcamentoServico != null && orcamentoServico.getServico() != null && orcamentoServico.getOrcamento() != null) {
                if (orcamentoServico.getId() != null) {
                    OrcamentoServico existingOrcamentoServico = orcamentoServicoDAO.findById(orcamentoServico.getId());
                    if (existingOrcamentoServico == null) {
                        throw new EntidadeNaoEncontradaException("Item de Orçamento-Serviço", orcamentoServico.getId());
                    }
                    orcamentoServicoDAO.merge(orcamentoServico);
                    System.out.println("Item de orçamento-serviço atualizado com sucesso!");
                } else {
                    orcamentoServicoDAO.persist(orcamentoServico);
                    System.out.println("Item de orçamento-serviço salvo com sucesso!");
                }
            } else {
                System.err.println("Dados do item de orçamento-serviço inválidos.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar/atualizar item de orçamento-serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void remove(OrcamentoServico orcamentoServico) {
        try {
            if (orcamentoServico != null && orcamentoServico.getId() != null) {
                OrcamentoServico existingOrcamentoServico = orcamentoServicoDAO.findById(orcamentoServico.getId());
                if (existingOrcamentoServico == null) {
                    throw new EntidadeNaoEncontradaException("Item de Orçamento-Serviço", orcamentoServico.getId());
                }
                orcamentoServicoDAO.remove(existingOrcamentoServico);
                System.out.println("Item de orçamento-serviço removido com sucesso!");
            } else {
                System.err.println("ID do item de orçamento-serviço inválido para remoção.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao remover item de orçamento-serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public OrcamentoServico findById(Long id) {
        try {
            OrcamentoServico orcamentoServico = orcamentoServicoDAO.findById(id);
            if (orcamentoServico == null) {
                throw new EntidadeNaoEncontradaException("Item de Orçamento-Serviço", id);
            }
            return orcamentoServico;
        } catch (Exception e) {
            System.err.println("Erro ao buscar item de orçamento-serviço por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<OrcamentoServico> findByOrcamentoId(Long orcamentoId) {
        try {
            return orcamentoServicoDAO.findByOrcamentoId(orcamentoId);
        } catch (Exception e) {
            System.err.println("Erro ao buscar itens de orçamento-serviço por ID de orçamento: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        orcamentoServicoDAO.close();
    }
}