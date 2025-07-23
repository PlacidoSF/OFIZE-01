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
import java.util.List;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Caminho absoluto garantido
            URL fxmlUrl = getClass().getResource("/com/ufersa/OFIZE/Exemplo.fxml");

            // Verificação robusta
            if(fxmlUrl == null) {
                // Mensagem de erro detalhada
                System.err.println("ERRO: Arquivo FXML não encontrado.");
                System.err.println("Procurando em: /com/ufersa/OFIZE/Exemplo.fxml");
                System.err.println("Classpath: " + System.getProperty("java.class.path"));
                System.err.println("Diretório atual: " + System.getProperty("user.dir"));

                // Tentativa alternativa de diagnóstico
                try {
                    System.err.println("Conteúdo do diretório view:");
                    java.nio.file.Path path = java.nio.file.Paths.get(
                            getClass().getResource("/com/ufersa/OFIZE").toURI()
                    );
                    java.nio.file.Files.list(path).forEach(System.err::println);
                } catch (Exception e) {
                    System.err.println("Falha ao listar diretório: " + e.getMessage());
                }

                throw new RuntimeException("FXML não encontrado após mover para resources");
            }

            System.out.println("SUCESSO: FXML encontrado em: " + fxmlUrl);

            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);

            primaryStage.setTitle("OFIZE - Sistema de Gerenciamento");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        System.out.println("[INFO] Iniciando aplicação OFIZE...");
        launch(args);
    }
}


/*
public class Main {

    public static void main(String[] args) {


        // Configurar DAO de automóveis
        AutomoveisService automoveisService = new AutomoveisService();
        ServicoService servicoService = new ServicoService();
        ClientesService clientesService = new ClientesService();

        // 1. Cadastrar automóvel (pré-requisito para serviços)
        Clientes proprietario = new Clientes("João Silva", "Rua Principal, 123", "111.222.333-44");
        Clientes proprietario1 = new Clientes("Plácido", "Upanema", "130.877.094-41");
        //clientesService.cadastrarCliente(proprietario);
        clientesService.cadastrarCliente(proprietario1);
        Automoveis automovel = new Automoveis("Toyota", "Preto", 2015, 3000, proprietario);
        Automoveis automovel1 = new Automoveis("Toyota", "vermelho", 2030, 3050, proprietario1);
        automoveisService.cadastrar(automovel1);

        Automoveis automovelBuscado = automoveisService.buscarPorId(1L);
        System.out.println("🔍 Automóvel encontrado: " + automovelBuscado.getMarca());

        // 4. Atualizar automóvel
        automovelBuscado.setCor("Vermelho");
        automovelBuscado.setQuilometragem(18000);
        //automoveisService.atualizar(automovelBuscado);
        System.out.println("🔄 Automóvel atualizado");
        // 2. Cadastrar serviço associado ao automóvel
        Servico novoServico = new Servico("Troca de Óleo", 150.0, automovel);
        //servicoService.cadastrarServico(novoServico);
        System.out.println("✅ Serviço cadastrado: " + novoServico.getNome());

        for(int i = 0; i < 100; i++) {
            System.out.println(i);
        }
        System.out.println("\n🔎 Resultados da pesquisa:");
        List<Automoveis> resultados = automoveisService.pesquisar("Toyota", "João Silva");
        for (Automoveis auto : resultados) {
            System.out.println("- " + auto.getMarca() + " " + auto.getCor() +
                    " | Proprietário: " + auto.getProprietario().getNome());
        }
        for(int i = 0; i < 100; i++) {
            System.out.println(i);
        }
    }
}
 */