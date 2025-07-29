package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.service.AutomoveisService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PesquisarAutomovelController {

    @FXML private TextField searchField;
    @FXML private VBox automoveisContainer;
    @FXML private Button cadastrarAutomovelButton;
    @FXML private VBox menuHamburguer;

    private final AutomoveisService automoveisService = new AutomoveisService();

    @FXML
    public void initialize() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarAutomoveis(newValue);
        });
        menuHamburguer.setOnMouseClicked(this::handleVoltarAoMenu);
        buscarAutomoveis("");
    }

    private void handleVoltarAoMenu(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/menu.fxml"));
            Scene scene = menuHamburguer.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buscarAutomoveis(String textoBusca) {
        automoveisContainer.getChildren().clear();
        List<Automoveis> automoveisEncontrados = automoveisService.pesquisar(textoBusca);
        for (Automoveis automovel : automoveisEncontrados) {
            automoveisContainer.getChildren().add(criarLinhaAutomovel(automovel));
        }
    }

    private HBox criarLinhaAutomovel(Automoveis automovel) {
        String nomeProprietario = (automovel.getProprietario() != null) ? automovel.getProprietario().getNome() : "N/A";

        Label proprietarioLabel = new Label(nomeProprietario);
        proprietarioLabel.setPrefWidth(250.0);
        proprietarioLabel.getStyleClass().add("client-data");

        Label placaLabel = new Label(automovel.getPlaca());
        placaLabel.setPrefWidth(150.0);
        placaLabel.getStyleClass().add("client-data");
        placaLabel.setAlignment(Pos.CENTER);

        Label marcaLabel = new Label(automovel.getMarca());
        marcaLabel.setPrefWidth(300.0);
        marcaLabel.getStyleClass().add("client-data");

        Button editButton = new Button();
        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/alterar.png")));
        editIcon.setFitHeight(20);
        editIcon.setFitWidth(20);
        editButton.setGraphic(editIcon);
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(event -> abrirTelaDeAlteracao(automovel));

        Button deleteButton = new Button();
        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/deletar.png")));
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(20);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> deletarAutomovel(automovel));

        HBox botoesBox = new HBox(10, editButton, deleteButton);
        botoesBox.setAlignment(Pos.CENTER);
        botoesBox.setPrefWidth(100.0);

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        HBox linha = new HBox(proprietarioLabel, placaLabel, marcaLabel, espacador, botoesBox);
        linha.getStyleClass().add("client-row");
        return linha;
    }

    private void deletarAutomovel(Automoveis automovel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Você realmente quer apagar este Automóvel?");
        alert.setContentText(automovel.getMarca() + " - Placa: " + automovel.getPlaca());

        ButtonType simButton = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
        ButtonType naoButton = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(simButton, naoButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == simButton) {
            automoveisService.deletar(automovel);
            buscarAutomoveis(searchField.getText());
        }
    }

    private void abrirTelaDeAlteracao(Automoveis automovel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/alterar_automovel.fxml"));
            Parent root = loader.load();

            AlterarAutomovelController controller = loader.getController();
            controller.setAutomovelParaAlterar(automovel);

            Scene scene = searchField.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCadastrarAutomovel() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/cadastrar_automovel.fxml"));
            Scene scene = cadastrarAutomovelButton.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}