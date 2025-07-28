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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AlterarClienteController {

    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField enderecoField;
    @FXML private Button confirmarButton;
    @FXML private Button cancelarButton;

    private final ClientesService clientesService = new ClientesService();
    private Clientes clienteParaAlterar;

    // Este método será chamado pelo controller de pesquisa para passar os dados do cliente
    public void setClienteParaAlterar(Clientes cliente) {
        this.clienteParaAlterar = cliente;
        // Preenche os campos da tela com os dados atuais do cliente
        if (cliente != null) {
            nomeField.setText(cliente.getNome());
            cpfField.setText(cliente.getCpf());
            enderecoField.setText(cliente.getEndereco());
        }
    }

    @FXML
    void handleConfirmar(ActionEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmação de Alteração");
        confirmationAlert.setHeaderText("Você realmente quer alterar os dados deste cliente?");

        ButtonType simButton = new ButtonType("Sim");
        ButtonType naoButton = new ButtonType("Não");
        confirmationAlert.getButtonTypes().setAll(simButton, naoButton);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == simButton) {
            // Atualiza o objeto cliente com os novos dados dos campos
            clienteParaAlterar.setNome(nomeField.getText());
            clienteParaAlterar.setCpf(cpfField.getText());
            clienteParaAlterar.setEndereco(enderecoField.getText());

            try {
                // Tenta atualizar usando o serviço (que já tem as validações de CPF)
                clientesService.atualizarCliente(clienteParaAlterar);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dados do cliente alterados com sucesso!");
                voltarParaPesquisa();

            } catch (IllegalArgumentException e) {
                // Mostra um erro se a validação do serviço falhar (ex: CPF duplicado)
                showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
            }
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        voltarParaPesquisa();
    }

    private void voltarParaPesquisa() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/pesquisar_cliente.fxml"));
            Scene scene = confirmarButton.getScene(); // Pega a cena atual
            scene.setRoot(root); // Substitui o conteúdo da cena
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