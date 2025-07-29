package com.ufersa.OFIZE.utils;

import com.ufersa.OFIZE.model.service.GerentesService;

public class DataInitializer {

    /**
     * Este método verifica se o gerente padrão 'admin' existe no banco de dados
     * e, caso não exista, realiza o cadastro.
     */
    public static void criarGerentePadraoSeNaoExistir() {
        GerentesService gerentesService = new GerentesService();
        gerentesService.criarGerentePadrao();
    }
}