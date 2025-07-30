package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Transient; // Importar para propriedades não persistidas

@Entity
@Table(name = "orcamento_servicos")
public class OrcamentoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;

    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico; // Referência à entidade Servico

    private int quantidade; // Este será sempre 1 para serviços (lógica nos controllers)
    private double valorUnitario; // Valor do serviço no momento da inclusão no orçamento


    public OrcamentoServico() {
    }

    // Construtor completo
    public OrcamentoServico(Orcamento orcamento, Servico servico, int quantidade, double valorUnitario) {
        this.orcamento = orcamento;
        this.servico = servico;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Orcamento getOrcamento() { return orcamento; }
    public void setOrcamento(Orcamento orcamento) { this.orcamento = orcamento; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }

    // Método auxiliar para obter o valor total deste item de serviço no orçamento
    public double getTotalItemValue() {
        return this.quantidade * this.valorUnitario;
    }


    @Transient // Indica que esta propriedade não é persistida no banco de dados
    public String getNomeServico() {
        return (this.servico != null) ? this.servico.getNome() : "";
    }


    @Transient // Indica que esta propriedade não é persistida no banco de dados
    public double getValorTotalServico() {
        // Para serviços, a quantidade será sempre 1 de acordo com o requisito,
        // então getTotalItemValue() já reflete o valor unitário * 1.
        return getTotalItemValue();
    }
}