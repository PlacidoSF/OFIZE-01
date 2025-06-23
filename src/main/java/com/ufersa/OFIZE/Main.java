package com.ufersa.OFIZE;

import com.ufersa.OFIZE.model.dao.ClientesDAO;
import com.ufersa.OFIZE.model.dao.ServicoDAO;
import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Servico;
import com.ufersa.OFIZE.model.service.ClientesService;
import com.ufersa.OFIZE.model.service.ServicoService;
import com.ufersa.OFIZE.utils.DatabaseTest;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Main {
    public static void main(String[] args) {

        //---> TESTANDO A CONEXÃO COM O BANCO DE DADOS<---
        /*
        DatabaseTest teste = new DatabaseTest();
        teste.datatest();
        */

        // --->TESTANDO AS CLASSES CLIENTES, CLIENTESDAO E CLIENTESSERVICE<---
        /*
        // Configuração inicial
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ofize-pu");
        EntityManager em = emf.createEntityManager();
        //DatabaseTest teste1 = new DatabaseTest();
        //teste1.datatest()

        ClientesDAO dao = new ClientesDAO(em);
        ClientesService service = new ClientesService(dao);

        // CADASTRAR usando construtor da entidade

        Clientes novoCliente = new Clientes(
                "Ana Souza",
                "Av. Brasil, 1500",
                "987.654.321-00"
        );

        service.cadastrarCliente(novoCliente);
        // BUSCAR cliente
        Clientes clienteBuscado = service.buscarCliente("987.654.321-00");
        System.out.println("Cliente encontrado: " + clienteBuscado.getNome());

        // ALTERAR cliente
        clienteBuscado.setNome("Ana Carolina Souza");
        clienteBuscado.setEndereco("Av. Brasil, 1501");
        service.atualizarCliente(clienteBuscado);

        // LISTAR todos os clientes
        System.out.println("\nTodos os clientes:");
        for (Clientes c : dao.findAll()) {
            System.out.println("- " + c.getNome() + " | " + c.getCpf());
        }
        for(int i = 0; i < 100; i++) {
            System.out.println(1);
        }

        // DELETAR cliente
        service.removerCliente(clienteBuscado);
        System.out.println("\nCliente removido com sucesso!");
        // Fechar recursos
        em.close();
        emf.close();
         */


        ClientesService clientesService = new ClientesService();
        ServicoService servicoService = new ServicoService();


        for(int i = 0; i < 100; i++) {
            System.out.println(i);
        }
        System.out.println("===== CADASTRO DE CLIENTE =====");
         //Cadastrar cliente
        Clientes cliente = new Clientes("Maria Oliveira", "Av. Principal, 456", "111.222.333-44");
        //clientesService.cadastrarCliente(cliente);
        Servico servico1 = new Servico("Troca de Óleo", 150.0, cliente);
        //servicoService.cadastrarServico(servico1);
        System.out.println("✅ Serviço cadastrado: " + servico1.getNome() + " | ID: " + servico1.getId());


        System.out.println("\n===== ALTERAÇÃO DE SERVIÇO =====");
        // Alterar serviço - FORMA CORRETA: buscar o serviço primeiro
        Servico servicoParaAtualizar = servicoService.buscarServico(1L);
        servicoParaAtualizar.setNome("Revisão Completa Premium");
        servicoParaAtualizar.setValor(500.0);
        servicoService.atualizarServico(servicoParaAtualizar);
        System.out.println("🔄 Serviço atualizado: " + servicoParaAtualizar.getNome() + " | Novo valor: R$" + servicoParaAtualizar.getValor());


    }
}
