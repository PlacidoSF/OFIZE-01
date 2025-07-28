package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.ServicoDAO;
import com.ufersa.OFIZE.model.entitie.Servico;

import java.util.List;


public class ServicoService {

    private final ServicoDAO servicoDAO; // O DAO é privado e final, instanciado no construtor

    public ServicoService() {
        this.servicoDAO = new ServicoDAO(); // Inicializa o DAO
    }


    public void salvarServico(Servico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("O objeto Serviço não pode ser nulo.");
        }
        if (servico.getNome() == null || servico.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do serviço não pode ser vazio.");
        }
        if (servico.getValor() <= 0) {
            throw new IllegalArgumentException("O valor do serviço deve ser maior que zero.");
        }

        servicoDAO.persist(servico);
    }


    public Servico buscarServicoPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do serviço inválido.");
        }
        return servicoDAO.findById(id);
    }


    public List<Servico> listarTodosServicos() {
        return servicoDAO.findAll();
    }


    public void atualizarServico(Servico servico) {
        if (servico == null || servico.getId() == null) {
            throw new IllegalArgumentException("Serviço ou ID do serviço não pode ser nulo para atualização.");
        }
        if (servico.getNome() == null || servico.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do serviço não pode ser vazio.");
        }
        if (servico.getValor() <= 0) {
            throw new IllegalArgumentException("O valor do serviço deve ser maior que zero.");
        }


        Servico existingServico = servicoDAO.findById(servico.getId());
        if (existingServico == null) {
            throw new IllegalArgumentException("Serviço com ID " + servico.getId() + " não encontrado para atualização.");
        }

        servicoDAO.merge(servico);
    }



    public void removerServico(Servico servico) {
        if (servico == null || servico.getId() == null) {
            throw new IllegalArgumentException("Serviço ou ID do serviço não pode ser nulo para remoção.");
        }

        servicoDAO.remove(servico);
    }


    public List<Servico> buscarServicosPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {

            return servicoDAO.findAll();
        }
        return servicoDAO.findByNomeContaining(nome);
    }

}