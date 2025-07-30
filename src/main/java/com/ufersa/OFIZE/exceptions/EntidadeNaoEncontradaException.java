package com.ufersa.OFIZE.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public EntidadeNaoEncontradaException(String entidadeNome, Long id) {
        super(String.format("%s com ID %d não encontrada.", entidadeNome, id));
    }

    public EntidadeNaoEncontradaException(String entidadeNome, String identificador) {
        super(String.format("%s '%s' não encontrada.", entidadeNome, identificador));
    }
}
