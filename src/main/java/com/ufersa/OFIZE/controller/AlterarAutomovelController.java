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
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AlterarAutomovelController {

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
    private Automoveis automovelParaAlterar;

    @FXML
    public void initialize() {
        carregarProprietarios();
    }

    public void setAutomovelParaAlterar(Automoveis automovel) {
        this.automovelParaAlterar = automovel;
        if (automovel != null) {
            marcaField.setText(automovel.getMarca());
            corField.setText(automovel.getCor());
            placaField.setText(automovel.getPlaca());
            anoField.setText(String.valueOf(automovel.getAno()));
            quilometragemField.setText(String.valueOf(automovel.getQuilometragem()));
            proprietarioComboBox.setValue(automovel.getProprietario());
        }
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Alteração");
        alert.setHeaderText("Você tem certeza que deseja salvar as alterações?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                automovelParaAlterar.setMarca(marcaField.getText());
                automovelParaAlterar.setCor(corField.getText());
                automovelParaAlterar.setPlaca(placaField.getText());
                automovelParaAlterar.setAno(Integer.parseInt(anoField.getText()));
                automovelParaAlterar.setQuilometragem(Integer.parseInt(quilometragemField.getText()));
                automovelParaAlterar.setProprietario(proprietarioComboBox.getValue());

                automoveisService.atualizar(automovelParaAlterar);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Automóvel alterado com sucesso!");
                voltarParaPesquisa();

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Os campos 'Ano' e 'Quilometragem' devem ser números.");
            } catch (IllegalArgumentException e) {
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