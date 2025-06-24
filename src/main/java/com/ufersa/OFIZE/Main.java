package com.ufersa.OFIZE;

import com.ufersa.OFIZE.model.entitie.*;
import com.ufersa.OFIZE.model.service.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        for(int i = 0; i < 20; i++) {
            System.out.println(i);
        }
        /*
        // Exemplos para ClientesService
        ClientesService clienteService = new ClientesService();

        // 1. Cadastrar cliente
        Clientes novoCliente = new Clientes("João Silva", "Rua das Flores, 123", "123.456.789-00");
        try {
            clienteService.cadastrarCliente(novoCliente);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao cadastrar cliente: " + e.getMessage());
        }

        // 2. Buscar cliente por CPF
        Clientes clienteEncontrado = clienteService.buscarCliente("123.456.789-00");
        System.out.println("Cliente encontrado: " + (clienteEncontrado != null ? clienteEncontrado.getNome() : "Não encontrado"));

        // 3. Atualizar cliente
        if (clienteEncontrado != null) {
            clienteEncontrado.setEndereco("Avenida Principal, 456");
            try {
                clienteService.atualizarCliente(clienteEncontrado);
                System.out.println("Cliente atualizado com sucesso!");
            } catch (IllegalArgumentException e) {
                System.err.println("Erro ao atualizar cliente: " + e.getMessage());
            }
        }

        // 4. Remover cliente
        try {
            clienteService.removerCliente(clienteEncontrado);
            System.out.println("Cliente removido com sucesso!");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao remover cliente: " + e.getMessage());
        }

        // Exemplos para AutomoveisService
        AutomoveisService automovelService = new AutomoveisService();

        // Primeiro precisamos de um cliente para ser proprietário
        Clientes proprietario = new Clientes("Maria Souza", "Rua dos Carros, 789", "987.654.321-00");
        clienteService.cadastrarCliente(proprietario);

        // 1. Cadastrar automóvel
        Automoveis novoAutomovel = new Automoveis("Ford", "Vermelho", 2020, 50000, proprietario);
        automovelService.cadastrar(novoAutomovel);

        // 2. Buscar todos automóveis
        List<Automoveis> todosAutomoveis = automovelService.buscarTodos();
        System.out.println("Total de automóveis cadastrados: " + todosAutomoveis.size());

        // 3. Buscar automóvel por ID
        Automoveis autoPorId = automovelService.buscarPorId(novoAutomovel.getId());
        System.out.println("Automóvel por ID: " + (autoPorId != null ? autoPorId.getMarca() : "Não encontrado"));

        // 4. Pesquisar automóveis por marca ou proprietário
        List<Automoveis> resultadosPesquisa = automovelService.pesquisar("Ford", "Maria");
        System.out.println("Resultados da pesquisa: " + resultadosPesquisa.size());

        // 5. Atualizar automóvel
        novoAutomovel.setCor("Azul");
        automovelService.atualizar(novoAutomovel);
        System.out.println("Cor do automóvel atualizada para: " + novoAutomovel.getCor());

        // 6. Deletar automóvel
        automovelService.deletar(novoAutomovel);
        System.out.println("Automóvel deletado");

        // Exemplos para ServicoService
        ServicoService servicoService = new ServicoService();

        // Cadastrar automóvel para o serviço
        Automoveis autoParaServico = new Automoveis("Chevrolet", "Prata", 2018, 80000, proprietario);
        automovelService.cadastrar(autoParaServico);

        // 1. Cadastrar serviço
        Servico novoServico = new Servico("Troca de óleo", 150.0, autoParaServico);
        servicoService.cadastrarServico(novoServico);
        System.out.println("Serviço cadastrado: " + novoServico.getNome());

        // 2. Buscar serviço por ID
        Servico servicoEncontrado = servicoService.buscarServico(novoServico.getId());
        System.out.println("Serviço encontrado: " + (servicoEncontrado != null ? servicoEncontrado.getNome() : "Não encontrado"));

        // 3. Atualizar serviço
        if (servicoEncontrado != null) {
            servicoEncontrado.setValor(180.0);
            servicoService.atualizarServico(servicoEncontrado);
            System.out.println("Valor do serviço atualizado para: " + servicoEncontrado.getValor());
        }

        // 4. Finalizar serviço e registrar pagamento
        boolean finalizado = servicoService.finalizarERegistrarPagamento(novoServico.getId());
        System.out.println("Serviço finalizado: " + finalizado);

        // 5. Buscar serviços por CPF do proprietário
        List<Servico> servicosDoProprietario = servicoService.buscarServicosPorProprietarioCpf("987.654.321-00");
        System.out.println("Serviços do proprietário: " + servicosDoProprietario.size());

        // 6. Remover serviço
        servicoService.removerServico(novoServico.getId());
        System.out.println("Serviço removido");

         */
    }
}