package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.exceptions.AutenticacaoException;
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

        try {
            if (funcionariosDAO.findByUsuario(funcn.getUsuario()) != null || gerentesService.login(funcn.getUsuario(), "qualquer_coisa") != null) {
                throw new DadoDuplicadoException("Este nome de usuário já está em uso.");
            }
        } catch (AutenticacaoException e) {
            // Ignorado intencionalmente
        }

        funcionariosDAO.persist(funcn);
    }

    public Funcionarios login(String usuario, String senha) throws AutenticacaoException {
        if (usuario == null || usuario.trim().isEmpty() || senha == null || senha.isEmpty()) {
            throw new AutenticacaoException("Usuário e senha devem ser preenchidos.");
        }
        Funcionarios funcn = funcionariosDAO.findByUsuario(usuario);

        if (funcn == null) {
            throw new AutenticacaoException("Usuário não encontrado.");
        }

        if (!funcn.getSenha().equals(senha)) {
            throw new AutenticacaoException("Senha inválida.");
        }

        return funcn;
    }
}