package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.service.AutomoveisService;
import com.ufersa.OFIZE.model.service.ClientesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.io.IOException;
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
        carregarProprietarios();
    }

    private void carregarProprietarios() {
        List<Clientes> clientes = clientesService.buscarTodos();
        ObservableList<Clientes> observableClientes = FXCollections.observableArrayList(clientes);
        proprietarioComboBox.setItems(observableClientes);

        proprietarioComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Clientes cliente) {
                return cliente != null ? cliente.getNome() : "";
            }

            @Override
            public Clientes fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    void handleConfirmar(ActionEvent event) {
        try {
            String placa = placaField.getText();
            Clientes proprietario = proprietarioComboBox.getValue();

            Automoveis novoAutomovel = new Automoveis.Builder(placa, proprietario)
                    .marca(marcaField.getText())
                    .cor(corField.getText())
                    .ano(Integer.parseInt(anoField.getText()))
                    .quilometragem(Integer.parseInt(quilometragemField.getText()))
                    .build();

            automoveisService.cadastrar(novoAutomovel);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Automóvel cadastrado com sucesso!");
            voltarParaPesquisaAutomovel();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Os campos 'Ano' e 'Quilometragem' devem ser números válidos.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        voltarParaPesquisaAutomovel();
    }

    private void voltarParaPesquisaAutomovel() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/pesquisar_automovel.fxml"));
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