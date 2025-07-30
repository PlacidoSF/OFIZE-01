package com.ufersa.OFIZE.model.entitie;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Id unico do orçamento

    private String nomeClienteString;
    private String veiculo;
    private double valorVeiculo; // Agora representa a "taxa mínima do veículo"
    private LocalDate data;
    private boolean status;
    private boolean pago;

    private double valorTotal; // NOVO CAMPO: Valor total do orçamento

    @ManyToOne
    private Clientes cliente;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoPeca> orcamentoPecas = new ArrayList<>();

    // Relacionamento OneToMany para OrcamentoServico
    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoServico> orcamentoServicos = new ArrayList<>();


    // Construtor padrão
    public Orcamento() {
        this.data = LocalDate.now();
        this.status = false;
        this.pago = false;
        this.valorTotal = 0.0;
    }

    public Orcamento(String veiculo, double valorVeiculo, Clientes cliente) {
        this();
        this.veiculo = veiculo;
        this.valorVeiculo = valorVeiculo;
        this.cliente = cliente;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVeiculo() { return veiculo; }
    public void setVeiculo(String veiculo) { this.veiculo = veiculo; }

    public double getValorVeiculo() { return valorVeiculo; }
    public void setValorVeiculo(double valorVeiculo) { this.valorVeiculo = valorVeiculo; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }

    public double getValorTotal() { return valorTotal; }


    public Clientes getCliente() { return cliente; }
    public void setCliente(Clientes cliente) { this.cliente = cliente; }

    public List<OrcamentoPeca> getOrcamentoPecas() { return orcamentoPecas; }
    public void setOrcamentoPecas(List<OrcamentoPeca> orcamentoPecas) {
        this.orcamentoPecas.clear();
        if (orcamentoPecas != null) {
            for (OrcamentoPeca op : orcamentoPecas) {
                addOrcamentoPeca(op);
            }
        }
        calcularValorTotal(); // Recalcular total ao definir as peças
    }

    public void addOrcamentoPeca(OrcamentoPeca orcamentoPeca) {
        this.orcamentoPecas.add(orcamentoPeca);
        orcamentoPeca.setOrcamento(this); // Garante a referência inversa
        calcularValorTotal(); // Recalcular total ao adicionar uma peça
    }

    public void removeOrcamentoPeca(OrcamentoPeca orcamentoPeca) {
        this.orcamentoPecas.remove(orcamentoPeca);
        orcamentoPeca.setOrcamento(null); // Remove a referência inversa
        calcularValorTotal(); // Recalcular total ao remover uma peça
    }

    public List<OrcamentoServico> getOrcamentoServicos() { return orcamentoServicos; }
    public void setOrcamentoServicos(List<OrcamentoServico> orcamentoServicos) {
        this.orcamentoServicos.clear();
        if (orcamentoServicos != null) {
            for (OrcamentoServico os : orcamentoServicos) {
                addOrcamentoServico(os);
            }
        }
        calcularValorTotal(); // Recalcular total ao definir os serviços
    }

    public void addOrcamentoServico(OrcamentoServico orcamentoServico) {
        this.orcamentoServicos.add(orcamentoServico);
        orcamentoServico.setOrcamento(this); // Garante a referência inversa
        calcularValorTotal(); // Recalcular total ao adicionar um serviço
    }

    public void removeOrcamentoServico(OrcamentoServico orcamentoServico) {
        this.orcamentoServicos.remove(orcamentoServico);
        orcamentoServico.setOrcamento(null); // Remove a referência inversa
        calcularValorTotal(); // Recalcular total ao remover um serviço
    }

    // Método para calcular o valor total do orçamento
    public void calcularValorTotal() {
        double totalPecas = orcamentoPecas.stream()
                .mapToDouble(op -> op.getQuantidade() * op.getValorUnitario())
                .sum();

        double totalServicos = orcamentoServicos.stream()
                .mapToDouble(os -> os.getQuantidade() * os.getValorUnitario())
                .sum();

        this.valorTotal = this.valorVeiculo + totalPecas + totalServicos;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orcamento orcamento = (Orcamento) o;
        return Objects.equals(id, orcamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}