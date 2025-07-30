package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Servico;
import com.ufersa.OFIZE.model.service.ServicoService;
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

public class AlterarServicoController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField valorField;
    @FXML
    private Button confirmarButton;
    @FXML
    private Button cancelarButton;

    private ServicoService servicoService;
    private Servico servicoParaAlterar;

    public AlterarServicoController() {
        this.servicoService = new ServicoService();
    }

    public void setServico(Servico servico) {
        this.servicoParaAlterar = servico;
        if (servico != null) {
            if (nomeField != null) {
                nomeField.setText(servico.getNome());
            }
            if (valorField != null) {
                valorField.setText(String.valueOf(servico.getValor()));
            }
        }
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        if (servicoParaAlterar == null) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro", "Nenhum serviço selecionado para alteração.");
            return;
        }

        try {
            servicoParaAlterar.setNome(nomeField.getText());
            servicoParaAlterar.setValor(Double.parseDouble(valorField.getText()));

            servicoService.atualizarServico(servicoParaAlterar);
            showAlert(currentStage, Alert.AlertType.INFORMATION, "Sucesso", "Serviço alterado com sucesso!");

            returnToServicoListView(event);

        } catch (NumberFormatException e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Entrada", "O valor do serviço deve ser um número válido.");
        } catch (IllegalArgumentException e) {
            showAlert(currentStage, Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro ao alterar serviço ", "Não foi possível alterar o serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        returnToServicoListView(event);
    }

    // MÉTODO ATUALIZADO
    private void returnToServicoListView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/ServicoView.fxml"));
            Parent servicoListView = loader.load();

            // Pega a cena ATUAL em vez de criar uma nova
            Scene scene = ((Button) event.getSource()).getScene();
            // Apenas troca o conteúdo da cena, preservando o tamanho da janela
            scene.setRoot(servicoListView);

            // Opcional: Garante que o título da janela seja o correto
            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Pesquisar Serviços");

        } catch (IOException e) {
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
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
}