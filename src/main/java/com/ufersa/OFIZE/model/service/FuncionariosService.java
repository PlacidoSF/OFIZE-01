package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.exceptions.DadoDuplicadoException;
import com.ufersa.OFIZE.model.entitie.Funcionarios;

public class FuncionariosService extends FuncionarioServiceAbstract {

    private final GerentesService gerentesService = new GerentesService();

    public void cadastrarFuncionario(Funcionarios funcn) {
        if (funcn == null || funcn.getUsuario() == null || funcn.getUsuario().trim().isEmpty() ||
                funcn.getSenha() == null || funcn.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Usuário e senha não podem ser vazios.");
        }
        if ("admin".equalsIgnoreCase(funcn.getUsuario())) {
            throw new IllegalArgumentException("O nome de usuário 'admin' é reservado para o gerente.");
        }
        if (funcionariosDAO.findByUsuario(funcn.getUsuario()) != null || gerentesService.login(funcn.getUsuario(), "") != null) {
            throw new DadoDuplicadoException("Este nome de usuário já está em uso.");
        }

        funcionariosDAO.persist(funcn);
    }

    public Funcionarios login(String usuario, String senha) {
        if (usuario == null || usuario.trim().isEmpty() || senha == null || senha.isEmpty()) {
            return null;
        }
        Funcionarios funcn = funcionariosDAO.findByUsuario(usuario);
        if (funcn != null && funcn.getSenha().equals(senha)) {
            return funcn;
        }
        return null;
    }
}