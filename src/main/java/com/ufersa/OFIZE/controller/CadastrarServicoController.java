package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Servico;
import com.ufersa.OFIZE.model.service.ServicoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CadastrarServicoController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField valorField;

    private ServicoService servicoService;

    public CadastrarServicoController() {
        this.servicoService = new ServicoService();
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();

        String nome = nomeField.getText();
        String valorStr = valorField.getText();

        if (nome.isEmpty() || valorStr.isEmpty()) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Validação ", "Todos os campos são obrigatórios.");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) {
                showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Validação", "O valor deve ser um número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Validação", "O preço deve ser um número válido.");
            return;
        }

        Servico novoServico = new Servico(nome, valor);

        try {

            servicoService.salvarServico(novoServico);
            showAlert(currentStage, Alert.AlertType.INFORMATION, "Sucesso", "Serviço cadastrado com sucesso!");
            clearFields();
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
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene currentScene = stage.getScene();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/ServicoView.fxml"));
            Parent servicoListView = loader.load();

            currentScene.setRoot(servicoListView);
            stage.setTitle("Pesquisar Serviços");


        } catch (IOException e) {
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