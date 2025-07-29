package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Funcionarios;
import com.ufersa.OFIZE.model.service.FuncionariosService;
import com.ufersa.OFIZE.model.service.GerentesService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

public class CadastroFuncionarioController {

    @FXML private TextField novoUsuarioField;
    @FXML private PasswordField novaSenhaField;
    @FXML private Button cadastrarButton;
    @FXML private Button voltarButton;

    private final FuncionariosService funcionariosService = new FuncionariosService();
    private final GerentesService gerentesService = new GerentesService();

    @FXML
    void handleCadastrar(ActionEvent event) {
        Optional<Pair<String, String>> result = showAdminLoginDialog();

        result.ifPresent(credentials -> {
            String adminUser = credentials.getKey();
            String adminPass = credentials.getValue();

            if (gerentesService.login(adminUser, adminPass) != null) {
                try {
                    String novoUsuario = novoUsuarioField.getText();
                    String novaSenha = novaSenhaField.getText();
                    Funcionarios novoFuncionario = new Funcionarios(novoUsuario, novaSenha);

                    funcionariosService.cadastrarFuncionario(novoFuncionario);

                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário cadastrado com sucesso!");
                    handleVoltar(null);

                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro de Cadastro", e.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Autenticação Falhou", "Usuário ou senha do gerente inválidos.");
            }
        });
    }

    @FXML
    void handleVoltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/tela_login.fxml"));
            Scene scene = voltarButton.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<Pair<String, String>> showAdminLoginDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Autenticação do Gerente");
        dialog.setHeaderText("Para cadastrar um novo funcionário, por favor, insira as credenciais do gerente.");

        ButtonType loginButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Usuário");
        PasswordField password = new PasswordField();
        password.setPromptText("Senha");

        grid.add(new Label("Usuário:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Senha:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(username::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}