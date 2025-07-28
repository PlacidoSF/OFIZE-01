package com.ufersa.OFIZE.model.dao;


import com.ufersa.OFIZE.model.entitie.Pecas;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery; // Importar para usar queries tipadas
import java.util.List;

public class PecasDAO {

    // Define o nome da unidade de persistência que você configurou no seu persistence.xml
    private static final String PERSISTENCE_UNIT_NAME = "OFIZE_PU"; // Substitua pelo nome real da sua PU

    // EntityManagerFactory deve ser criado uma única vez por aplicação
    private static EntityManagerFactory emf;

    // Bloco estático para inicializar o EntityManagerFactory
    static {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            System.err.println("Erro ao inicializar EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            // Lançar RuntimeException para falha crítica na inicialização
            throw new RuntimeException("Falha na inicialização do sistema de persistência.", e);
        }
    }

    // Método para obter um EntityManager. Ele deve ser criado por Thread/requisição e fechado.
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Método para persistir (salvar pela primeira vez) uma Peça
    public void persist(Pecas pecas) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pecas);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao persistir peça: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar peça no banco de dados.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Método para atualizar (merge) uma Peça existente
    public Pecas merge(Pecas pecas) {
        EntityManager em = getEntityManager();
        Pecas updatedPecas = null;
        try {
            em.getTransaction().begin();
            updatedPecas = em.merge(pecas);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao atualizar peça: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar peça no banco de dados.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return updatedPecas;
    }

    // Método para remover uma Peça
    public void remove(Pecas pecas) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // Para remover, a entidade precisa estar no estado managed.
            // Se a entidade veio de outra transação ou foi "detached", precisamos encontrá-la.
            Pecas pecasManaged = em.find(Pecas.class, pecas.getId());
            if (pecasManaged != null) {
                em.remove(pecasManaged);
            } else {
                throw new IllegalArgumentException("Peça não encontrada para remoção.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao remover peça: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover peça do banco de dados.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Método para buscar uma Peça pelo ID
    public Pecas findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pecas.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Método para buscar todas as Peças
    public List<Pecas> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Pecas> query = em.createQuery("SELECT p FROM Pecas p", Pecas.class);
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Método para buscar peças por nome OU fabricante (usado pelo PecasService)
    public List<Pecas> buscarPorNomeOufabricante(String nome, String fabricante) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT p FROM Pecas p WHERE 1=1");
            boolean hasNome = nome != null && !nome.trim().isEmpty();
            boolean hasFabricante = fabricante != null && !fabricante.trim().isEmpty();

            if (hasNome) {
                jpql.append(" AND LOWER(p.nome) LIKE :nome");
            }
            if (hasFabricante) {
                jpql.append(" AND LOWER(p.fabricante) LIKE :fabricante");
            }

            TypedQuery<Pecas> query = em.createQuery(jpql.toString(), Pecas.class);

            if (hasNome) {
                query.setParameter("nome", "%" + nome.toLowerCase() + "%");
            }
            if (hasFabricante) {
                query.setParameter("fabricante", "%" + fabricante.toLowerCase() + "%");
            }

            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}