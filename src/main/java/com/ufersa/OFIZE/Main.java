package com.ufersa.OFIZE;

import com.ufersa.OFIZE.model.entitie.Funcionarios;
import com.ufersa.OFIZE.model.entitie.Gerentes;
import com.ufersa.OFIZE.utils.DataInitializer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    // Adicione esta variável estática para armazenar o usuário logado
    private static Funcionarios usuarioLogado;

    // Método para obter o usuário logado
    public static Funcionarios getUsuarioLogado() {
        return usuarioLogado;
    }

    // Método para definir o usuário logado
    public static void setUsuarioLogado(Funcionarios usuario) {
        Main.usuarioLogado = usuario;
    }

    @Override
    public void start(Stage primaryStage) throws Exception { // Use 'throws Exception' para cobrir o DataInitializer

        DataInitializer.criarGerentePadraoSeNaoExistir();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/ufersa/OFIZE/view/tela_login.fxml")));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("OFIZE - Gestão"); // Título apropriado
        primaryStage.setMaximized(true); // Maximiza a janela principal
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Lança a aplicação JavaFX
    }
}