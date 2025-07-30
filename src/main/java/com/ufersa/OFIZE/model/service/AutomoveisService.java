package com.ufersa.OFIZE.model.service;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Clientes;

import java.util.List;

public class AutomoveisService extends AutomoveisServiceAbstract {

    public AutomoveisService() {
        super();
    }

    public void cadastrar(Automoveis automovel) {
        if (!isAutomovelValido(automovel)) {
            throw new IllegalArgumentException("Dados do automóvel são inválidos. Verifique todos os campos.");
        }
        if (automoveisDAO.findByPlaca(automovel.getPlaca()) != null) {
            throw new IllegalArgumentException("Placa já cadastrada no sistema.");
        }
        automoveisDAO.persist(automovel);
    }

    public void atualizar(Automoveis automovel) {
        if (automovel.getId() == null) {
            throw new IllegalArgumentException("ID do automóvel não pode ser nulo para atualização.");
        }
        if (!isAutomovelValido(automovel)) {
            throw new IllegalArgumentException("Dados do automóvel são inválidos para atualização.");
        }

        // Validação de placa única para atualização
        Automoveis automovelExistente = automoveisDAO.findByPlaca(automovel.getPlaca());
        if (automovelExistente != null && !automovelExistente.getId().equals(automovel.getId())) {
            throw new IllegalArgumentException("A placa informada já pertence a outro automóvel.");
        }

        automoveisDAO.merge(automovel);
    }

    // ... resto da classe ...
    public void deletar(Automoveis automovel) {
        if (automovel == null || automovel.getId() == null) {
            throw new IllegalArgumentException("Automóvel inválido para remoção.");
        }
        automoveisDAO.remove(automovel);
    }

    public Automoveis buscarPorId(Long id) {
        return automoveisDAO.findById(id);
    }

    public List<Automoveis> buscarTodos() {
        return automoveisDAO.findAll();
    }

    public List<Automoveis> pesquisar(String textoBusca) {
        if (textoBusca == null || textoBusca.trim().isEmpty()) {
            return automoveisDAO.findAll();
        }
        return automoveisDAO.buscarPorMarcaOuProprietario(textoBusca);
    }

    public List<Automoveis> buscarAutomoveisPorCliente(Clientes cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("Cliente ou ID do cliente não pode ser nulo para buscar automóveis.");
        }
        return automoveisDAO.findByClienteId(cliente.getId());
    }

    public Automoveis buscarAutomovelPorPlaca(String placa) {
        return automoveisDAO.buscarPorPlaca(placa);
    }

    private boolean isAutomovelValido(Automoveis automovel) {
        if (automovel == null) return false;
        if (automovel.getMarca() == null || automovel.getMarca().trim().isEmpty()) return false;
        if (automovel.getCor() == null || automovel.getCor().trim().isEmpty()) return false;
        if (automovel.getPlaca() == null || automovel.getPlaca().trim().isEmpty()) return false;
        if (automovel.getAno() <= 1885 || automovel.getAno() > java.time.LocalDate.now().getYear()) return false;
        if (automovel.getQuilometragem() < 0) return false;
        if (automovel.getProprietario() == null || automovel.getProprietario().getId() == null) return false;

        return true;
    }
}