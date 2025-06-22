package com.ufersa.OFIZE.model.entitie;
public class Gerente {
    //Atributos
    private String usuario;
    private String senha;

    //Construtor
    public Gerente(String usuario, String senha){
        setUsuario(usuario);
        setSenha(senha);
    }

    //Métodos Getters e Setters
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
    public boolean Login(String usuario, String senha){
        if(usuario == this.usuario && senha == this.senha){
            return true;
        } else{
            return false;
        }
    }

    //Os métodos que alteram uma peça ou um serviço serão implementados futuramente
}
