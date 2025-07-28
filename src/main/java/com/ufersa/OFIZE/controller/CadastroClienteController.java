package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.service.ClientesService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroClienteController {

    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField enderecoField;
    @FXML private Button confirmarButton;
    @FXML private Button cancelarButton;

    // Instância do nosso serviço que contém as regras de negócio.
    private final ClientesService clientesService = new ClientesService();

    @FXML
    void handleConfirmar(ActionEvent event) {
        // 1. Coleta os dados da tela
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String endereco = enderecoField.getText();

        // 2. Cria um objeto Cliente com os dados
        Clientes novoCliente = new Clientes(nome, endereco, cpf);

        try {
            // 3. Tenta cadastrar o cliente usando o serviço
            clientesService.cadastrarCliente(novoCliente); //

            // 4. Se tudo der certo, mostra um alerta de sucesso
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cliente cadastrado com sucesso!");
            limparCampos();

        } catch (IllegalArgumentException e) {
            // 5. Se o serviço lançar uma exceção (CPF já existe, campo vazio, etc.),
            // mostra um alerta de erro com a mensagem da exceção.
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        // Pega a janela atual a partir de qualquer botão e a fecha.
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    // Método auxiliar para limpar os campos após o cadastro
    private void limparCampos() {
        nomeField.clear();
        cpfField.clear();
        enderecoField.clear();
    }

    // Método auxiliar para criar e exibir alertas
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}