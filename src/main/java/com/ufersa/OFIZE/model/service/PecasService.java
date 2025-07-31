package com.ufersa.OFIZE.model.service;

import java.util.List;
import com.ufersa.OFIZE.exceptions.EntidadeNaoEncontradaException;
import com.ufersa.OFIZE.model.dao.PecasDao;
import com.ufersa.OFIZE.model.entitie.Pecas;

public class PecasService {

    private final PecasDao dao;

    public PecasService() {
        this.dao = new PecasDao();
    }

    private boolean ValidarPeca(Pecas peca) {
        return peca != null && peca.getPreco() > 0 &&
                peca.getNome() != null && !peca.getNome().trim().isEmpty() &&
                peca.getFabricante() != null && !peca.getFabricante().trim().isEmpty() && peca.getQuantidade() >= 0;
    }

    public void cadastrarPeca(Pecas peca) {
        try {
            if (ValidarPeca(peca)) {
                dao.persist(peca);
                System.out.println("Peça cadastrada com sucesso!");
            } else {
                System.err.println("Dados da peça inválidos para cadastro.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void atualizarPeca(Pecas peca) {
        try {
            if (ValidarPeca(peca)) {
                Pecas pecaExistente = dao.findById(peca.getId());
                if (pecaExistente == null) {
                    throw new EntidadeNaoEncontradaException("Peça", peca.getId());
                }
                dao.merge(peca);
                System.out.println("Peça atualizada com sucesso!");
            } else {
                System.err.println("Dados da peça inválidos para atualização.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removerPeca(Pecas peca) {
        try {
            if (peca != null && peca.getId() != null) {
                Pecas pecaExistente = dao.findById(peca.getId());
                if (pecaExistente == null) {
                    throw new EntidadeNaoEncontradaException("Peça", peca.getId());
                }
                dao.remove(pecaExistente);
                System.out.println("Peça removida com sucesso!");
            } else {
                System.err.println("ID da peça inválido para remoção.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao remover peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Pecas buscarPeca(Long id) {
        try {
            Pecas peca = dao.findById(id);
            if (peca == null) {
                throw new EntidadeNaoEncontradaException("Peça", id);
            }
            return peca;
        } catch (Exception e) {
            System.err.println("Erro ao buscar peça: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Pecas> buscarTodasPecas() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todas as peças: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Pecas> buscarPecasPorNomeOuFabricante(String nome, String fabricante) {
        try {
            return dao.buscarPorNomeOufabricante(nome, fabricante);
        } catch (Exception e) {
            System.err.println("Erro ao buscar peças por nome ou fabricante: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void decrementarQuantidade(Long pecaId, int quantidade) {
        try {
            Pecas peca = buscarPeca(pecaId);
            if (peca != null) {
                if (peca.getQuantidade() >= quantidade) {
                    peca.setQuantidade(peca.getQuantidade() - quantidade);
                    dao.merge(peca);
                } else {
                    System.err.println("Erro: Quantidade insuficiente em estoque para a peça " + peca.getNome());
                }
            }
        } catch (EntidadeNaoEncontradaException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao decrementar quantidade da peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void incrementarQuantidade(Long pecaId, int quantidade) {
        try {
            Pecas peca = buscarPeca(pecaId);
            if (peca != null) {
                peca.setQuantidade(peca.getQuantidade() + quantidade);
                dao.merge(peca);
            }
        } catch (EntidadeNaoEncontradaException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao incrementar quantidade da peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        dao.close();
    }
}