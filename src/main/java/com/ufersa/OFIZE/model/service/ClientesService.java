package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.ClientesDAO;
import com.ufersa.OFIZE.model.entitie.Clientes;

public class ClientesService {
    private final ClientesDAO dao;

    public ClientesService(ClientesDAO dao) {
        this.dao = dao;
    }

    /**
     * Cadastra um novo cliente após validação
     * @param cliente Objeto Cliente válido
     * @throws IllegalArgumentException Se o cliente for inválido
     */
    public void cadastrarCliente(Clientes cliente) {
        if (isClienteValido(cliente)) {
            dao.persist(cliente);
        } else {
            throw new IllegalArgumentException("Dados do cliente são inválidos");
        }
    }

    /**
     * Busca um cliente pelo CPF
     * @param cpf CPF do cliente
     * @return Cliente encontrado ou null
     */
    public Clientes buscarCliente(String cpf) {
        return dao.findById(cpf);
    }

    /**
     * Atualiza os dados de um cliente existente
     * @param cliente Objeto Cliente com dados atualizados
     * @throws IllegalArgumentException Se o cliente não existir ou for inválido
     */
    public void atualizarCliente(Clientes cliente) {
        if (isClienteValido(cliente) && dao.findById(cliente.getCpf()) != null) {
            dao.merge(cliente);
        } else {
            throw new IllegalArgumentException("Cliente inválido ou não encontrado");
        }
    }

    /**
     * Remove um cliente do sistema
     * @param cliente Objeto Cliente a ser removido
     * @throws IllegalArgumentException Se o cliente não existir
     */
    public void removerCliente(Clientes cliente) {
        Clientes clienteExistente = dao.findById(cliente.getCpf());
        if (clienteExistente != null) {
            dao.remove(clienteExistente);
        } else {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
    }

    /**
     * Valida se um cliente tem todos os dados necessários
     * @param cliente Objeto Cliente a ser validado
     * @return true se válido, false caso contrário
     */
    private boolean isClienteValido(Clientes cliente) {
        return cliente != null &&
                cliente.getCpf() != null && !cliente.getCpf().isEmpty() &&
                cliente.getNome() != null && !cliente.getNome().isEmpty() &&
                cliente.getEndereco() != null && !cliente.getEndereco().isEmpty();
    }
}