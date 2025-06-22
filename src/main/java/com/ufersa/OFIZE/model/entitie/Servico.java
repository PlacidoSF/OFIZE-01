//import java.util.ArrayList;
//import java.util.List;
package com.ufersa.OFIZE.model.entitie;
public class Servico {

    // Atributos
    private String nome;
    private double valor;
    private boolean status;
    //private List<Clientes> cliente;

    //construtor || cadastrar 
    public Servico(String nome, double valor) {
        setNome(nome);
        setValor(valor);
        this.status = false;

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

    //Set e Get: valor
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if(valor > 0) {
            this.valor = valor;
        } else {
            valor = 0;
        }
    }

    // Get: Status
    public boolean getStatus() {
        return status;
    }

    //Deletar
    public void deletar() {
        this.nome = null;
        this.valor = 0;
        this.status = false;
    }

    //pesquisar por cliente
    public void pesquisarCliente() {
        //Ainda não tenho o conhecimento necessário para fazer esse método
        System.out.println("Aqui está o seu cliente/Serviço.");
    }

    //pesquisar por Automóvel/placa
    public void pesquisarPLaca() {
        //Ainda não tenho o conhecimento necessário para fazer esse método
        System.out.println("Aqui está o Automóvel/Serviço.");
    }

    //Finalizar
    public boolean finalizar() {
        this.status = true;
        return status;
    }

    //Registrar Pagamento
    public void registarPagamento() {
        if(finalizar()) {
            // Ainda não tenho o conhecimento necessário para fazer esse método
            System.out.println("O serviço foi registrado");
        } else {
            System.out.println("O serviço ainda não pode ser registrado.");
        }
    }

}