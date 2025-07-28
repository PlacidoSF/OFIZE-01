
package com.ufersa.OFIZE.model.entitie;

import javax.persistence.*;

@Entity
@Table(name="funcionarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Funcionarios{
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private String senha;

    //Construtor vazio obrigatorio
    public Funcionarios(){
    }

    //Construtor
    public Funcionarios(Long id, String usuario, String senha) {

    }

    private void setIdFunc(Long id) {
    }

    //Métodos Getters e Setters
        //id_Funcionario
    public void setId(Long id){
        if(id > 0){
            this.id = id;
        }
    }

    public Long getId(){
        return id;
    }

        //Usuário
    public void setUsuario(String usuario){
        if(!usuario.isEmpty()){
            this.usuario = usuario;
        } else{
            this.usuario = null;
        }
    }

    public String getUsuario(){
        return usuario;
    }

        //Senha
    public void setSenha(String senha){
        if(!senha.isEmpty()){
            this.senha = senha;
        } else{
            this.senha = null;
        }
    }

    public String getSenha(){
        return senha;
    }

    //Método Login
    public boolean login(String usuario, String senha){
        if(usuario == null || senha == null){
            return false;
        }
        return this.usuario.equals(usuario) && this.senha.equals(senha);
    }

    //Escreve como uma string o objeto funcionario
    @Override
    public String toString(){
        return "Funcionario [ID: " + id + ", Usuario: " + usuario + ", Senha: " + senha + "]";
    }

}

