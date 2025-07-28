package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.entitie.Clientes;
import java.util.List;

public class ClientesService extends ClienteServiceAbstract {

    public ClientesService() {
        super();
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

    public void removerClientePorCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        Clientes clienteExistente = clientesDAO.findByCpf(cpf);
        if (clienteExistente != null) {
            clientesDAO.remove(clienteExistente);
        } else {
            throw new IllegalArgumentException("Cliente com o CPF informado não foi encontrado.");
        }
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
        clientesDAO.remove(cliente);
    }

    private boolean isClienteValido(Clientes cliente) {
        return cliente != null &&
                cliente.getCpf() != null && !cliente.getCpf().isEmpty() &&
                cliente.getNome() != null && !cliente.getNome().isEmpty() &&
                cliente.getEndereco() != null && !cliente.getEndereco().isEmpty();
    }
}