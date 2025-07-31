package com.ufersa.OFIZE;

import com.ufersa.OFIZE.model.entitie.Funcionarios;
import com.ufersa.OFIZE.utils.DataInitializer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    private static Funcionarios usuarioLogado;

    public static Funcionarios getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Funcionarios usuario) {
        Main.usuarioLogado = usuario;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        DataInitializer.criarGerentePadraoSeNaoExistir();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/ufersa/OFIZE/view/tela_login.fxml")));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("OFIZE - Gestão");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Lança a aplicação JavaFX
    }
}