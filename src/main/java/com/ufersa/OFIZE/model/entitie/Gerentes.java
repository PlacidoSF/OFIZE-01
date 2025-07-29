package com.ufersa.OFIZE.model.entitie;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="gerentes")
public class Gerentes extends Funcionarios {

    public Gerentes() {
        super();
    }

    public Gerentes(String usuario, String senha){
        super(usuario, senha);
    }
}