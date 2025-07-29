package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Funcionarios;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class FuncionariosDAO extends FuncionarioDAOAbstract {

    public Funcionarios findByUsuario(String usuario) {
        try {
            TypedQuery<Funcionarios> query = em.createQuery("SELECT f FROM Funcionarios f WHERE f.usuario = :usuario", Funcionarios.class);
            query.setParameter("usuario", usuario);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}