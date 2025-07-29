package com.ufersa.OFIZE.model.dao;


import com.ufersa.OFIZE.model.entitie.Pecas;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class PecasDao {

    private static final String PERSISTENCE_UNIT_NAME = "ofize-pu";
    private static EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            System.err.println("Erro ao inicializar EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha na inicialização do sistema de persistência.", e);
        }
    }

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

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

    public Pecas merge(Pecas pecas) {
        EntityManager em = getEntityManager();
        Pecas updatedPecas = null;
        try {
            em.getTransaction().begin();
            updatedPecas = em.merge(pecas);
            em.getTransaction().commit();
        }
        catch (Exception e) {
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

    public void remove(Pecas pecas) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
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

    // *** VERSÃO CORRIGIDA DO MÉTODO DE PESQUISA POR NOME OU FABRICANTE ***
    public List<Pecas> buscarPorNomeOufabricante(String nome, String fabricante) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT p FROM Pecas p WHERE ");
            boolean firstCondition = true; // Ajuda a gerenciar AND/OR

            String trimmedNome = (nome != null) ? nome.trim().toLowerCase() : "";
            String trimmedFabricante = (fabricante != null) ? fabricante.trim().toLowerCase() : "";

            if (!trimmedNome.isEmpty()) {
                jpql.append("LOWER(p.nome) LIKE :nome");
                firstCondition = false;
            }

            if (!trimmedFabricante.isEmpty()) {
                if (!firstCondition) { // Se já adicionamos a condição de nome, use OR
                    jpql.append(" OR ");
                }
                jpql.append("LOWER(p.fabricante) LIKE :fabricante");
                firstCondition = false; // Não é mais a primeira condição
            }

            // Se nenhum critério foi fornecido (o que PecasService já trata com findAll,
            // mas é bom ter uma fallback ou condição para evitar query vazia "WHERE ")
            if (firstCondition) {
                // Isso deve ser tratado pelo PecasService que chama findAll()
                // Mas, como fallback ou se essa função for chamada diretamente com ambos vazios,
                // podemos adicionar uma condição que sempre é verdadeira ou retornar uma lista vazia.
                // Idealmente, esta parte nunca será alcançada se PecasService.pesquisar() estiver correto.
                jpql.append("1=1"); // Adiciona uma condição sempre verdadeira se não houver critérios
            }

            TypedQuery<Pecas> query = em.createQuery(jpql.toString(), Pecas.class);

            if (!trimmedNome.isEmpty()) {
                query.setParameter("nome", "%" + trimmedNome + "%");
            }
            if (!trimmedFabricante.isEmpty()) {
                query.setParameter("fabricante", "%" + trimmedFabricante + "%");
            }

            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}