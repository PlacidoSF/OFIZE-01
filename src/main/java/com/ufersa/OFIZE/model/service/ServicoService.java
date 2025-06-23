package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.ClientesDAO;
import com.ufersa.OFIZE.model.dao.ServicoDAO;
import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Servico;
import java.util.List;

public class ServicoService {

    private final ServicoDAO servicoDAO;
    private final ClientesDAO clientesDAO;

    public ServicoService(ServicoDAO servicoDAO, ClientesDAO clientesDAO) {
        this.servicoDAO = servicoDAO;
        this.clientesDAO = clientesDAO;
    }

    public void cadastrarServico(Servico servico) {
        if (!isServicoValido(servico)) {
            throw new IllegalArgumentException("Dados do serviço são inválidos");
        }

        Clientes clienteExistente = clientesDAO.findById(servico.getCliente().getCpf());
        if (clienteExistente == null) {
            throw new IllegalArgumentException("Cliente associado não cadastrado");
        }

        servico.setCliente(clienteExistente);
        servicoDAO.persist(servico);
    }

    public Servico buscarServico(Long id) {
        return servicoDAO.findById(id);
    }

    public void atualizarServico(Servico servicoAtualizado) {
        // Verifica se o ID está presente
        if (servicoAtualizado.getId() == null) {
            throw new IllegalArgumentException("ID do serviço não pode ser nulo para atualização");
        }

        // Busca o serviço existente no banco
        Servico servicoExistente = servicoDAO.findById(servicoAtualizado.getId());
        if (servicoExistente == null) {
            throw new IllegalArgumentException("Serviço não encontrado para o ID: " + servicoAtualizado.getId());
        }

        // Atualiza os campos permitidos
        servicoExistente.setNome(servicoAtualizado.getNome());
        servicoExistente.setValor(servicoAtualizado.getValor());

        // Se o cliente for alterado, verifica se o novo cliente existe
        if (servicoAtualizado.getCliente() != null &&
                !servicoAtualizado.getCliente().getCpf().equals(servicoExistente.getCliente().getCpf())) {

            Clientes novoCliente = clientesDAO.findById(servicoAtualizado.getCliente().getCpf());
            if (novoCliente == null) {
                throw new IllegalArgumentException("Cliente associado não encontrado");
            }
            servicoExistente.setCliente(novoCliente);
        }

        // Valida o serviço atualizado
        if (!isServicoValido(servicoExistente)) {
            throw new IllegalArgumentException("Dados do serviço são inválidos após atualização");
        }

        // Persiste as alterações
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

    public List<Servico> buscarServicosPorCliente(String cpf) {
        return servicoDAO.findByClienteCpf(cpf);
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
                servico.getCliente() != null &&
                servico.getCliente().getCpf() != null;
    }
}