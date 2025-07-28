package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.service.ClientesService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CadastroClienteController {

    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField enderecoField;
    @FXML private Button confirmarButton;
    @FXML private Button cancelarButton;

    private final ClientesService clientesService = new ClientesService();

    @FXML
    void handleConfirmar(ActionEvent event) {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String endereco = enderecoField.getText();
        Clientes novoCliente = new Clientes(nome, endereco, cpf);

        try {
            clientesService.cadastrarCliente(novoCliente);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cliente cadastrado com sucesso!");
            voltarParaPesquisa();

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        voltarParaPesquisa();
    }

    private void voltarParaPesquisa() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/pesquisar_cliente.fxml"));
            Scene scene = confirmarButton.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}