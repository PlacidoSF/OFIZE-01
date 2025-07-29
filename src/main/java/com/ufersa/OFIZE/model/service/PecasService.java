package com.ufersa.OFIZE.model.service;

import java.util.List;
import com.ufersa.OFIZE.model.dao.PecasDao; // Assumindo que PecasDAO está em dao
import com.ufersa.OFIZE.model.entitie.Pecas;

public class PecasService{

    private final PecasDao dao = new PecasDao();

    public PecasService(){
    }

    //Confirma se todos os dados de peça são válidos
    private boolean ValidarPeca(Pecas peca) {
        boolean isIdValid = (peca.getId() == null || peca.getId() > 0);
        return peca != null && isIdValid && peca.getPreco() > 0 &&
                peca.getNome() != null && !peca.getNome().isEmpty() &&
                peca.getFabricante() != null && !peca.getFabricante().isEmpty() && peca.getQuantidade() > 0;
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

    // Pesquisa peças por nome ou fabricante. Se ambos forem vazios, retorna todas as peças.
    public List<Pecas> pesquisar(String nome, String fabricante) {
        // Normaliza os inputs para garantir que null e strings vazias sejam tratadas uniformemente
        String trimmedNome = (nome == null) ? "" : nome.trim();
        String trimmedFabricante = (fabricante == null) ? "" : fabricante.trim();

        // Se ambos os campos de pesquisa estiverem vazios, retorne todas as peças
        if (trimmedNome.isEmpty() && trimmedFabricante.isEmpty()) {
            return dao.findAll(); // <--- CHAME dao.findAll() AQUI
        }

        // Caso contrário, execute a busca específica no DAO
        return dao.buscarPorNomeOufabricante(trimmedNome, trimmedFabricante);
    }
}