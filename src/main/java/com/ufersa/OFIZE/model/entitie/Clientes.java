package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clientes")
public class Clientes {

    @Id
    private String cpf;

    private String nome;
    private String endereco;

    /**
     * Construtor padrão obrigatório para JPA/Hibernate
     */
    public Clientes() {
    }

    /**
     * Construtor para criação de novos clientes
     * @param nome Nome completo do cliente
     * @param endereco Endereço completo do cliente
     * @param cpf CPF do cliente (chave primária)
     */
    public Clientes(String nome, String endereco, String cpf) {
        this.nome = nome;
        this.endereco = endereco;
        this.cpf = cpf;
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Representação em string do objeto Cliente
     * @return String formatada com os dados do cliente
     */
    @Override
    public String toString() {
        return "Cliente [CPF: " + cpf + ", Nome: " + nome + ", Endereço: " + endereco + "]";
    }
}