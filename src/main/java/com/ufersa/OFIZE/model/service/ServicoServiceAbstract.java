package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.ServicoDAO;

// Classe-base abstrata para o Serviço de Serviço.
public abstract class ServicoServiceAbstract {

    // Instância do DAO para uso nas classes filhas.
    protected final ServicoDAO servicoDAO;

    // Construtor que inicializa o DAO.
    public ServicoServiceAbstract() {
        this.servicoDAO = new ServicoDAO();
    }
}
