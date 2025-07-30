package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.Main;
import com.ufersa.OFIZE.model.entitie.Gerentes;
import com.ufersa.OFIZE.model.entitie.Pecas;
import com.ufersa.OFIZE.model.service.PecasService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PecasViewController implements Initializable {

    @FXML private TextField searchField;
    @FXML private VBox pecasContainer;
    @FXML private Button newPecaButton;
    @FXML private VBox menuHamburguer;

    private PecasService pecasService;
    private List<Pecas> allPecas;

    public PecasViewController() {
        this.pecasService = new PecasService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPecas("");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> loadPecas(newValue));

        menuHamburguer.setOnMouseClicked(this::handleVoltarAoMenu);

        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            if (newPecaButton != null) {
                newPecaButton.setDisable(true);
            }
        }
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

    private void loadPecas(String searchTerm) {
        pecasContainer.getChildren().clear();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            allPecas = pecasService.buscarTodasPecas();
        } else {
            allPecas = pecasService.buscarPecasPorNomeOuFabricante(searchTerm, searchTerm);
        }

        if (allPecas.isEmpty()) {
            Label noResultsLabel = new Label("Nenhuma peça encontrada.");
            pecasContainer.getChildren().add(noResultsLabel);
        } else {
            for (Pecas peca : allPecas) {
                HBox pecaItem = createPecaItem(peca);
                pecasContainer.getChildren().add(pecaItem);
            }
        }
    }

    private HBox createPecaItem(Pecas peca) {
        HBox hbox = new HBox(10);
        hbox.setStyle("-fx-padding: 10px; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label nomeLabel = new Label(peca.getNome());
        nomeLabel.setPrefWidth(200);
        Label fabricanteLabel = new Label(peca.getFabricante());
        fabricanteLabel.setPrefWidth(150);
        Label precoLabel = new Label(String.format("R$ %.2f", peca.getPreco()));
        precoLabel.setPrefWidth(100);
        precoLabel.setAlignment(javafx.geometry.Pos.CENTER);
        Label quantidadeLabel = new Label(String.valueOf(peca.getQuantidade()));
        quantidadeLabel.setPrefWidth(100);
        quantidadeLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editButton = new Button();
        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Imagens/alterar.png"))));
        editIcon.setFitHeight(20);
        editIcon.setFitWidth(20);
        editButton.setGraphic(editIcon);
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(event -> handleEditPeca(peca));

        Button deleteButton = new Button();
        ImageView deleteIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Imagens/deletar.png"))));
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(20);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> handleDeletePeca(peca));

        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }

        hbox.getChildren().addAll(nomeLabel, fabricanteLabel, precoLabel, quantidadeLabel, spacer, editButton, deleteButton);
        return hbox;
    }

    @FXML
    private void handleNewPeca(ActionEvent event) {
        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            showAlert(null, Alert.AlertType.ERROR, "Acesso Negado", "Apenas gerentes podem cadastrar peças.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/CadastrarPecas.fxml"));
            Parent cadastrarPecaView = loader.load();
            Scene scene = ((Button) event.getSource()).getScene();
            scene.setRoot(cadastrarPecaView);
            ((Stage) scene.getWindow()).setTitle("Cadastrar Peça");
        } catch (IOException e) {
            showAlert(null, Alert.AlertType.ERROR, "Erro ao abrir tela", "Não foi possível carregar a tela de cadastro de peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleEditPeca(Pecas peca) {
        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            showAlert(null, Alert.AlertType.ERROR, "Acesso Negado", "Apenas gerentes podem alterar peças.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/alterar_pecaVIEW.fxml"));
            Parent alterarPecasView = loader.load();
            AlterarPecasController controller = loader.getController();
            controller.setPeca(peca);
            Scene scene = pecasContainer.getScene();
            scene.setRoot(alterarPecasView);
            ((Stage) scene.getWindow()).setTitle("Alterar Peça");
        } catch (IOException e) {
            showAlert(null, Alert.AlertType.ERROR, "Erro ao abrir tela", "Não foi possível carregar a tela de alteração de peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeletePeca(Pecas peca) {
        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            showAlert(null, Alert.AlertType.ERROR, "Acesso Negado", "Apenas gerentes podem excluir peças.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Exclusão");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Tem certeza que deseja excluir a peça: " + peca.getNome() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    pecasService.removerPeca(peca);
                    loadPecas("");
                    showAlert(null, Alert.AlertType.INFORMATION, "Sucesso", "Peça excluída com sucesso!");
                } catch (Exception e) {
                    showAlert(null, Alert.AlertType.ERROR, "Erro", "Erro ao excluir peça: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAlert(Stage owner, Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }
}