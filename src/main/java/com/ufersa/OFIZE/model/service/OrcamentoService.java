package com.ufersa.OFIZE.model.service;

import java.time.LocalDate;
import java.util.List;

import com.ufersa.OFIZE.exceptions.EntidadeNaoEncontradaException; // Import da exceção
import com.ufersa.OFIZE.model.dao.OrcamentoDAO;
import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Orcamento;
import com.ufersa.OFIZE.model.entitie.OrcamentoPeca;
import com.ufersa.OFIZE.model.entitie.Pecas;

public class OrcamentoService {

    private OrcamentoDAO dao;
    private PecasService pecasService;

    public OrcamentoService() {
        this.dao = new OrcamentoDAO();
        this.pecasService = new PecasService();
    }

    public void salvarOrcamento(Orcamento orcamento) {
        try {
            dao.salvar(orcamento);
            System.out.println("Orçamento salvo com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao salvar orçamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Orcamento buscarOrcamentoPorId(Long id) {
        try {
            Orcamento orcamento = dao.buscarPorId(id);
            if (orcamento == null) {
                throw new EntidadeNaoEncontradaException("Orçamento", id); // Adição da exceção
            }
            return orcamento;
        } catch (Exception e) {
            System.err.println("Erro ao buscar orçamento por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Orcamento> buscarTodos() {
        try {
            return dao.buscarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os orçamentos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void atualizarOrcamento(Orcamento orcamento) {
        try {
            Orcamento orcamentoExistente = dao.buscarPorId(orcamento.getId());
            if (orcamentoExistente == null) {
                throw new EntidadeNaoEncontradaException("Orçamento", orcamento.getId()); // Adição da exceção
            }
            dao.atualizar(orcamento);
            System.out.println("Orçamento atualizado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar orçamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deletarOrcamento(Long id) {
        try {
            Orcamento orcamentoParaDeletar = dao.buscarPorId(id);
            if (orcamentoParaDeletar == null) {
                throw new EntidadeNaoEncontradaException("Orçamento", id); // Adição da exceção
            }

            if (orcamentoParaDeletar.getOrcamentoPecas() != null) {
                for (OrcamentoPeca op : orcamentoParaDeletar.getOrcamentoPecas()) {
                    if (op.getPeca() != null) {
                        pecasService.incrementarQuantidade(op.getPeca().getId(), op.getQuantidade());
                    }
                }
            }

            dao.deletar(id);
            System.out.println("Orçamento excluído com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao excluir orçamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Orcamento> pesquisarOrcamentos(String veiculo, Clientes cliente, LocalDate inicio, LocalDate fim) {
        try {
            return dao.buscarPorVeiculoClienteOuPeriodo(veiculo, cliente, inicio, fim);
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar orçamentos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Orcamento> buscarOrcamentosParaRelatorio(LocalDate inicio, LocalDate fim, Boolean statusConcluido, Boolean statusPago) {
        try {
            return dao.buscarPorPeriodoEStatus(inicio, fim, statusConcluido, statusPago);
        } catch (Exception e) {
            System.err.println("Erro ao buscar orçamentos para relatório: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void encerrarConexao() {
        dao.close();
    }
}