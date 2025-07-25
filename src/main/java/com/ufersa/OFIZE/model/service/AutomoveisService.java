package com.ufersa.OFIZE.model.service;

import java.util.List;

import com.ufersa.OFIZE.model.dao.AutomoveisDAO;
import com.ufersa.OFIZE.model.entitie.Automoveis;

public class AutomoveisService {

    private AutomoveisDAO dao = new AutomoveisDAO();

    public void cadastrar(Automoveis auto) {
        try {
            if (auto.getMarca() == null || auto.getMarca().isEmpty() ||
                auto.getProprietario() == null || auto.getProprietario().getCpf() == null) {
                System.err.println("Dados inválidos para cadastro do automóvel");
                return;
            }
            dao.persist(auto);
            System.out.println("Automóvel cadastrado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar automóvel: " + e.getMessage());
        }
    }

    public void atualizar(Automoveis auto) {
        try {
            dao.merge(auto);
            System.out.println("Automóvel atualizado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar automóvel: " + e.getMessage());
        }
    }

    public void deletar(Automoveis auto) {
        try {
            dao.remove(auto);
            System.out.println("Automóvel deletado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao deletar automóvel: " + e.getMessage());
        }
    }

    public Automoveis buscarPorId(Long id) {
        try {
            return dao.findById(id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar automóvel por ID: " + e.getMessage());
            return null;
        }
    }

    public List<Automoveis> buscarTodos() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os automóveis: " + e.getMessage());
            return List.of();
        }
    }

    public List<Automoveis> pesquisar(String marca, String nomeProprietario) {
        try {
            if ((marca == null || marca.trim().isEmpty()) &&
                (nomeProprietario == null || nomeProprietario.trim().isEmpty())) {
                System.err.println("Informe pelo menos a marca ou o proprietário para pesquisa");
                return List.of();
            }
            return dao.buscarPorMarcaOuProprietario(marca, nomeProprietario);
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar automóveis: " + e.getMessage());
            return List.of();
        }
    }

}