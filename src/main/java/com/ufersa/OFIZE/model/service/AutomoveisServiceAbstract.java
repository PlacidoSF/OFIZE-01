package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.AutomoveisDAO;

public abstract class AutomoveisServiceAbstract {

    protected final AutomoveisDAO automoveisDAO;

    public AutomoveisServiceAbstract() {
        this.automoveisDAO = new AutomoveisDAO();
    }
}