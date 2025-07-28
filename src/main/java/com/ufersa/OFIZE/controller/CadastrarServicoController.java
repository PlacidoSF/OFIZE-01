package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Servico;
import com.ufersa.OFIZE.model.service.ServicoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; // Manter para 'getScene()'
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
// Removido: import javafx.scene.input.KeyCombination; // Não é mais necessário aqui com setRoot()

import java.io.IOException;

public class CadastrarServicoController {

    @FXML
    private AnchorPane rootPane; // Usado para obter a Stage para os alertas e para a navegação

    @FXML
    private TextField nomeField;

    @FXML
    private TextField valorField;

    private ServicoService servicoService; // Instância do ServicoService

    public CadastrarServicoController() {
        this.servicoService = new ServicoService(); // Inicializa a instância
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        // Obtenha a Stage atual para ser o owner dos alertas
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        String nome = nomeField.getText();
        String valorStr = valorField.getText();

        if (nome.isEmpty() || valorStr.isEmpty()) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Validação", "Todos os campos são obrigatórios.");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) { // Alterado para valor <= 0, já que 0 não faz sentido para preço
                showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Validação", "O valor deve ser um número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Validação", "O preço deve ser um número válido.");
            return;
        }

        Servico novoServico = new Servico(nome, valor);

        try {
            // *** CORREÇÃO AQUI: Chamar o método na instância do servicoService ***
            servicoService.salvarServico(novoServico);
            showAlert(currentStage, Alert.AlertType.INFORMATION, "Sucesso", "Serviço cadastrado com sucesso!");
            clearFields();
            // Retorne para ServicoView após o cadastro bem-sucedido
            returnToServicoListView(event);
        } catch (IllegalArgumentException e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro ao Cadastrar", "Ocorreu um erro ao cadastrar o serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        returnToServicoListView(event);
    }

    private void returnToServicoListView(ActionEvent event) {
        try {
            // Obtenha a Stage e a Scene atual
            Stage stage = (Stage) rootPane.getScene().getWindow(); // Obtenha a Stage através de um nó FXML
            Scene currentScene = stage.getScene(); // Obtenha a cena ATUAL da Stage

            // Carregue o FXML da tela de Serviços. CONFIRME O NOME EXATO DO ARQUIVO FXML.
            // Para consistência, usando 'ServicoView.fxml'.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/ServicoView.fxml"));
            Parent servicoListView = loader.load();

            // *** CORREÇÃO AQUI: Use setRoot() para trocar o conteúdo da cena existente ***
            currentScene.setRoot(servicoListView);
            stage.setTitle("Pesquisar Serviços");

            // NÃO PRECISA MAIS CHAMAR setFullScreen() AQUI.
            // A Stage já deve estar em tela cheia se foi configurada no Main.java.
            // As propriedades de FullScreen (ExitHint, ExitKeyCombination) são da Stage e permanecem.
            // stage.setFullScreen(true); // REMOVA ESTA LINHA
            // stage.setFullScreenExitHint("Pressione ESC para sair da tela cheia"); // REMOVA ESTA LINHA
            // stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("ESC")); // REMOVA ESTA LINHA
            // stage.show(); // NÃO PRECISA DE show() novamente, a stage já está visível

            // Opcional: Se ServicoViewController precisar ser inicializado/atualizado ao retornar
            // ServicoViewController servicoController = loader.getController();
            // if (servicoController != null) {
            //     servicoController.initialize(); // Ou um método de atualização, se initialize() for chamado uma única vez
            // }

        } catch (IOException e) {
            // Se houver erro ao carregar a tela, ainda mostre o alerta na stage atual.
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de pesquisa de serviços.");
            e.printStackTrace();
        }
    }

    private void showAlert(Stage owner, Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (owner != null) {
            alert.initOwner(owner);
        } else {
            System.err.println("AVISO: Stage owner do alerta é nula. O alerta pode não ser exibido corretamente.");
        }
        alert.showAndWait();
    }

    private void clearFields() {
        nomeField.clear();
        valorField.clear();
    }
}