package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private double valor;
    private boolean status; // false: não finalizado, true: finalizado

    @ManyToOne
    @JoinColumn(name = "automovel_id")
    private Automoveis automovel;

    // Construtor padrão (obrigatório para JPA)
    public Servico() {
        this.status = false; // Status inicia como não finalizado
    }

    /**
     * Construtor para criação de novos serviços
     * @param nome Nome do serviço
     * @param valor Valor do serviço
     * @param automovel Automóvel associado ao serviço
     */
    public Servico(String nome, double valor, Automoveis automovel) {
        this.nome = nome;
        this.valor = valor;
        this.automovel = automovel;
        this.status = false; // Status inicia como não finalizado
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome != null && !nome.isEmpty()) {
            this.nome = nome;
        }
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if(valor > 0) {
            this.valor = valor;
        }
    }

    public boolean isStatus() {
        return status;
    }

    public Automoveis getAutomovel() {
        return automovel;
    }

    public void setAutomovel(Automoveis automovel) {
        if(automovel != null) {
            this.automovel = automovel;
        }
    }

    /**
     * Finaliza o serviço e registra o pagamento
     * @return true se o serviço foi finalizado com sucesso
     */
    public boolean finalizarERegistrarPagamento() {
        if(!this.status) {
            this.status = true;
            return true;
        }
        return false; // Já estava finalizado
    }
}