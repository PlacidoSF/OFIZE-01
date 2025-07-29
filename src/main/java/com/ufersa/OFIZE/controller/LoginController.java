package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.service.FuncionariosService;
import com.ufersa.OFIZE.model.service.GerentesService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usuarioField;
    @FXML private PasswordField senhaField;
    @FXML private Button loginButton;
    @FXML private Button cadastrarFuncionarioButton;

    private final GerentesService gerentesService = new GerentesService();
    private final FuncionariosService funcionariosService = new FuncionariosService();

    @FXML
    void handleLogin(ActionEvent event) {
        String usuario = usuarioField.getText();
        String senha = senhaField.getText();

        if (gerentesService.login(usuario, senha) != null || funcionariosService.login(usuario, senha) != null) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Login realizado com sucesso!");
            navigateTo("/com/ufersa/OFIZE/view/menu.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário ou senha inválidos.");
        }
    }

    @FXML
    void handleCadastrarFuncionario(ActionEvent event) {
        navigateTo("/com/ufersa/OFIZE/view/cadastrar_funcionario.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = loginButton.getScene();
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