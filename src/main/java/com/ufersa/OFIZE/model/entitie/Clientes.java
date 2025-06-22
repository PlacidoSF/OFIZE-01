package com.ufersa.OFIZE.model.entitie;
public class Clientes {

    // atributos
    private String nome;
    private String endereco;
    private String cpf;

    //Construtor || cadastrar
    public Clientes(String nome, String endereco, String cpf) {
        setNome(nome);
        setEndereco(endereco);
        setCPF(cpf);
    }

    // Set e Get: nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome != null || !nome.isEmpty()) {
            this.nome = nome;
        } else {
            this.nome = null;
        }
    }

    // Set e Get: endereço
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        if(endereco != null || !endereco.isEmpty()) {
            this.endereco = endereco;
        } else {
            this.endereco = null;
        } 
    }

    //Set e Get: cpf
    public String getCPF() {
        return cpf;
    }
    
    public void setCPF(String cpf) {
        if(cpf != null || !cpf.isEmpty()) {
            this.cpf = cpf;
        } else {
            this.cpf = null;
        }  
    }

    //Alterar
    public void aLterar(String nome,  String endereco, String cpf) {
        setNome(nome);
        setEndereco(endereco);
        setCPF(cpf);
    }

    //Deletar
    public void deletar() {
        this.nome = null;
        this.endereco = null;
        this.cpf = null;
    }
    
}
