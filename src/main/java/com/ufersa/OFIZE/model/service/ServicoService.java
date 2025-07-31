package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.exceptions.EntidadeNaoEncontradaException;
import com.ufersa.OFIZE.model.dao.ServicoDAO;
import com.ufersa.OFIZE.model.entitie.Servico;

import java.util.List;

public class ServicoService {

    private final ServicoDAO servicoDAO;

    public ServicoService() {
        this.servicoDAO = new ServicoDAO();
    }

    public void salvarServico(Servico servico) {
        try {
            if (servico != null && servico.getNome() != null && !servico.getNome().trim().isEmpty() && servico.getValor() > 0) {
                servicoDAO.persist(servico);
                System.out.println("Serviço salvo com sucesso.");
            } else {
                System.err.println("Dados do serviço inválidos.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Servico buscarServicoPorId(Long id) {
        try {
            Servico servico = servicoDAO.findById(id);
            if (servico == null) {
                throw new EntidadeNaoEncontradaException("Serviço", id);
            }
            return servico;
        } catch (Exception e) {
            System.err.println("Erro ao buscar serviço por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Servico> listarTodosServicos() {
        try {
            return servicoDAO.findAll();
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os serviços: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void atualizarServico(Servico servico) {
        try {
            if (servico != null && servico.getId() != null && servico.getNome() != null && !servico.getNome().trim().isEmpty() && servico.getValor() > 0) {
                Servico servicoExistente = servicoDAO.findById(servico.getId());
                if (servicoExistente == null) {
                    throw new EntidadeNaoEncontradaException("Serviço", servico.getId());
                }
                servicoDAO.merge(servico);
                System.out.println("Serviço atualizado com sucesso.");
            } else {
                System.err.println("Dados do serviço inválidos para atualização.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removerServico(Long id) {
        try {
            if (id != null) {
                Servico servicoExistente = servicoDAO.findById(id);
                if (servicoExistente == null) {
                    throw new EntidadeNaoEncontradaException("Serviço", id);
                }
                servicoDAO.remove(servicoExistente);
                System.out.println("Serviço removido com sucesso.");
            } else {
                System.err.println("ID do serviço inválido para remoção.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao remover serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Servico> buscarServicoPorNome(String nome) {
        try {
            return servicoDAO.findByNomeContaining(nome);
        } catch (Exception e) {
            System.err.println("Erro ao buscar serviço por nome: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        servicoDAO.close();
    }
}