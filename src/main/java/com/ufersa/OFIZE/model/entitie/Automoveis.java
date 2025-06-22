package com.ufersa.OFIZE.model.entitie;
public class Automoveis {
    
    // Atributos

    private String marca;
    private String cor;
    private int ano;
    private int quilometragem;
    private String proprietario;

    // GETTERS AND SETTERS
    // Marca
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        if (!getMarca().isEmpty()) {
                this.marca = marca;
      }else{
        this.marca = null;
      }    }

    //Cor
    
    public String getCor() {
        return cor;
    }
    public void setCor(String cor) {
        if (!getCor().isEmpty()) {
                this.cor = cor;
      }else{
        this.cor = null;
      }    }

    // Ano

    public int getAno() {
        return ano;
    }
    public void setAno(int ano) {
        if (ano > 1885 && ano <= 2025) {
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

    public String getProprietario() {
        return proprietario;
    }
    public void setProprietario(String proprietario) {
        if (!getProprietario().isEmpty()) {
                this.proprietario = proprietario;
      }else{
        this.proprietario = "sem proprietario";
      }    }

    // Construtor
    public Automoveis(String marca, String cor, int ano, int quilometragem, String proprietario) {
        setMarca(marca);
        setCor(cor);
        setAno(ano);
        setQuilometragem(quilometragem);
        setProprietario(proprietario); //foi considerado que o proprietario nao precisa ter cadastro (estar na lista de clientes), o proprietario precisa ter cadastro?
    }

    // metodo alterar

    public void Alterar(String marca, String cor, int ano, int quilometragem, String proprietario) {
        setMarca(marca);
        setCor(cor);
        setAno(ano);
        setQuilometragem(quilometragem);
        setProprietario(proprietario);
    }

    // metodo Deletar
    // Deletar apenas limpando os atributos pois ainda nao existe uma lista de automoveis para que possa deletar o objeto dela
    public void Deletar() {
        this.marca = null;
        this.cor = null;
        this.ano = 0;
        this.quilometragem = 0;
        this.proprietario = null;
    }

    // Metodo Pesquisar por placa e/ou por proprietario

    public void Pesquisar(String marca, String proprietario) {
        if (this.marca.equalsIgnoreCase(marca) || this.proprietario.equalsIgnoreCase(proprietario)) {
            System.out.println("Marca: " + this.marca);
            System.out.println("Cor: " + this.cor);
            System.out.println("Ano: " + this.ano);
            System.out.println("Quilometragem: " + this.quilometragem);
            System.out.println("Proprietário: " + this.proprietario);
        } else {
            System.out.println("Veículo não encontrado.");
        }
    }

   

}
