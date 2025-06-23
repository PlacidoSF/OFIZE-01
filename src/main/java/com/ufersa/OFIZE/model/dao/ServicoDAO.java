package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Servico;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class ServicoDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
    private final EntityManager em = emf.createEntityManager();

    public ServicoDAO() {
    }

    /**
     * Persiste um novo serviço no banco de dados
     * @param servico Serviço a ser persistido
     */
    public void persist(Servico servico) {
        em.getTransaction().begin();
        em.persist(servico);
        em.getTransaction().commit();
    }

    /**
     * Busca um serviço pelo ID
     * @param id ID do serviço
     * @return Serviço encontrado ou null
     */
    public Servico findById(Long id) {
        return em.find(Servico.class, id);
    }

    /**
     * Retorna todos os serviços cadastrados
     * @return Lista de todos os serviços
     */
    public List<Servico> findAll() {
        TypedQuery<Servico> query = em.createQuery("SELECT s FROM Servico s", Servico.class);
        return query.getResultList();
    }

    /**
     * Busca serviços associados a um cliente
     * @param cpf CPF do cliente
     * @return Lista de serviços do cliente
     */
    public List<Servico> findByClienteCpf(String cpf) {
        TypedQuery<Servico> query = em.createQuery(
                "SELECT s FROM Servico s WHERE s.cliente.cpf = :cpf", Servico.class);
        query.setParameter("cpf", cpf);
        return query.getResultList();
    }

    /**
     * Atualiza um serviço existente
     * @param servico Serviço com dados atualizados
     */
    public void merge(Servico servico) {
        em.getTransaction().begin();
        em.merge(servico);
        em.getTransaction().commit();
    }

    /**
     * Remove um serviço do banco de dados
     * @param servico Serviço a ser removido
     */
    public void remove(Servico servico) {
        em.getTransaction().begin();
        em.remove(servico);
        em.getTransaction().commit();
    }

}
