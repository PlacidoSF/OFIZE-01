package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clientes")
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária autoincrementável.

    private String cpf;
    private String nome;
    private String endereco;

    // Construtor padrão para JPA.
    public Clientes() {
    }

    // Construtor com parâmetros.
    public Clientes(String nome, String endereco, String cpf) {
        this.nome = nome;
        this.endereco = endereco;
        this.cpf = cpf;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

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

    // Representação em string do objeto.
    @Override
    public String toString() {
        return "Cliente [ID: " + id + ", CPF: " + cpf + ", Nome: " + nome + ", Endereço: " + endereco + "]";
    }
}