package com.ufersa.OFIZE.model.entitie;
public class Pecas {
    //Atributos
    private String nome;
    private double preco;
    private String fabricante;

    //Construtor
    public Pecas(String nome, double preco, String fabricante){
        setNome(nome);
        setPreco(preco);
        setFabricante(fabricante);
    }

    //Métodos Getters e Setters
        //Nome
    public void setNome(String nome){
        if(!nome.isEmpty()){
            this.nome = nome;
        } else{
            this.nome = null;
        }
    }

    public String getNome(){
        return nome;
    }

        //Preço
    public void setPreco(double preco){
        if(preco > 0){
            this.preco = preco;
        }else{
            this.preco = 0;
        }
    }

    public double getPreco(){
        return preco;
    }

        //Fabricante
    public void setFabricante(String fabricante){
        if(!fabricante.isEmpty()){
            this.fabricante = fabricante;
        } else{
            this.fabricante = null;
        }
    }

    public String getFabricante(){
        return fabricante;
    }

    //Método Alterar
    public void Alterar(String nome, double preco, String fabricante){
        setNome(nome);
        setPreco(preco);
        setFabricante(fabricante);
    }

    //Método Deletar
    public void Deletar(){
        this.nome = null;
        this.preco = 0;
        this.fabricante = null;
    }

    //Método Pesquisar
    public void Pesquisar(String nome, String fabricante){
        if(this.nome.equalsIgnoreCase(nome) || this.fabricante.equalsIgnoreCase(fabricante)){
            System.out.println("Nome: " + this.nome);
            System.out.println("Preco: " + this.preco);
            System.out.println("Fabricante: " + this.fabricante);
        }
    }
}