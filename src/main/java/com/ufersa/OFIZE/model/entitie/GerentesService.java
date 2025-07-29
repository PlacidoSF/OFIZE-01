package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.entitie.Gerentes;

public class GerentesService extends GerenteServiceAbstract {

    public void criarGerentePadrao() {
        // Verifica se o gerente "admin" já existe. Se não, cria.
        if (gerentesDAO.findByUsuario("admin") == null) {
            Gerentes admin = new Gerentes("admin", "admin");
            gerentesDAO.persist(admin);
            System.out.println("Gerente 'admin' criado com sucesso.");
        }
    }

    public Gerentes login(String usuario, String senha) {
        if (usuario == null || usuario.trim().isEmpty() || senha == null || senha.isEmpty()) {
            return null;
        }
        Gerentes gerente = gerentesDAO.findByUsuario(usuario);
        if (gerente != null && gerente.getSenha().equals(senha)) {
            return gerente;
        }
        return null;
    }
}