package com.ufersa.OFIZE;

import com.ufersa.OFIZE.utils.DataInitializer; // Importar a nova classe
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // ATUALIZADO: Chama a classe de inicialização para criar o gerente, se necessário.
        DataInitializer.criarGerentePadraoSeNaoExistir();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/ufersa/OFIZE/view/pesquisar_automovel.fxml")));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("OFIZE - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}