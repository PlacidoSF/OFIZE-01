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

    private Automoveis(Builder builder) {
        this.marca = builder.marca;
        this.cor = builder.cor;
        this.placa = builder.placa;
        this.ano = builder.ano;
        this.quilometragem = builder.quilometragem;
        this.proprietario = builder.proprietario;
    }

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

    public static class Builder {
        private final String placa;
        private final Clientes proprietario;
        private String marca = "";
        private String cor = "";
        private int ano = 0;
        private int quilometragem = 0;

        public Builder(String placa, Clientes proprietario) {
            this.placa = placa;
            this.proprietario = proprietario;
        }

        public Builder marca(String marca) {
            this.marca = marca;
            return this;
        }

        public Builder cor(String cor) {
            this.cor = cor;
            return this;
        }

        public Builder ano(int ano) {
            this.ano = ano;
            return this;
        }

        public Builder quilometragem(int quilometragem) {
            this.quilometragem = quilometragem;
            return this;
        }

        public Automoveis build() {
            return new Automoveis(this);
        }
    }
}