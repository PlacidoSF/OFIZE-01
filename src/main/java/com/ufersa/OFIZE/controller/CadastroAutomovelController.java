package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.service.AutomoveisService;
import com.ufersa.OFIZE.model.service.ClientesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;

public class CadastroAutomovelController {

    @FXML private TextField marcaField;
    @FXML private TextField corField;
    @FXML private TextField placaField;
    @FXML private TextField anoField;
    @FXML private TextField quilometragemField;
    @FXML private ComboBox<Clientes> proprietarioComboBox;
    @FXML private Button confirmarButton;
    @FXML private Button cancelarButton;

    private final AutomoveisService automoveisService = new AutomoveisService();
    private final ClientesService clientesService = new ClientesService();

    @FXML
    public void initialize() {
        // Carrega os clientes do banco e os adiciona ao ComboBox
        carregarProprietarios();
    }

    private void carregarProprietarios() {
        List<Clientes> clientes = clientesService.buscarTodos();
        ObservableList<Clientes> observableClientes = FXCollections.observableArrayList(clientes);
        proprietarioComboBox.setItems(observableClientes);

        // Define como o nome do cliente será exibido na lista
        proprietarioComboBox.setConverter(new StringConverter<Clientes>() {
            @Override
            public String toString(Clientes cliente) {
                return cliente != null ? cliente.getNome() : "";
            }

            @Override
            public Clientes fromString(String string) {
                return null; // Não precisamos converter de volta
            }
        });
    }

    @FXML
    void handleConfirmar(ActionEvent event) {
        try {
            // Coleta os dados da tela
            String marca = marcaField.getText();
            String cor = corField.getText();
            String placa = placaField.getText();
            // Converte ano e quilometragem para números, tratando possíveis erros
            int ano = Integer.parseInt(anoField.getText());
            int quilometragem = Integer.parseInt(quilometragemField.getText());
            Clientes proprietario = proprietarioComboBox.getValue();

            // Cria o objeto Automovel
            Automoveis novoAutomovel = new Automoveis(marca, cor, placa, ano, quilometragem, proprietario);

            // Tenta cadastrar usando o serviço, que fará as validações
            automoveisService.cadastrar(novoAutomovel);

            // Se o cadastro for bem-sucedido, mostra um alerta de sucesso
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Automóvel cadastrado com sucesso!");
            limparCampos();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Os campos 'Ano' e 'Quilometragem' devem ser números válidos.");
        } catch (IllegalArgumentException e) {
            // Captura os erros de validação do nosso serviço (placa duplicada, campos vazios, etc.)
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    private void limparCampos() {
        marcaField.clear();
        corField.clear();
        placaField.clear();
        anoField.clear();
        quilometragemField.clear();
        proprietarioComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
