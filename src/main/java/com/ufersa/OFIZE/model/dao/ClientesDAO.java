package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Clientes;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class ClientesDAO extends ClienteDAOAbstract {

    public Clientes findByCpf(String cpf) {
        try {
            TypedQuery<Clientes> query = em.createQuery("SELECT c FROM Clientes c WHERE c.cpf = :cpf", Clientes.class);
            query.setParameter("cpf", cpf);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Clientes> searchByName(String name) {
        TypedQuery<Clientes> query = em.createQuery("SELECT c FROM Clientes c WHERE c.nome LIKE :name", Clientes.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }
}