package com.ufersa.OFIZE.model.dao;

import com.ufersa.OFIZE.model.entitie.Servico;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;


public class ServicoDAO {
    private static final String PERSISTENCE_UNIT_NAME = "ofize-pu"; // *** CONFIRME SE ESTE NOME BATE COM SEU persistenc
    private static EntityManagerFactory emf;


    static {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            System.err.println("Erro ao inicializar EntityManagerFactory para ServicoDAO: " + e.getMessage());
            e.printStackTrace();

            throw new RuntimeException("Falha na inicialização do sistema de persistência para Serviços.", e);
        }
    }

    // Método para obter um EntityManager.
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Salva um novo serviço.
    public void persist(Servico servico) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(servico);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao persistir serviço: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar serviço no banco de dados.", e);
        } finally {
            if (em != null) {
                em.close(); // *** FECHAR O ENTITYMANAGER É CRÍTICO ***
            }
        }
    }

    // Busca um serviço pelo ID.
    public Servico findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servico.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Lista todos os serviços.
    public List<Servico> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Servico> query = em.createQuery("SELECT s FROM Servico s", Servico.class);
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close(); // *** FECHAR O ENTITYMANAGER ***
            }
        }
    }

    // Atualiza um serviço existente.
    public Servico merge(Servico servico) {
        EntityManager em = getEntityManager();
        Servico updatedServico = null; // Inicializar a variável
        try {
            em.getTransaction().begin();
            updatedServico = em.merge(servico);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao atualizar serviço: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar serviço no banco de dados.", e);
        } finally {
            if (em != null) {
                em.close(); // *** FECHAR O ENTITYMANAGER ***
            }
        }
        return updatedServico; // Retorna a entidade gerenciada após o merge
    }

    // Remove um serviço.
    public void remove(Servico servico) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // Para remover, a entidade precisa estar no estado managed.
            // Se a entidade veio de outra transação ou foi "detached", precisamos encontrá-la.
            Servico servicoManaged = em.find(Servico.class, servico.getId());
            if (servicoManaged != null) {
                em.remove(servicoManaged);
            } else {
                // Se não for encontrado, significa que já foi removido ou nunca existiu
                throw new IllegalArgumentException("Serviço não encontrado para remoção.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao remover serviço: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover serviço do banco de dados.", e);
        } finally {
            if (em != null) {
                em.close(); // *** FECHAR O ENTITYMANAGER ***
            }
        }
    }

    // Adicionado: Método para buscar serviços por nome (parcialmente ou totalmente)
    // Este método é essencial para o ServicoService que você modificou antes.
    public List<Servico> findByNomeContaining(String nome) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Servico> query = em.createQuery("SELECT s FROM Servico s WHERE LOWER(s.nome) LIKE :nome", Servico.class);
            query.setParameter("nome", "%" + nome.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}