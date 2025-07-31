package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Orcamento;
import com.ufersa.OFIZE.model.service.OrcamentoService;
import com.ufersa.OFIZE.utils.strategy.ReportExporterStrategy;
import com.ufersa.OFIZE.utils.strategy.TxtReportExporterStrategy;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

public class RelatoriosController implements Initializable {

    @FXML
    private DatePicker dataInicioPicker;
    @FXML
    private DatePicker dataFimPicker;
    @FXML
    private CheckBox concluidoCheckBox;
    @FXML
    private CheckBox pagoCheckBox;
    @FXML
    private CheckBox pendenteCheckBox;
    @FXML
    private TableView<Orcamento> orcamentosTableView;
    @FXML
    private TableColumn<Orcamento, String> dataColumn;
    @FXML
    private TableColumn<Orcamento, String> clienteColumn;
    @FXML
    private TableColumn<Orcamento, String> veiculoColumn;
    @FXML
    private TableColumn<Orcamento, Double> valorTotalColumn;
    @FXML
    private TableColumn<Orcamento, String> statusColumn;
    @FXML
    private TableColumn<Orcamento, String> pagoColumn;
    @FXML
    private Label totalGeralLabel;
    @FXML
    private VBox menuHamburguer;

    private OrcamentoService orcamentoService;
    private ObservableList<Orcamento> orcamentoList;

    private ReportExporterStrategy currentExporterStrategy;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orcamentoService = new OrcamentoService();
        orcamentoList = FXCollections.observableArrayList();
        orcamentosTableView.setItems(orcamentoList);

        this.currentExporterStrategy = new TxtReportExporterStrategy();

        dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getData() != null ? cellData.getValue().getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : ""
        ));
        clienteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().getNome() : ""
        ));
        veiculoColumn.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
        valorTotalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValorTotal()).asObject());

        statusColumn.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().isStatus();
            return new SimpleStringProperty(status ? "Concluído" : "Pendente");
        });
        statusColumn.setCellFactory(col -> new TableCell<Orcamento, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        pagoColumn.setCellValueFactory(cellData -> {
            boolean pago = cellData.getValue().isPago();
            return new SimpleStringProperty(pago ? "Concluído" : "Pendente");
        });
        pagoColumn.setCellFactory(col -> new TableCell<Orcamento, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        dataInicioPicker.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosAutomaticamente());
        dataFimPicker.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosAutomaticamente());
        concluidoCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosAutomaticamente());
        pagoCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosAutomaticamente());
        pendenteCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosAutomaticamente());

        aplicarFiltrosAutomaticamente();

        menuHamburguer.setOnMouseClicked(this::voltarAoMenu);
    }

    private void aplicarFiltrosAutomaticamente() {
        LocalDate inicio = dataInicioPicker.getValue();
        LocalDate fim = dataFimPicker.getValue();

        Boolean statusConcluido = null;
        Boolean statusPago = null;

        if (pendenteCheckBox.isSelected()) {
            statusConcluido = false;
            statusPago = false;

            concluidoCheckBox.setDisable(true);
            pagoCheckBox.setDisable(true);
            concluidoCheckBox.setSelected(false);
            pagoCheckBox.setSelected(false);

        } else {
            concluidoCheckBox.setDisable(false);
            pagoCheckBox.setDisable(false);

            statusConcluido = concluidoCheckBox.isSelected() ? true : null;
            statusPago = pagoCheckBox.isSelected() ? true : null;
        }

        List<Orcamento> orcamentosFiltrados = orcamentoService.buscarOrcamentosParaRelatorio(inicio, fim, statusConcluido, statusPago);
        orcamentoList.setAll(orcamentosFiltrados);

        double totalGeral = orcamentosFiltrados.stream().mapToDouble(Orcamento::getValorTotal).sum();
        totalGeralLabel.setText(String.format("Total Geral dos Orçamentos Filtrados: R$ %.2f", totalGeral));
    }

    @FXML
    private void exportarTxt() {
        List<Orcamento> dadosParaTxt = orcamentosTableView.getItems();

        if (dadosParaTxt.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Nenhum Dado", "Não há orçamentos para exportar para TXT.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório de Orçamentos (TXT)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos TXT (*.txt)", "*.txt"));
        fileChooser.setInitialFileName("relatorio_orcamentos.txt");

        Stage currentStage = (Stage) orcamentosTableView.getScene().getWindow();
        File file = fileChooser.showSaveDialog(currentStage);

        if (file != null) {
            try {
                currentExporterStrategy.export(dadosParaTxt, file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Arquivo TXT gerado com sucesso em: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar TXT: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Operação Cancelada", "Geração de TXT cancelada.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void voltarAoMenu(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/menu.fxml"));
            Scene scene = menuHamburguer.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}