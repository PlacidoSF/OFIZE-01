package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private double valor;


    @ManyToOne
    @JoinColumn(name = "automovel_id")
    private Automoveis automovel;

    public Servico() {

    }

    // Construtor para novos serviços.
    public Servico(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome != null && !nome.isEmpty()) {
            this.nome = nome;
        }
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if(valor > 0) {
            this.valor = valor;
        }
    }


}