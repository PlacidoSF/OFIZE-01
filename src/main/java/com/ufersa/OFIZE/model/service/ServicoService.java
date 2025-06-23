package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.AutomoveisDAO;
import com.ufersa.OFIZE.model.dao.ServicoDAO;
import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Servico;
import java.util.List;

public class ServicoService {

    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final AutomoveisDAO automoveisDAO = new AutomoveisDAO();

    public ServicoService() {
    }

    public void cadastrarServico(Servico servico) {
        if (!isServicoValido(servico)) {
            throw new IllegalArgumentException("Dados do serviço são inválidos");
        }

        Automoveis automovelExistente = automoveisDAO.findById(servico.getAutomovel().getId());
        if (automovelExistente == null) {
            throw new IllegalArgumentException("Automóvel associado não cadastrado");
        }

        servico.setAutomovel(automovelExistente);
        servicoDAO.persist(servico);
    }

    public Servico buscarServico(Long id) {
        return servicoDAO.findById(id);
    }

    public void atualizarServico(Servico servicoAtualizado) {
        if (servicoAtualizado.getId() == null) {
            throw new IllegalArgumentException("ID do serviço não pode ser nulo para atualização");
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
                throw new IllegalArgumentException("Automóvel associado não encontrado");
            }
            servicoExistente.setAutomovel(novoAutomovel);
        }

        if (!isServicoValido(servicoExistente)) {
            throw new IllegalArgumentException("Dados do serviço são inválidos após atualização");
        }

        servicoDAO.merge(servicoExistente);
    }

    public boolean finalizarERegistrarPagamento(Long id) {
        Servico servico = servicoDAO.findById(id);
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        if (servico.finalizarERegistrarPagamento()) {
            servicoDAO.merge(servico);
            return true;
        }
        return false;
    }

    public List<Servico> buscarServicosPorProprietarioCpf(String cpf) {
        return servicoDAO.findByProprietarioCpf(cpf);
    }

    public void removerServico(Long id) {
        Servico servico = servicoDAO.findById(id);
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }
        servicoDAO.remove(servico);
    }

    private boolean isServicoValido(Servico servico) {
        return servico != null &&
                servico.getNome() != null && !servico.getNome().isEmpty() &&
                servico.getValor() > 0 &&
                servico.getAutomovel() != null &&
                servico.getAutomovel().getId() != null;
    }
}