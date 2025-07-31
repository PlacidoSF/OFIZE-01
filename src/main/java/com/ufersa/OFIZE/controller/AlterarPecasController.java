package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Pecas;
import com.ufersa.OFIZE.model.service.PecasService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AlterarPecasController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField fabricanteField;
    @FXML
    private TextField precoField;
    @FXML
    private TextField quantidadeField;
    @FXML
    private Button confirmarButton;
    @FXML
    private Button cancelarButton;

    private PecasService pecasService;
    private Pecas pecaToAlter; // A peça que será alterada

    public AlterarPecasController() {
        this.pecasService = new PecasService();
    }

    public void setPeca(Pecas peca) {
        this.pecaToAlter = peca;
        if (pecaToAlter != null) {
            nomeField.setText(pecaToAlter.getNome());
            fabricanteField.setText(pecaToAlter.getFabricante());
            precoField.setText(String.valueOf(pecaToAlter.getPreco()));
            quantidadeField.setText(String.valueOf(pecaToAlter.getQuantidade()));
        }
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        if (pecaToAlter == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Nenhuma peça selecionada para alteração.");
            return;
        }

        try {
            pecaToAlter.setNome(nomeField.getText());
            pecaToAlter.setFabricante(fabricanteField.getText());
            pecaToAlter.setPreco(Double.parseDouble(precoField.getText()));
            pecaToAlter.setQuantidade(Integer.parseInt(quantidadeField.getText()));

            pecasService.atualizarPeca(pecaToAlter); // Chama o método de atualização do serviço

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Peça alterada com sucesso!");
            returnToPecasView(event); // Retorna para a tela de listagem de peças
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Por favor, insira valores válidos para preço e quantidade.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Alterar Peça", "Ocorreu um erro ao tentar alterar a peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        returnToPecasView(event);
    }

    private void returnToPecasView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/PecasView.fxml"));
            Parent pecasView = loader.load();

            Scene scene = ((Button) event.getSource()).getScene();
            scene.setRoot(pecasView);

            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Lista de Peças");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de peças: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage owner = (Stage) nomeField.getScene().getWindow();
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }
}
