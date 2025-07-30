package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MenuController {

    @FXML private Button automoveisButton;
    @FXML private Button clientesButton;
    @FXML private Button financasButton;
    @FXML private Button orcamentosButton;
    @FXML private Button pecasButton;
    @FXML private Button servicosButton;
    @FXML private VBox logoutMenu;

    @FXML
    public void initialize() {
        logoutMenu.setOnMouseClicked(this::handleLogout);
        // Lógica de desabilitar botões foi removida, como solicitado.
    }

    @FXML
    void handleNavegar(ActionEvent event) {
        Object source = event.getSource();
        String fxmlPath = null;

        if (source == clientesButton) {
            fxmlPath = "/com/ufersa/OFIZE/view/pesquisar_cliente.fxml";
        } else if (source == automoveisButton) {
            fxmlPath = "/com/ufersa/OFIZE/view/pesquisar_automovel.fxml";
        } else if (source == pecasButton) {
            fxmlPath = "/com/ufersa/OFIZE/view/PecasView.fxml";
        } else if (source == servicosButton) {
            fxmlPath = "/com/ufersa/OFIZE/view/ServicoView.fxml";
        } else if (source == orcamentosButton) {
            fxmlPath = "/com/ufersa/OFIZE/view/OrcamentoView.fxml";
        }

        if (fxmlPath != null) {
            navigateTo(fxmlPath, (Button) source);
        }
    }

    void handleLogout(MouseEvent event) {
        Main.setUsuarioLogado(null); // Limpa a sessão ao fazer logout
        navigateTo("/com/ufersa/OFIZE/view/tela_login.fxml", logoutMenu);
    }

    private void navigateTo(String fxmlPath, javafx.scene.Node node) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = node.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}