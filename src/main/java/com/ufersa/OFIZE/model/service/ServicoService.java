package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.AutomoveisDAO;
import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Servico;
import java.util.List;

// Serviço concreto com as regras de negócio para Serviço.
public class ServicoService extends ServicoServiceAbstract {

    // DAO para Automoveis, necessário para validações.
    private final AutomoveisDAO automoveisDAO = new AutomoveisDAO();

    // Cadastra um novo serviço com validações.
    public void cadastrarServico(Servico servico) {
        if (!isServicoValido(servico)) {
            throw new IllegalArgumentException("Dados do serviço são inválidos.");
        }
        Automoveis automovelExistente = automoveisDAO.findById(servico.getAutomovel().getId());
        if (automovelExistente == null) {
            throw new IllegalArgumentException("Automóvel associado não cadastrado.");
        }
        servico.setAutomovel(automovelExistente);
        servicoDAO.persist(servico);
    }

    // Busca um serviço pelo ID contido no objeto.
    public Servico buscarServico(Servico servico) {
        return servicoDAO.findById(servico.getId());
    }

    // Atualiza um serviço existente com validações.
    public void atualizarServico(Servico servicoAtualizado) {
        if (servicoAtualizado.getId() == null) {
            throw new IllegalArgumentException("ID do serviço não pode ser nulo para atualização.");
        }
        Servico servicoExistente = servicoDAO.findById(servicoAtualizado.getId());
        if (servicoExistente == null) {
            throw new IllegalArgumentException("Serviço não encontrado para o ID: " + servicoAtualizado.getId());
        }
        servicoExistente.setNome(servicoAtualizado.getNome());
        servicoExistente.setValor(servicoAtualizado.getValor());

        if (servicoAtualizado.getAutomovel() != null &&
                !servicoAtualizado.getAutomovel().getId().equals(servicoExistente.getAutomovel().getId())) {
            Automoveis novoAutomovel = automoveisDAO.findById(servicoAtualizado.getAutomovel().getId());
            if (novoAutomovel == null) {
                throw new IllegalArgumentException("Automóvel associado não encontrado.");
            }
            servicoExistente.setAutomovel(novoAutomovel);
        }
        if (!isServicoValido(servicoExistente)) {
            throw new IllegalArgumentException("Dados do serviço são inválidos após atualização.");
        }
        servicoDAO.merge(servicoExistente);
    }

    // Finaliza um serviço e registra seu pagamento.
    public boolean finalizarERegistrarPagamento(Servico servico) {
        Servico servicoParaFinalizar = servicoDAO.findById(servico.getId());
        if (servicoParaFinalizar == null) {
            throw new IllegalArgumentException("Serviço não encontrado.");
        }
        if (servicoParaFinalizar.finalizarERegistrarPagamento()) {
            servicoDAO.merge(servicoParaFinalizar);
            return true;
        }
        return false;
    }

    // Busca todos os serviços de um proprietário pelo CPF.
    public List<Servico> buscarServicosPorProprietarioCpf(String cpf) {
        return servicoDAO.findByProprietarioCpf(cpf);
    }

    // Remove um serviço do sistema.
    public void removerServico(Servico servico) {
        Servico servicoParaRemover = servicoDAO.findById(servico.getId());
        if (servicoParaRemover == null) {
            throw new IllegalArgumentException("Serviço não encontrado.");
        }
        servicoDAO.remove(servicoParaRemover);
    }

    // Valida os dados obrigatórios do serviço.
    private boolean isServicoValido(Servico servico) {
        return servico != null &&
                servico.getNome() != null && !servico.getNome().isEmpty() &&
                servico.getValor() > 0 &&
                servico.getAutomovel() != null &&
                servico.getAutomovel().getId() != null;
    }
}