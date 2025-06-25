package com.ufersa.OFIZE.model.service;

import java.util.List;
import com.ufersa.OFIZE.model.dao.FuncionariosDAO;
import com.ufersa.OFIZE.model.entitie.Funcionarios;

public class FuncionariosService{

    private final FuncionariosDAO dao = new FuncionariosDAO();

    public FuncionariosService(){
    }

    //Confirma se todos os dados de funcionário são válidos
    private boolean ValidarFuncionario(Funcionarios funcn) {
        return funcn != null && funcn.getUsuario() != null && !funcn.getUsuario().isEmpty() &&
                funcn.getSenha() != null && !funcn.getSenha().isEmpty();
    }

    //Cadastra um novo funcionário, mas validando os dados primeiro
    public void cadastrarFuncionario(Funcionarios funcn) {
        if (ValidarFuncionario(funcn)) {
            dao.persist(funcn);
        } else {
            throw new IllegalArgumentException("Os dados do funcionário são inválidos");
        }
    }

    //Atualiza um funcionário, mas validando os dados primeiro
    public void atualizarFuncionario(Funcionarios funcn) {
        if (ValidarFuncionario(funcn) && dao.findById(funcn.getId()) != null) {
            dao.merge(funcn);
        } else {
            throw new IllegalArgumentException("Funcionário possui dados inválidos ou não encontrados");
        }
    }

    //Remove um funcionario
    public void removerFuncionario(Funcionarios funcn) {
        Funcionarios funcnExistente = dao.findById(funcn.getId());
        if (funcnExistente != null) {
            dao.remove(funcnExistente);
        } else {
            throw new IllegalArgumentException("Funcionário não foi encontrado");
        }
    }

    //Busca um funcionário
    public Funcionarios buscarFuncionario(Long id) {
        return dao.findById(id);
    }

    //Busca todas os funcionários
    public List<Funcionarios> buscarTodos() {
        return dao.findAll();
    }

}