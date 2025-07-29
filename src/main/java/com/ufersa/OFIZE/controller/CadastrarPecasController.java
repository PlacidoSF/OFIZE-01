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

public class CadastrarPecasController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField precoField;
    @FXML
    private TextField fabricanteField;
    @FXML
    private TextField quantidadeField;
    @FXML
    private Button confirmarButton; // Você pode querer um fx:id para este no FXML
    @FXML
    private Button cancelarButton;  // E para este também

    private PecasService pecasService;

    public CadastrarPecasController() {
        this.pecasService = new PecasService();
    }

    @FXML
    public void initialize() {
        // Inicialização, se houver
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        try {
            String nome = nomeField.getText();
            double preco = Double.parseDouble(precoField.getText());
            String fabricante = fabricanteField.getText();
            int quantidade = Integer.parseInt(quantidadeField.getText());

            Pecas novaPeca = new Pecas(nome, preco, fabricante, quantidade);
            pecasService.cadastrarPeca(novaPeca); // Chama o serviço para cadastrar

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Peça cadastrada com sucesso!");

            // Limpa os campos após o cadastro, antes de voltar
            limparCampos();

            // *** Voltar para a tela PecasView após sucesso ***
            returnToPecasView(event);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Por favor, insira valores válidos para preço e quantidade.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Cadastrar Peça", "Ocorreu um erro ao tentar cadastrar a peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        // *** Voltar para a tela PecasView ao cancelar ***
        returnToPecasView(event);
    }

    private void limparCampos() {
        nomeField.clear();
        precoField.clear();
        fabricanteField.clear();
        quantidadeField.clear();
    }

    // *** Novo método para retornar à PecasView ***
    private void returnToPecasView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/PecasView.fxml"));
            Parent pecasView = loader.load();

            // Obtém a Scene atual a partir do botão que disparou o evento
            Scene scene = ((Button) event.getSource()).getScene();
            // Define o novo conteúdo para a Scene existente
            scene.setRoot(pecasView);

            // Opcional: Atualiza o título da janela (Stage)
            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Lista de Peças"); // Ou o título que você preferir para a lista
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

        // Tenta obter a Stage para definir o owner do alerta, evitando que ele flutue
        Stage owner = null;
        if (nomeField != null && nomeField.getScene() != null) {
            owner = (Stage) nomeField.getScene().getWindow();
        }
        if (owner != null) {
            alert.initOwner(owner);
        } else {
            System.err.println("AVISO: A janela proprietária do alerta é nula. O alerta pode não ser exibido corretamente.");
        }
        alert.showAndWait();
    }
}