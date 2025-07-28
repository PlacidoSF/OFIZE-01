package com.ufersa.OFIZE.model.entitie;

// Mude todas as importações de 'javax.persistence' para 'jakarta.persistence'
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "orcamento_pecas") // Ou o nome da sua tabela de associação
public class OrcamentoPeca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;

    @ManyToOne
    @JoinColumn(name = "peca_id")
    private Pecas peca; // Referência à entidade Pecas

    private int quantidade;
    private double valorUnitario; // Valor da peça no momento da inclusão no orçamento

    public OrcamentoPeca() {
    }

    // *** ADICIONE OU VERIFIQUE ESTE CONSTRUTOR ***
    public OrcamentoPeca(Orcamento orcamento, Pecas peca, int quantidade, double valorUnitario) {
        this.orcamento = orcamento;
        this.peca = peca;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Orcamento getOrcamento() { return orcamento; }
    public void setOrcamento(Orcamento orcamento) { this.orcamento = orcamento; }
    public Pecas getPeca() { return peca; }
    public void setPeca(Pecas peca) { this.peca = peca; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }

    // Método auxiliar para obter o valor total deste item de peça no orçamento
    public double getTotalItemValue() {
        return this.quantidade * this.valorUnitario;
    }
}