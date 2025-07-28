package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Servico;
import com.ufersa.OFIZE.model.service.ServicoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene; // Manter import para Scene, embora a criação mude
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
import javafx.stage.Stage; // Manter import para Stage, para Alerts
import javafx.scene.layout.AnchorPane; // Usado no handleNewService, então manter


import java.io.IOException;
import java.util.List;

public class ServicoViewController {

    @FXML
    private TextField searchField;
    @FXML
    private VBox servicosContainer; // Usado para obter a cena atual
    @FXML
    private Button newServiceButton; // Não diretamente usado para obter a cena, mas o evento sim

    private ServicoService servicoService;
    private List<Servico> allServices;

    @FXML
    public void initialize() {
        this.servicoService = new ServicoService();
        loadAndDisplayServices();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Get the current stage for the alert owner when filtering
            Stage currentStage = (Stage) searchField.getScene().getWindow();
            filterServices(currentStage, newValue); // Pass stage to filterServices
        });
    }

    private void loadAndDisplayServices() {
        try {
            allServices = servicoService.listarTodosServicos();
            populateServicesContainer(allServices);
        } catch (Exception e) {
            // Get the current stage for the alert owner
            Stage currentStage = null;
            if (servicosContainer != null && servicosContainer.getScene() != null) {
                currentStage = (Stage) servicosContainer.getScene().getWindow();
            }
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro ao carregar serviços",
                    "Não foi possível carregar os serviços do banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateServicesContainer(List<Servico> servicesToDisplay) {
        servicosContainer.getChildren().clear();

        if (servicesToDisplay == null || servicesToDisplay.isEmpty()) {
            servicosContainer.getChildren().add(new Label("Nenhum serviço encontrado."));
            return;
        }

        for (Servico servico : servicesToDisplay) {
            servicosContainer.getChildren().add(createServiceRow(servico));
        }
    }

    private HBox createServiceRow(Servico servico) {
        HBox row = new HBox();
        row.getStyleClass().add("service-row");
        row.setSpacing(10);

        Label nameLabel = new Label(servico.getNome());
        nameLabel.getStyleClass().add("service-data");
        nameLabel.setPrefWidth(300.0);
        nameLabel.setMinWidth(Region.USE_PREF_SIZE);

        Label valueLabel = new Label(String.format("R$ %.2f", servico.getValor()));
        valueLabel.getStyleClass().add("service-data");
        valueLabel.setPrefWidth(150.0);
        valueLabel.setMinWidth(Region.USE_PREF_SIZE);
        valueLabel.setStyle("-fx-alignment: center;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editButton = new Button();
        try {
            ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/alterar.png")));
            editIcon.setFitHeight(16);
            editIcon.setFitWidth(16);
            editButton.setGraphic(editIcon);
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem /Imagens/alterar.png: " + e.getMessage());
            editButton.setText("Editar");
        }
        editButton.getStyleClass().add("edit-button");
        editButton.setPrefWidth(32);
        editButton.setOnAction(event -> handleEditService(event, servico));

        Button deleteButton = new Button();
        try {
            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/deletar.png")));
            deleteIcon.setFitHeight(16);
            deleteIcon.setFitWidth(16);
            deleteButton.setGraphic(deleteIcon);
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem /Imagens/deletar.png: " + e.getMessage());
            deleteButton.setText("Excluir");
        }
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setPrefWidth(32);
        deleteButton.setOnAction(event -> handleDeleteService(event, servico));

        HBox actionButtonsContainer = new HBox(5);
        actionButtonsContainer.getChildren().addAll(editButton, deleteButton);
        actionButtonsContainer.setPrefWidth(100.0);
        actionButtonsContainer.setAlignment(javafx.geometry.Pos.CENTER);

        row.getChildren().addAll(nameLabel, valueLabel, spacer, actionButtonsContainer);
        return row;
    }

    private void filterServices(Stage ownerStage, String searchText) {
        try {
            List<Servico> filteredServices = servicoService.buscarServicosPorNome(searchText);
            populateServicesContainer(filteredServices);
        } catch (Exception e) {
            showAlert(ownerStage, Alert.AlertType.ERROR, "Erro ao pesquisar serviços",
                    "Não foi possível pesquisar os serviços: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditService(ActionEvent event, Servico servico) {
        // A stage atual pode ser obtida a partir de qualquer nó FXML.
        // Se a aplicação está em tela cheia, a stage permanece a mesma.
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        try {
            // CONFIRA O NOME EXATO DO SEU ARQUIVO FXML: alterar_servico.fxml ou alterar_servicoVIEW.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/alterar_servicoVIEW.fxml")); // Ajustado para nome mais comum
            Parent alterarServicoView = loader.load();

            AlterarServicoController controller = loader.getController();
            controller.setServico(servico);

            // *** MUDANÇA AQUI: USAR scene.setRoot() em vez de currentStage.setScene(new Scene(...)) ***
            // Isso manterá a mesma Stage e Scene, apenas trocando o conteúdo.
            Scene scene = currentStage.getScene(); // Obtém a cena ATUAL da Stage
            scene.setRoot(alterarServicoView); // Define a nova raiz da cena ATUAL

            currentStage.setTitle("Alterar Serviço"); // Pode querer atualizar o título da janela
            // NÃO PRECISA CHAMAR currentStage.setFullScreen(true) AQUI NOVAMENTE
            // NEM setFullScreenExitHint/KeyCombination. Essas propriedades são da Stage
            // e devem ser definidas UMA VEZ no Main.java se a aplicação começar em FullScreen.

        } catch (IOException e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro ao abrir tela", "Não foi possível carregar a tela de alteração de serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeleteService(ActionEvent event, Servico servico) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar Exclusão");
        confirmDialog.setHeaderText("Tem certeza que deseja excluir o serviço?");
        confirmDialog.setContentText("Serviço: " + servico.getNome());
        confirmDialog.initOwner(currentStage);

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    servicoService.removerServico(servico);
                    showAlert(currentStage, Alert.AlertType.INFORMATION, "Sucesso", "Serviço removido com sucesso!");
                    loadAndDisplayServices();
                } catch (IllegalArgumentException e) {
                    showAlert(currentStage, Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
                } catch (Exception e) {
                    showAlert(currentStage, Alert.AlertType.ERROR, "Erro ao remover serviço",
                            "Não foi possível remover o serviço: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleNewService(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/cadastrar_servico.fxml"));
            AnchorPane cadastrarServicoView = loader.load();

            // *** MUDANÇA AQUI: USAR scene.setRoot() em vez de currentStage.setScene(new Scene(...)) ***
            Scene scene = currentStage.getScene(); // Obtém a cena ATUAL da Stage
            scene.setRoot(cadastrarServicoView); // Define a nova raiz da cena ATUAL

            currentStage.setTitle("Cadastrar Serviço"); // Pode querer atualizar o título da janela
            // NÃO PRECISA CHAMAR currentStage.setFullScreen(true) AQUI NOVAMENTE
            // NEM setFullScreenExitHint/KeyCombination. Essas propriedades são da Stage
            // e devem ser definidas UMA VEZ no Main.java se a aplicação começar em FullScreen.

        } catch (IOException e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro ao abrir tela", "Não foi possível carregar a tela de cadastro de serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Stage owner, Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (owner != null) {
            alert.initOwner(owner);
        } else {
            System.err.println("WARNING: Alert owner stage is null. Alert might not display correctly.");
        }
        alert.showAndWait();
    }
}