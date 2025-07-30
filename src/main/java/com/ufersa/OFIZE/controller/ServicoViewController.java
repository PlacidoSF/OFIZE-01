package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.Main;
import com.ufersa.OFIZE.model.entitie.Gerentes;
import com.ufersa.OFIZE.model.entitie.Servico;
import com.ufersa.OFIZE.model.service.ServicoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ServicoViewController implements Initializable {

    @FXML private TextField searchField;
    @FXML private VBox servicosContainer;
    @FXML private Button newServiceButton;
    @FXML private VBox menuHamburguer; // Novo campo

    private ServicoService servicoService;
    private List<Servico> allServicos;

    public ServicoViewController() {
        this.servicoService = new ServicoService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadServicos("");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> loadServicos(newValue));

        // Adiciona a ação de clique para voltar ao menu
        menuHamburguer.setOnMouseClicked(this::handleVoltarAoMenu);

        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            if (newServiceButton != null) {
                newServiceButton.setDisable(true);
            }
        }
    }

    // NOVO MÉTODO
    private void handleVoltarAoMenu(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/menu.fxml"));
            Scene scene = menuHamburguer.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadServicos(String searchTerm) {
        servicosContainer.getChildren().clear();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            allServicos = servicoService.listarTodosServicos();
        } else {
            allServicos = servicoService.buscarServicoPorNome(searchTerm);
        }

        if (allServicos.isEmpty()) {
            Label noResultsLabel = new Label("Nenhum serviço encontrado.");
            servicosContainer.getChildren().add(noResultsLabel);
        } else {
            for (Servico servico : allServicos) {
                HBox servicoItem = createServicoItem(servico);
                servicosContainer.getChildren().add(servicoItem);
            }
        }
    }

    private HBox createServicoItem(Servico servico) {
        HBox hbox = new HBox(10);
        hbox.setStyle("-fx-padding: 10px; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label nomeLabel = new Label(servico.getNome());
        nomeLabel.setPrefWidth(300);

        Label valorLabel = new Label(String.format("R$ %.2f", servico.getValor()));
        valorLabel.setPrefWidth(150);
        valorLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editButton = new Button();
        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Imagens/alterar.png"))));
        editIcon.setFitHeight(20);
        editIcon.setFitWidth(20);
        editButton.setGraphic(editIcon);
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(event -> handleEditServico(servico));

        Button deleteButton = new Button();
        ImageView deleteIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Imagens/deletar.png"))));
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(20);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> handleDeleteServico(servico));

        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }

        hbox.getChildren().addAll(nomeLabel, valorLabel, spacer, editButton, deleteButton);
        return hbox;
    }

    @FXML
    private void handleNewService(ActionEvent event) {
        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            showAlert(null, Alert.AlertType.ERROR, "Acesso Negado", "Apenas gerentes podem cadastrar serviços.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/cadastrar_servico.fxml"));
            Parent cadastrarServicoView = loader.load();
            Scene scene = ((Button) event.getSource()).getScene();
            scene.setRoot(cadastrarServicoView);
            ((Stage) scene.getWindow()).setTitle("Cadastrar Serviço");
        } catch (IOException e) {
            showAlert(null, Alert.AlertType.ERROR, "Erro ao abrir tela", "Não foi possível carregar a tela de cadastro de serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleEditServico(Servico servico) {
        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            showAlert(null, Alert.AlertType.ERROR, "Acesso Negado", "Apenas gerentes podem alterar serviços.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/alterar_servicoVIEW.fxml"));
            Parent alterarServicoView = loader.load();
            AlterarServicoController controller = loader.getController();
            controller.setServico(servico);
            Scene scene = servicosContainer.getScene();
            scene.setRoot(alterarServicoView);
            ((Stage) scene.getWindow()).setTitle("Alterar Serviço");
        } catch (IOException e) {
            showAlert(null, Alert.AlertType.ERROR, "Erro ao abrir tela", "Não foi possível carregar a tela de alteração de serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeleteServico(Servico servico) {
        if (Main.getUsuarioLogado() == null || !(Main.getUsuarioLogado() instanceof Gerentes)) {
            showAlert(null, Alert.AlertType.ERROR, "Acesso Negado", "Apenas gerentes podem excluir serviços.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Exclusão");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Tem certeza que deseja excluir o serviço: " + servico.getNome() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    servicoService.removerServico(servico.getId());
                    loadServicos("");
                    showAlert(null, Alert.AlertType.INFORMATION, "Sucesso", "Serviço excluído com sucesso!");
                } catch (Exception e) {
                    showAlert(null, Alert.AlertType.ERROR, "Erro", "Erro ao excluir serviço: " + e.getMessage());
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