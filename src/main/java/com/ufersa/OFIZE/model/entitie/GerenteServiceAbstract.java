package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.dao.GerentesDAO;

public abstract class GerenteServiceAbstract {
    protected final GerentesDAO gerentesDAO;

    public GerenteServiceAbstract() {
        this.gerentesDAO = new GerentesDAO();
    }
}