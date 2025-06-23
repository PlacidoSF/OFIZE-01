package com.ufersa.OFIZE.model.service;

import java.util.List;

import com.ufersa.OFIZE.model.dao.AutomoveisDAO;
import com.ufersa.OFIZE.model.entitie.Automoveis;

public class AutomoveisService {

    private final AutomoveisDAO dao = new AutomoveisDAO();

    public AutomoveisService() {

    }

    // Cadastro com validação básica
    public boolean cadastrar(Automoveis auto) {
        if (auto.getMarca() == null || auto.getMarca().isEmpty() ||
            auto.getProprietario() == null || auto.getProprietario().getCpf() == null) {
            return false;
        }

        dao.persist(auto);
        return true;
    }

    public void atualizar(Automoveis auto) {
        dao.merge(auto);
    }

    public void deletar(Automoveis auto) {
        dao.remove(auto);
    }

    public Automoveis buscarPorId(Long id) {
        return dao.findById(id);
    }

    public List<Automoveis> buscarTodos() {
        return dao.findAll();
    }

    // Pesquisa por marca ou proprietário com validação
    public List<Automoveis> pesquisar(String marca, String nomeProprietario) {
        if ((marca == null || marca.trim().isEmpty()) &&
            (nomeProprietario == null || nomeProprietario.trim().isEmpty())) {
            throw new IllegalArgumentException("Informe pelo menos a marca ou o proprietário para realizar a pesquisa.");
        }

        return dao.buscarPorMarcaOuProprietario(marca, nomeProprietario);
    }
}
