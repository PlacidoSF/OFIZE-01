package com.ufersa.OFIZE.model.service;

import java.time.LocalDate;
import java.util.List;

import com.ufersa.OFIZE.model.dao.OrcamentoDAO;
import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Orcamento;

public class OrcamentoService {

    private OrcamentoDAO dao = new OrcamentoDAO();

    public void salvarOrcamento(Orcamento orcamento) {
        try {
            dao.salvar(orcamento);
            System.out.println("Orçamento salvo com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao salvar orçamento: " + e.getMessage());
        }
    }

    public Orcamento buscarOrcamentoPorId(Long id) {
        try {
            return dao.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar orçamento por ID: " + e.getMessage());
            return null;
        }
    }

    public void atualizarOrcamento(Orcamento orcamento) {
        try {
            dao.atualizar(orcamento);
            System.out.println("Orçamento atualizado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar orçamento: " + e.getMessage());
        }
    }

    public void deletarOrcamento(Long id) {
        try {
            dao.deletar(id);
            System.out.println("Orçamento deletado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao deletar orçamento: " + e.getMessage());
        }
    }

    public List<Orcamento> pesquisarOrcamentos(String veiculo, Clientes cliente, LocalDate inicio, LocalDate fim) {
        try {
            return dao.buscarPorVeiculoClienteOuPeriodo(veiculo, cliente, inicio, fim);
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar orçamentos: " + e.getMessage());
            return List.of(); // retorna lista vazia
        }
    }

    public void encerrarConexao() {
        try {
            dao.fechar();
        } catch (Exception e) {
            System.err.println("Erro ao encerrar conexão com o banco: " + e.getMessage());
        }
    }
}
