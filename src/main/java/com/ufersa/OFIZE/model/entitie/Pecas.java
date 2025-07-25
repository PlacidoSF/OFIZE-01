package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="pecas")
public class Pecas {
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private double preco;
    private String fabricante;

    //Construtor vazio
    public Pecas(){
    }

    //Construtor
    public Pecas(String nome, double preco, String fabricante){
        setNome(nome);
        setPreco(preco);
        setFabricante(fabricante);
    }

    //Métodos Getters e Setters
    //ID
    public void setId(Long id){
        if(id > 0){
            this.id = id;
        }
    }

    public Long getId(){
        return id;
    }

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

}
