package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="funcionarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Funcionarios{
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFunc;
    private String usuario;
    private String senha;

    //Construtor vazio obrigatorio
    public Funcionarios(){
    }
    //Construtor
    public Funcionarios(String usuario, String senha){
        setUsuario(usuario);
        setSenha(senha);
    }

    //Métodos Getters e Setters
        //id_Funcionario
    public void setIdFunc(int idFunc){
        if(idFunc > 0){
            this.idFunc = idFunc;
        }
    }

    public int getIdFunc(){
        return idFunc;
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
        return "Funcionario [ID: " + idFunc + ", Usuario: " + usuario + ", Senha: " + senha + "]";
    }
        
}
