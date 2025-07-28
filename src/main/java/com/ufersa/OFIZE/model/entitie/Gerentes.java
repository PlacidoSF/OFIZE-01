
package com.ufersa.OFIZE.model.entitie;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="gerentes")
public class Gerentes extends Funcionarios {

    //Construtor vazio
    public Gerentes(){
    }
    //Construtor
    public Gerentes(String usuario, String senha){
        setId(usuario, senha);
    }

    private void setId(String usuario, String senha) {
    }
    //Os métodos alterar peça e alterar serviço serão implementados futuramente
}


