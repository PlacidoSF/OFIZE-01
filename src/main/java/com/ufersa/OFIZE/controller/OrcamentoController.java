package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Orcamento;
import com.ufersa.OFIZE.model.service.ClientesService;
import com.ufersa.OFIZE.model.service.OrcamentoService;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrcamentoController {

    @FXML
    private TextField searchVeiculoField;
    @FXML
    private ComboBox<Clientes> clienteComboBox;
    @FXML
    private DatePicker dataInicioPicker;
    @FXML
    private DatePicker dataFimPicker;
    @FXML
    private Button cadastrarOrcamentoButton;
    @FXML
    private VBox orcamentosContainer;
    @FXML
    private VBox menuHamburguer; // NOVO CAMPO

    private OrcamentoService orcamentoService;
    private ClientesService clientesService;

    @FXML
    public void initialize() {
        orcamentoService = new OrcamentoService();
        clientesService = new ClientesService();

        carregarClientesComboBox();
        handlePesquisarOrcamentos();

        searchVeiculoField.textProperty().addListener((obs, oldText, newText) -> handlePesquisarOrcamentos());
        clienteComboBox.valueProperty().addListener((obs, oldCliente, newCliente) -> handlePesquisarOrcamentos());
        dataInicioPicker.valueProperty().addListener((obs, oldDate, newDate) -> handlePesquisarOrcamentos());
        dataFimPicker.valueProperty().addListener((obs, oldDate, newDate) -> handlePesquisarOrcamentos());

        // LÓGICA ADICIONADA: Adiciona a ação de clique para voltar ao menu
        menuHamburguer.setOnMouseClicked(this::handleVoltarAoMenu);
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

    private void carregarClientesComboBox() {
        List<Clientes> clientes = clientesService.buscarTodos();
        clienteComboBox.setItems(FXCollections.observableArrayList(clientes));
        clienteComboBox.setConverter(new StringConverter<Clientes>() {
            @Override
            public String toString(Clientes cliente) {
                return cliente != null ? cliente.getNome() + " (CPF: " + cliente.getCpf() + ")" : "Todos os Clientes";
            }

            @Override
            public Clientes fromString(String string) {
                return null;
            }
        });
        clienteComboBox.getItems().add(0, null);
        clienteComboBox.setPromptText("Filtrar por Cliente (Todos)");
    }

    @FXML
    private void handlePesquisarOrcamentos() {
        orcamentosContainer.getChildren().clear();

        String termoVeiculo = searchVeiculoField.getText();
        Clientes clienteSelecionado = clienteComboBox.getSelectionModel().getSelectedItem();
        LocalDate dataInicio = dataInicioPicker.getValue();
        LocalDate dataFim = dataFimPicker.getValue();

        List<Orcamento> resultados = orcamentoService.buscarTodos();

        if (resultados != null) {
            if (termoVeiculo != null && !termoVeiculo.trim().isEmpty()) {
                String termoLowerCase = termoVeiculo.toLowerCase();
                resultados = resultados.stream()
                        .filter(o -> o.getVeiculo() != null && o.getVeiculo().toLowerCase().contains(termoLowerCase))
                        .collect(Collectors.toList());
            }

            if (clienteSelecionado != null) {
                resultados = resultados.stream()
                        .filter(o -> o.getCliente() != null && o.getCliente().getId().equals(clienteSelecionado.getId()))
                        .collect(Collectors.toList());
            }

            if (dataInicio != null && dataFim != null) {
                if (dataInicio.isAfter(dataFim)) {
                    showAlert(Alert.AlertType.WARNING, "Datas Inválidas", "A data de início não pode ser posterior à data de fim.");
                    return;
                } else {
                    resultados = resultados.stream()
                            .filter(o -> o.getData() != null && !o.getData().isBefore(dataInicio) && !o.getData().isAfter(dataFim))
                            .collect(Collectors.toList());
                }
            } else if (dataInicio != null) {
                resultados = resultados.stream()
                        .filter(o -> o.getData() != null && !o.getData().isBefore(dataInicio))
                        .collect(Collectors.toList());
            } else if (dataFim != null) {
                resultados = resultados.stream()
                        .filter(o -> o.getData() != null && !o.getData().isAfter(dataFim))
                        .collect(Collectors.toList());
            }

            for (Orcamento orcamento : resultados) {
                orcamentosContainer.getChildren().add(createOrcamentoRow(orcamento));
            }
        }
    }

    private HBox createOrcamentoRow(Orcamento orcamento) {
        HBox row = new HBox(10);
        row.getStyleClass().add("client-row");
        row.setAlignment(Pos.CENTER_LEFT);

        Label dataLabel = new Label(orcamento.getData() != null ? orcamento.getData().toString() : "N/A");
        dataLabel.setPrefWidth(100.0);
        dataLabel.getStyleClass().add("client-data");

        Label veiculoLabel = new Label(orcamento.getVeiculo());
        veiculoLabel.setPrefWidth(200.0);
        veiculoLabel.getStyleClass().add("client-data");

        Label clienteLabel = new Label(orcamento.getCliente() != null ? orcamento.getCliente().getNome() : "N/A");
        clienteLabel.setPrefWidth(250.0);
        clienteLabel.getStyleClass().add("client-data");

        Label valorTotalLabel = new Label(String.format("R$ %.2f", orcamento.getValorTotal()));
        valorTotalLabel.setPrefWidth(120.0);
        valorTotalLabel.getStyleClass().add("client-data");
        valorTotalLabel.setAlignment(Pos.CENTER);


        HBox statusBox = new HBox(5);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        statusBox.setPrefWidth(120.0);
        Label statusLabel = new Label(orcamento.isStatus() ? "Concluído" : "Pendente");
        Button statusToggleButton = new Button();
        statusToggleButton.getStyleClass().add("toggle-button");
        ImageView checkIconStatus = createIcon("/Imagens/check.png", 15, 15);
        statusToggleButton.setGraphic(checkIconStatus);
        statusToggleButton.setOnAction(event -> {
            orcamento.setStatus(!orcamento.isStatus());
            orcamentoService.salvarOrcamento(orcamento);
            handlePesquisarOrcamentos();
            showAlert(Alert.AlertType.INFORMATION, "Status Alterado",
                    "O status do orçamento ID " + orcamento.getId() + " foi alterado para " +
                            (orcamento.isStatus() ? "Concluído" : "Pendente") + ".");
        });
        statusBox.getChildren().addAll(statusLabel, statusToggleButton);
        statusLabel.setPrefWidth(80.0);
        statusToggleButton.setPrefWidth(30.0);

        HBox pagoBox = new HBox(5);
        pagoBox.setAlignment(Pos.CENTER_LEFT);
        pagoBox.setPrefWidth(120.0);
        Label pagoLabel = new Label(orcamento.isPago() ? "Concluído" : "Pendente");
        Button pagoToggleButton = new Button();
        pagoToggleButton.getStyleClass().add("toggle-button");
        ImageView checkIconPago = createIcon("/Imagens/check.png", 15, 15);
        pagoToggleButton.setGraphic(checkIconPago);
        pagoToggleButton.setOnAction(event -> {
            orcamento.setPago(!orcamento.isPago());
            orcamentoService.salvarOrcamento(orcamento);
            handlePesquisarOrcamentos();
            showAlert(Alert.AlertType.INFORMATION, "Pagamento Alterado",
                    "O status de pagamento do orçamento ID " + orcamento.getId() + " foi alterado para " +
                            (orcamento.isPago() ? "Concluído" : "Pendente") + ".");
        });
        pagoBox.getChildren().addAll(pagoLabel, pagoToggleButton);
        pagoLabel.setPrefWidth(80.0);
        pagoToggleButton.setPrefWidth(30.0);

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox actionButtons = new HBox(5);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPrefWidth(100.0);

        Button alterarButton = new Button();
        ImageView alterarIcon = createIcon("/Imagens/alterar.png", 20, 20);
        alterarButton.setGraphic(alterarIcon);
        alterarButton.getStyleClass().add("edit-button");
        alterarButton.setOnAction(event -> handleAlterarOrcamento(orcamento, event));

        Button deletarButton = new Button();
        ImageView deletarIcon = createIcon("/Imagens/deletar.png", 20, 20);
        deletarButton.setGraphic(deletarIcon);
        deletarButton.getStyleClass().add("delete-button");
        deletarButton.setOnAction(event -> handleDeletarOrcamento(orcamento));

        if (orcamento.isStatus() && orcamento.isPago()) {
            alterarButton.setDisable(true);
            deletarButton.setDisable(true);
        }

        actionButtons.getChildren().addAll(alterarButton, deletarButton);

        row.getChildren().addAll(dataLabel, veiculoLabel, clienteLabel, valorTotalLabel, statusBox, pagoBox, spacer, actionButtons);
        return row;
    }

    private ImageView createIcon(String imagePath, int width, int height) {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is == null) {
                System.err.println("Recurso de imagem não encontrado: " + imagePath);
                return new ImageView();
            }
            Image image = new Image(is);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            return imageView;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar imagem: " + imagePath);
            return new ImageView();
        }
    }

    @FXML
    private void handleCadastrarOrcamento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/cadastrar_orcamento.fxml"));
            Parent root = loader.load();
            // Use the current scene to set the new root
            Scene scene = ((Button) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar a tela de cadastro de orçamento.");
        }
    }

    private void handleAlterarOrcamento(Orcamento orcamento, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/alterar_orcamento.fxml"));
            Parent root = loader.load();

            AlterarOrcamentoController controller = loader.getController();
            if (controller != null) {
                controller.setOrcamentoParaAlterar(orcamento);
            }

            // Use the current scene to set the new root
            Scene scene = ((Button) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar a tela de alteração de orçamento.");
        }
    }

    private void handleDeletarOrcamento(Orcamento orcamento) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Você realmente quer apagar este Orçamento?");
        alert.setContentText("ID: " + orcamento.getId() + " - Veículo: " + orcamento.getVeiculo());

        ButtonType simButton = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
        ButtonType naoButton = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(simButton, naoButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == simButton) {
            orcamentoService.deletarOrcamento(orcamento.getId());
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Orçamento deletado com sucesso.");
            handlePesquisarOrcamentos();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}