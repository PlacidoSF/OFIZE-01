package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.AutomoveisDAO;
import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Clientes;
import java.util.List;

public class ClientesService extends ClienteServiceAbstract {

    private final AutomoveisDAO automoveisDAO = new AutomoveisDAO();

    public ClientesService() {
        super();
    }

    public List<Clientes> buscarTodos() {
        return clientesDAO.findAll();
    }

    public void cadastrarCliente(Clientes cliente) {
        if (!isClienteValido(cliente)) {
            throw new IllegalArgumentException("Dados do cliente são inválidos.");
        }
        if (clientesDAO.findByCpf(cliente.getCpf()) != null) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema.");
        }
        clientesDAO.persist(cliente);
    }

    public Clientes buscarClientePorCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return null;
        }
        return clientesDAO.findByCpf(cpf);
    }

    public void atualizarCliente(Clientes cliente) {
        if (!isClienteValido(cliente) || cliente.getId() == null) {
            throw new IllegalArgumentException("Dados do cliente ou ID são inválidos para atualização.");
        }
        if (clientesDAO.findById(cliente.getId()) == null) {
            throw new IllegalArgumentException("Cliente não encontrado para o ID informado.");
        }
        Clientes clienteExistenteComCpf = clientesDAO.findByCpf(cliente.getCpf());
        if (clienteExistenteComCpf != null && !clienteExistenteComCpf.getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("O CPF informado já pertence a outro cliente.");
        }
        clientesDAO.merge(cliente);
    }

    public List<Clientes> pesquisarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return clientesDAO.findAll();
        } else {
            return clientesDAO.searchByName(nome);
        }
    }

    public void removerCliente(Clientes cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("Cliente inválido para remoção.");
        }
        List<Automoveis> automoveisDoCliente = automoveisDAO.findByClienteId(cliente.getId());
        for (Automoveis auto : automoveisDoCliente) {
            automoveisDAO.remove(auto);
        }
        clientesDAO.remove(cliente);
    }

    public boolean clientePossuiAutomoveis(Long clienteId) {
        return automoveisDAO.clientePossuiAutomoveis(clienteId);
    }

    private boolean isClienteValido(Clientes cliente) {
        return cliente != null &&
                cliente.getCpf() != null && !cliente.getCpf().isEmpty() &&
                cliente.getNome() != null && !cliente.getNome().isEmpty() &&
                cliente.getEndereco() != null && !cliente.getEndereco().isEmpty();
    }
}