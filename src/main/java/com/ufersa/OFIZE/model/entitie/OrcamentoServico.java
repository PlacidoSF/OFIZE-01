// src/main/java/com/ufersa/OFIZE/model/entitie/OrcamentoServico.java
package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "orcamento_servicos") // Ou o nome da sua tabela de associação
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

    private int quantidade;
    private double valorUnitario; // Valor do serviço no momento da inclusão no orçamento

    // Construtor padrão (obrigatório para JPA)
    public OrcamentoServico() {
    }

    // *** ADICIONE OU VERIFIQUE ESTE CONSTRUTOR ***
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
}