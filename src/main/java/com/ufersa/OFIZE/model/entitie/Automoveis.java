package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Automoveis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String cor;
    private String placa;
    private int ano;
    private int quilometragem;

    @ManyToOne
    @JoinColumn(name = "proprietario_id")
    private Clientes proprietario;

    public Automoveis() {}

    public Automoveis(String marca, String cor, String placa, int ano, int quilometragem, Clientes proprietario) {
        this.marca = marca;
        this.cor = cor;
        this.placa = placa;
        this.ano = ano;
        this.quilometragem = quilometragem;
        this.proprietario = proprietario;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public int getQuilometragem() { return quilometragem; }
    public void setQuilometragem(int quilometragem) { this.quilometragem = quilometragem; }

    public Clientes getProprietario() { return proprietario; }
    public void setProprietario(Clientes proprietario) { this.proprietario = proprietario; }
}