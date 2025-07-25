package com.ufersa.OFIZE.model.service;

import java.util.List;
import com.ufersa.OFIZE.model.dao.GerentesDAO;
import com.ufersa.OFIZE.model.entitie.Gerentes;

public class GerentesService{

    private final GerentesDAO dao = new GerentesDAO();

    public GerentesService(){
    }

    //Confirma se todos os dados de gerente são válidos
    private boolean validarGerenteParaCadastro(Gerentes gerente) {
        return gerente != null &&
                gerente.getUsuario() != null && !gerente.getUsuario().isEmpty() &&
                gerente.getSenha() != null && !gerente.getSenha().isEmpty();
    }

    //Cadastra um novo gerente, mas validando os dados primeiro
    public void cadastrarGerente(Gerentes gerente) {
        if (ValidarGerente(gerente)) {
            dao.persist(gerente);
        } else {
            throw new IllegalArgumentException("Os dados do gerente são inválidos");
        }
    }

    //Atualiza um gerente, mas validando os dados primeiro
    public void atualizarGerente(Gerentes gerente) {
        if (ValidarGerente(gerente) && dao.findById(gerente.getId()) != null) {
            dao.merge(gerente);
        } else {
            throw new IllegalArgumentException("Gerente possui dados inválidos ou não encontrados");
        }
    }

    //Remove um gerente
    public void removerGerente(Gerentes gerente) {
        Gerentes gerenteExistente = dao.findById(gerente.getId());
        if (gerenteExistente != null) {
            dao.remove(gerenteExistente);
        } else {
            throw new IllegalArgumentException("Gerente não foi encontrado");
        }
    }

    //Busca um funcionário
    public Gerentes buscarGerente(Long id) {
        return dao.findById(id);
    }

    //Busca todas os funcionários
    public List<Gerentes> buscarTodos() {
        return dao.findAll();
    }

}