package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.ClientesDAO;

// Classe-base abstrata para o Serviço de Clientes.
public abstract class ClienteServiceAbstract {

    // Instância do DAO para uso nas classes filhas.
    protected final ClientesDAO clientesDAO;

    // Construtor que inicializa o DAO.
    public ClienteServiceAbstract() {
        this.clientesDAO = new ClientesDAO();
    }
}