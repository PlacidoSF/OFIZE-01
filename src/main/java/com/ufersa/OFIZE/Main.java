package com.ufersa.OFIZE;

import com.ufersa.OFIZE.model.dao.AutomoveisDAO;
import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Servico;
import com.ufersa.OFIZE.model.service.ClientesService;
import com.ufersa.OFIZE.model.service.ServicoService;
import com.ufersa.OFIZE.model.service.AutomoveisService;
import com.ufersa.OFIZE.utils.DatabaseTest;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Main {
    public static void main(String[] args) {
        // Configurar DAO de automóveis
        AutomoveisService automoveisService = new AutomoveisService();
        ServicoService servicoService = new ServicoService();
        ClientesService clientesService = new ClientesService();

        // 1. Cadastrar automóvel (pré-requisito para serviços)
        Clientes proprietario = new Clientes("João Silva", "Rua Principal, 123", "111.222.333-44");
        //clientesService.cadastrarCliente(proprietario);
        Automoveis automovel = new Automoveis("Toyota", "Preto", 2015, 3000, proprietario);

        Automoveis automovelBuscado = automoveisService.buscarPorId(1L);
        System.out.println("🔍 Automóvel encontrado: " + automovelBuscado.getMarca());

        // 4. Atualizar automóvel
        automovelBuscado.setCor("Vermelho");
        automovelBuscado.setQuilometragem(18000);
        automoveisService.atualizar(automovelBuscado);
        System.out.println("🔄 Automóvel atualizado");
        // 2. Cadastrar serviço associado ao automóvel
        Servico novoServico = new Servico("Troca de Óleo", 150.0, automovel);
        //servicoService.cadastrarServico(novoServico);
        System.out.println("✅ Serviço cadastrado: " + novoServico.getNome());

        for(int i = 0; i < 100; i++) {
            System.out.println(i);
        }

    }
}
