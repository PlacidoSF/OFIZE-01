package com.ufersa.OFIZE.model.entitie;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Importar Objects para o equals/hashCode

@Entity
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Id unico do orçamento

    private String veiculo;
    private double valorVeiculo; // Agora representa a "taxa mínima do veículo"
    private LocalDate data;
    private boolean status;
    private boolean pago;

    private double valorTotal; // NOVO CAMPO: Valor total do orçamento

    @ManyToOne
    private Clientes cliente;

    // Relacionamento OneToMany para OrcamentoPeca
    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoPeca> orcamentoPecas = new ArrayList<>();

    // Relacionamento OneToMany para OrcamentoServico
    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoServico> orcamentoServicos = new ArrayList<>();


    // CONSTRUTORES

    // Construtor padrão
    public Orcamento() {
        this.data = LocalDate.now();
        this.status = false;
        this.pago = false;
        this.valorTotal = 0.0; // Inicializa com 0
    }

    // Construtor com parâmetros atualizado
    public Orcamento(String veiculo, double valorVeiculo, List<OrcamentoPeca> orcamentoPecas,
                     List<OrcamentoServico> orcamentoServicos, Clientes cliente) {
        this(); // Chama o construtor padrão para inicializar data, status, pago e valorTotal
        setVeiculo(veiculo);
        setValorVeiculo(valorVeiculo); // Define a taxa mínima do veículo
        setCliente(cliente);
        setOrcamentoPecas(orcamentoPecas);
        setOrcamentoServicos(orcamentoServicos);
        // O valorTotal será calculado no controller ou em um método à parte
        calcularValorTotal(); // Chame este método após definir todas as listas
    }


    // GETTERS AND SETTERS

    // Valor Total
    public double getValorTotal() {
        return valorTotal;
    }

    // O setter para valorTotal deve ser privado ou não existir, pois ele é calculado.
    // Ou pode ser usado para persistência, mas a lógica de cálculo deve vir de outro lugar.
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Método para calcular o valor total (pode ser chamado no Service ou Controller)
    public void calcularValorTotal() {
        double totalPecas = orcamentoPecas.stream()
                .mapToDouble(op -> op.getQuantidade() * op.getValorUnitario())
                .sum();
        double totalServicos = orcamentoServicos.stream()
                .mapToDouble(os -> os.getQuantidade() * os.getValorUnitario())
                .sum();
        this.valorTotal = this.valorVeiculo + totalPecas + totalServicos;
    }


    // Métodos existentes (mantidos, mas com observações)

    public String getVeiculo() { return veiculo; }
    public void setVeiculo(String veiculo) { this.veiculo = veiculo; }

    public double getValorVeiculo() { return valorVeiculo; } // Taxa mínima do veículo
    public void setValorVeiculo(double valorVeiculo) {
        this.valorVeiculo = valorVeiculo;
        // Recalcular total se a taxa mínima mudar
        calcularValorTotal();
    }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }

    public Clientes getCliente() { return cliente; }
    public void setCliente(Clientes cliente) {
        if (cliente != null && cliente.getNome() != null && !cliente.getNome().isEmpty()) {
            this.cliente = cliente;
        } else {
            this.cliente = null;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // OrcamentoPecas
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


    // OrcamentoServicos
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

    // Opcional: Adicionar equals e hashCode para melhor funcionamento com coleções e persistência
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