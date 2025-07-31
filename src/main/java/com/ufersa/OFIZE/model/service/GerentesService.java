package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.exceptions.AutenticacaoException;
import com.ufersa.OFIZE.model.entitie.Gerentes;

public class GerentesService extends GerenteServiceAbstract {

    public void criarGerentePadrao() {
        if (gerentesDAO.findByUsuario("admin") == null) {
            Gerentes admin = new Gerentes("admin", "admin");
            gerentesDAO.persist(admin);
            System.out.println("Gerente 'admin' criado com sucesso.");
        }
    }

    public Gerentes login(String usuario, String senha) throws AutenticacaoException {
        if (usuario == null || usuario.trim().isEmpty() || senha == null || senha.isEmpty()) {
            throw new AutenticacaoException("Usuário e senha devem ser preenchidos.");
        }
        Gerentes gerente = gerentesDAO.findByUsuario(usuario);

        if (gerente == null) {
            return null;
        }

        if (!gerente.getSenha().equals(senha)) {
            throw new AutenticacaoException("Senha inválida.");
        }

        return gerente;
    }
}