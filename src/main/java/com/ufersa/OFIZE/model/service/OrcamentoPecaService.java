package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.exceptions.EntidadeNaoEncontradaException; // Import da exceção
import com.ufersa.OFIZE.model.dao.OrcamentoPecaDAO;
import com.ufersa.OFIZE.model.entitie.OrcamentoPeca;
import com.ufersa.OFIZE.model.entitie.Pecas;

import java.util.List;

public class OrcamentoPecaService {

    private final OrcamentoPecaDAO orcamentoPecaDAO;
    private final PecasService pecasService;

    public OrcamentoPecaService() {
        this.orcamentoPecaDAO = new OrcamentoPecaDAO();
        this.pecasService = new PecasService();
    }

    public void saveOrUpdate(OrcamentoPeca orcamentoPeca) {
        try {
            if (orcamentoPeca != null && orcamentoPeca.getPeca() != null && orcamentoPeca.getOrcamento() != null) {
                Pecas pecaAssociada = orcamentoPeca.getPeca();
                int quantidadeNova = orcamentoPeca.getQuantidade();
                int quantidadeAntiga = 0;

                Pecas pecaNoEstoque = pecasService.buscarPeca(pecaAssociada.getId()); // Este pode lançar a exceção

                if (orcamentoPeca.getId() != null) {
                    OrcamentoPeca existingOrcamentoPeca = orcamentoPecaDAO.findById(orcamentoPeca.getId());
                    if (existingOrcamentoPeca == null) {
                        throw new EntidadeNaoEncontradaException("Item de Orçamento-Peça", orcamentoPeca.getId()); // Adição da exceção
                    }
                    quantidadeAntiga = existingOrcamentoPeca.getQuantidade();

                    pecasService.incrementarQuantidade(pecaNoEstoque.getId(), quantidadeAntiga);
                    pecasService.decrementarQuantidade(pecaNoEstoque.getId(), quantidadeNova);

                    orcamentoPecaDAO.merge(orcamentoPeca);
                    System.out.println("Item de orçamento-peça atualizado com sucesso!");
                } else {
                    pecasService.decrementarQuantidade(pecaNoEstoque.getId(), quantidadeNova);
                    orcamentoPecaDAO.persist(orcamentoPeca);
                    System.out.println("Item de orçamento-peça salvo com sucesso!");
                }
            } else {
                System.err.println("Dados do item de orçamento-peça inválidos.");
            }
        } catch (EntidadeNaoEncontradaException e) { // Captura exceção de peça ou do próprio item
            throw e; // Relança para a camada superior
        } catch (Exception e) {
            System.err.println("Erro ao salvar/atualizar item de orçamento-peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void remove(OrcamentoPeca orcamentoPeca) {
        try {
            if (orcamentoPeca != null && orcamentoPeca.getId() != null) {
                OrcamentoPeca existingOrcamentoPeca = orcamentoPecaDAO.findById(orcamentoPeca.getId());
                if (existingOrcamentoPeca == null) {
                    throw new EntidadeNaoEncontradaException("Item de Orçamento-Peça", orcamentoPeca.getId()); // Adição da exceção
                }

                if (existingOrcamentoPeca.getPeca() != null && existingOrcamentoPeca.getQuantidade() > 0) {
                    pecasService.incrementarQuantidade(existingOrcamentoPeca.getPeca().getId(), existingOrcamentoPeca.getQuantidade());
                }

                orcamentoPecaDAO.remove(existingOrcamentoPeca);
                System.out.println("Item de orçamento-peça removido com sucesso!");
            } else {
                System.err.println("ID do item de orçamento-peça inválido para remoção.");
            }
        } catch (EntidadeNaoEncontradaException e) { // Captura exceção se o item não for encontrado
            throw e; // Relança para a camada superior
        } catch (Exception e) {
            System.err.println("Erro ao remover item de orçamento-peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public OrcamentoPeca findById(Long id) {
        try {
            OrcamentoPeca orcamentoPeca = orcamentoPecaDAO.findById(id);
            if (orcamentoPeca == null) {
                throw new EntidadeNaoEncontradaException("Item de Orçamento-Peça", id); // Adição da exceção
            }
            return orcamentoPeca;
        } catch (Exception e) {
            System.err.println("Erro ao buscar item de orçamento-peça por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<OrcamentoPeca> findByOrcamentoId(Long orcamentoId) {
        try {
            return orcamentoPecaDAO.findByOrcamentoId(orcamentoId);
        } catch (Exception e) {
            System.err.println("Erro ao buscar itens de orçamento-peça por ID de orçamento: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        orcamentoPecaDAO.close();
    }
}