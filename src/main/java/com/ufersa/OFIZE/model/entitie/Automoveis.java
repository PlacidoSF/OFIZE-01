package com.ufersa.OFIZE.model.entitie;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Automoveis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String cor;
    private int ano;
    private int quilometragem;
   
    @ManyToOne
    @JoinColumn(name = "proprietario_id")
    private Clientes proprietario;

    public Automoveis() {} // Obrigatório para o Hibernate

    public Automoveis(String marca, String cor, int ano, int quilometragem, Clientes proprietario) {
        setMarca(marca);
        setCor(cor);
        setAno(ano);
        setQuilometragem(quilometragem);
        setProprietario(proprietario);
    }


    // GETTERS AND SETTERS
    // Marca
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        if (!getMarca().isEmpty() && getMarca() != null) {
                this.marca = marca;
      }else{
        this.marca = null;
      }    }

    //Cor
    
    public String getCor() {
        return cor;
    }
    public void setCor(String cor) {
        if (!getCor().isEmpty() && getCor() != null) {
                this.cor = cor;
      }else{
        this.cor = null;
      }    }

    // Ano

    public int getAno() {
        return ano;
    }
    public void setAno(int ano) {
        int anoAtual = java.time.LocalDate.now().getYear();
        if (getAno() > 1885 && getAno() <= anoAtual) {
            this.ano = ano;
        } else {
            this.ano = 0;
        }
    }

    // Quilometragem

    public double getQuilometragem() {
        return quilometragem;
    }
    public void setQuilometragem(int quilometragem) {
        if (quilometragem >= 0) {
            this.quilometragem = quilometragem;
        } else {
            this.quilometragem = 0;
        }
    }

    // Proprietario

    public Clientes getProprietario() {
        return proprietario;
    }
    public void setProprietario(Clientes proprietario) {
        if (proprietario.getNome() != null && !proprietario.getNome().isEmpty()) {
                this.proprietario = proprietario;
      }else{
        throw new IllegalArgumentException("Nome do proprietário é inválido");
      }    }

      // ID
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }


    }