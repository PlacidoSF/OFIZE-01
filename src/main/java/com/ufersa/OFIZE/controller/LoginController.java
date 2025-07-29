package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.Main;
import com.ufersa.OFIZE.model.entitie.Funcionarios; // Importar Funcionarios
import com.ufersa.OFIZE.model.entitie.Gerentes;     // Importar Gerentes
import com.ufersa.OFIZE.model.service.FuncionariosService;
import com.ufersa.OFIZE.model.service.GerentesService; // Importar GerentesService
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

        // Tenta logar como gerente primeiro
        Gerentes gerenteLogado = gerentesService.login(usuario, senha);
        if (gerenteLogado != null) {
            Main.setUsuarioLogado(gerenteLogado); // <--- ADICIONE ESTA LINHA
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Login realizado com sucesso!");
            navigateTo("/com/ufersa/OFIZE/view/ServicoView.fxml");
            return; // Importante para não tentar logar como funcionário se já é gerente
        }

        // Se não for gerente, tenta como funcionário
        Funcionarios funcionarioLogado = funcionariosService.login(usuario, senha);
        if (funcionarioLogado != null) {
            Main.setUsuarioLogado(funcionarioLogado); // <--- ADICIONE ESTA LINHA
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Login realizado com sucesso!");
            navigateTo("/com/ufersa/OFIZE/view/OrcamentoView.fxml");
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
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela: " + fxmlPath);
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