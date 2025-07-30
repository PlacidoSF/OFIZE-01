package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Servico;
import javax.persistence.TypedQuery;
import java.util.List;


public class ServicoDAO extends DAOGenerico<Servico, Long> {


    // Construtor
    public ServicoDAO() {
        super(Servico.class);
    }

    public void persist(Servico servico) {
        super.persist(servico);
    }

    public Servico findById(Long id) {
        return super.findById(id);
    }

    public List<Servico> findAll() {
        return super.findAll();
    }

    public Servico merge(Servico servico) {
        super.merge(servico);
        return servico;
    }

    public void remove(Servico servico) {
        Servico servicoManaged = super.findById(servico.getId());
        if (servicoManaged != null) {
            super.remove(servicoManaged);
        } else {
            throw new IllegalArgumentException("Serviço não encontrado para remoção.");
        }
    }

    public List<Servico> findByNomeContaining(String nome) {
        TypedQuery<Servico> query = em.createQuery("SELECT s FROM Servico s WHERE LOWER(s.nome) LIKE :nome", Servico.class); // 'em' é acessível via protected da superclasse
        query.setParameter("nome", "%" + nome.toLowerCase() + "%");
        return query.getResultList();
    }

    @Override
    public void close() {
        super.close();
    }
}