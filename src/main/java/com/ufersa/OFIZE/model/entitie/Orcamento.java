package com.ufersa.OFIZE.model.entitie;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;



@Entity
public class Orcamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Id unico do orçamento

   
    private String veiculo;
    private double valorVeiculo;
    private LocalDate data;
    private boolean status;

    @ManyToOne
    private Clientes cliente;

    @ManyToMany
    private List<Pecas> pecas; 

    @ManyToMany
    private List<Servico> servicos; 


    // GETTERS AND SETTERS

    //Pecas

    public List<Pecas> getPecas() {
        return pecas;
    }

    public void setPecas(List<Pecas> pecas) {
        if (pecas == null || pecas.isEmpty()) {
            this.pecas = new ArrayList<>();
        } else {
            this.pecas = pecas;
        }
    }

    //Servicos

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        if (servicos == null || servicos.isEmpty()) {
            this.servicos = new ArrayList<>();
        } else {
            this.servicos = servicos;
        }
    }

    //Veiculo

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        if (veiculo != null && !veiculo.isEmpty()) {
            this.veiculo = veiculo;
        } else {
            this.veiculo = null;
        }
    }

    //Valor Veiculo

    public double getValorVeiculo() {
        return valorVeiculo;
    }

    public void setValorVeiculo(double valorVeiculo) {
        if (valorVeiculo > 0) {
            this.valorVeiculo = valorVeiculo;
        } else {
            this.valorVeiculo = 0;
        }
    }

    //Data

    public LocalDate getData() {
        return data;
    }

    //Status

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    //Cliente

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        if (cliente != null && cliente.getNome() != null && !cliente.getNome().isEmpty()) {
            this.cliente = cliente;
        } else {
            this.cliente = null;
        }
    }

    //ID

    public Long getId() {
        return id;
}

    public void setId(Long id) {
        this.id = id;
    }


    //Construtor

    public Orcamento() {
        this.data = LocalDate.now();
        this.status = false;
    }

    public Orcamento(String veiculo, double valorVeiculo , List<Pecas> pecas, List<Servico> servicos, Clientes cliente) {
        setPecas(pecas);
        setServicos(servicos);
        this.data = LocalDate.now();
        this.status = false;
        setVeiculo(veiculo);
        setValorVeiculo(valorVeiculo);
        setCliente(cliente);}

       
}