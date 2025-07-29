package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.FuncionariosDAO;

public abstract class FuncionarioServiceAbstract {
    protected final FuncionariosDAO funcionariosDAO;

    public FuncionarioServiceAbstract() {
        this.funcionariosDAO = new FuncionariosDAO();
    }
}