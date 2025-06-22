package com.ufersa.OFIZE.model.entitie;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Orcamento {
    /*
    private List<Pecas> pecas; //lista de pecas da classe pecas (a comunicacao entre as classes ainda nao implementada)
    private List<Servico> servicos; //lista de servicos da classe servicos (a comunicacao entre as classes ainda nao implementada)
    private String veiculo;
    private double valorVeiculo;
    private LocalDate data;
    private boolean status;
    private List<Clientes> cliente;

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


    //Construtor

    public Orcamento(String veiculo, double valorVeiculo , List<Peca> pecas, List<Servico> servicos) {
        setPecas(pecas);
        setServicos(servicos);
        this.data = LocalDate.now();
        this.status = false;
        setVeiculo(veiculo);
        setValorVeiculo(valorVeiculo); }

    
    //Métodos

    // Alterar

    public void Alterar (String veiculo, double valorVeiculo, boolean status, List<Peca> pecas, List<Servico> servicos) {
        setVeiculo(veiculo);
        setValorVeiculo(valorVeiculo);
        setStatus(status);
        setPecas(pecas);
        setServicos(servicos);
    }

    // Deletar
    // Deletar apenas limpando os atributos pois ainda nao existe uma lista de orcamentos para que possa deletar o objeto dela
    
    public void Deletar() {
        this.pecas = null;
        this.servicos = null;
        this.veiculo = null;
        this.valorVeiculo = 0;
        this.data = null;
        this.status = false;
    }

    // Pesquisar por veiculo e/ou por periodo e/ou por cliente
    // Cliente nao pertence a esta classe, mas a ideia é que exista uma lista de clientes e haja comunicacao entre as classes

    public void Pesquisar(String veiculo, List<Clientes> cliente, LocalDate dataInicio, LocalDate dataFim) {
        boolean veiculoOk = veiculo == null || this.veiculo.equalsIgnoreCase(veiculo);
        boolean clienteOk = cliente == null || this.cliente.equalsIgnoreCase(cliente);
        boolean dataOk = true;

        if (dataInicio != null && this.data.isBefore(dataInicio)) {
            dataOk = false;
        }
        if (dataFim != null && this.data.isAfter(dataFim)) {
            dataOk = false;
        }

        if (veiculoOk && clienteOk && dataOk) {
            System.out.println("Veículo: " + getVeiculo());
            System.out.println("Cliente: " + getCliente());
            System.out.println("Data: " + getData());
            System.out.println("Valor do veículo: " + getValorVeiculo());
            System.out.println("Pecas: " + getPecas());
            System.out.println("Servicos: " + getServicos());
            System.out.println("Status: " + (this.status ? "Pago" : "Não pago"));
        } else {
            System.out.println("Orçamento não encontrado.");
        }


}
     */
}