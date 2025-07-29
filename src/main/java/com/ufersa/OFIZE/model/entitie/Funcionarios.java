package com.ufersa.OFIZE.model.entitie;

import javax.persistence.*;

@Entity
@Table(name="funcionarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Funcionarios {

    @Id
    private String usuario;
    private String senha;

    public Funcionarios() {}

    public Funcionarios(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    // Getters e Setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}