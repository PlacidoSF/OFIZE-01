package com.ufersa.OFIZE.model.service;

import java.util.List;
import com.ufersa.OFIZE.model.dao.PecasDAO;
import com.ufersa.OFIZE.model.entitie.Pecas;

public class PecasService{

    private final PecasDAO dao = new PecasDAO();

    public PecasService(){
    }

    //Confirma se todos os dados de peça são válidos
    private boolean ValidarPeca(Pecas peca) {
        return peca != null && peca.getId() != null &&
                peca.getId() > 0 && peca.getPreco() > 0 &&
                peca.getNome() != null && !peca.getNome().isEmpty() &&
                peca.getFabricante() != null && !peca.getFabricante().isEmpty();
    }

    //Cadastra uma nova peça, mas validando os dados primeiro
    public void cadastrarPeca(Pecas peca) {
        if (ValidarPeca(peca)) {
            dao.persist(peca);
        } else {
            throw new IllegalArgumentException("Os dados da peça são inválidos");
        }
    }

    //Atualiza uma peça, mas validando os dados primeiro
    public void atualizarPeca(Pecas peca) {
        if (ValidarPeca(peca) && dao.findById(peca.getId()) != null) {
            dao.merge(peca);
        } else {
            throw new IllegalArgumentException("Peça possui dados inválidos ou não encontrados");
        }
    }

    //Remove uma peça
    public void removerPeca(Pecas peca) {
        Pecas pecaExistente = dao.findById(peca.getId());
        if (pecaExistente != null) {
            dao.remove(pecaExistente);
        } else {
            throw new IllegalArgumentException("Peça não foi encontrada");
        }
    }

    //Busca uma peça
    public Pecas buscarPeca(Long id) {
        return dao.findById(id);
    }

    //Busca todas as peça
    public List<Pecas> buscarTodas() {
        return dao.findAll();
    }

    //
    public List<Pecas> pesquisar(String nome, String fabricante) {
        if ((nome == null || nome.trim().isEmpty()) &&
                (fabricante == null || fabricante.trim().isEmpty())) {
            System.err.println("Informe pelo menos o nome da peça ou seu fabricante para pesquisar");
            return List.of();
        }
            return dao.buscarPorNomeOufabricante(nome, fabricante);
        }

}