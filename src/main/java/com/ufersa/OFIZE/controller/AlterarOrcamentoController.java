package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.entitie.Orcamento;
import com.ufersa.OFIZE.model.entitie.OrcamentoPeca;
import com.ufersa.OFIZE.model.entitie.OrcamentoServico;
import com.ufersa.OFIZE.model.entitie.Pecas;
import com.ufersa.OFIZE.model.entitie.Servico;

import com.ufersa.OFIZE.model.service.AutomoveisService;
import com.ufersa.OFIZE.model.service.PecasService;
import com.ufersa.OFIZE.model.service.ServicoService;
import com.ufersa.OFIZE.model.service.ClientesService;
import com.ufersa.OFIZE.model.service.OrcamentoService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
// import javafx.scene.control.CheckBox; // Removido
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
// import javafx.util.Callback; // Pode ser removido se não estiver explicitamente usando um Callback genérico
import javafx.util.StringConverter;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label; // Adicionado para totalOrcamentoLabel
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class AlterarOrcamentoController {

    @FXML
    private TextField veiculoField;
    @FXML
    private ComboBox<Clientes> clienteComboBox;
    @FXML
    private DatePicker dataPicker;
    @FXML
    private TextField valorVeiculoField;
    // @FXML private CheckBox statusCheckBox; // REMOVIDO
    // @FXML private CheckBox pagoCheckBox;     // REMOVIDO
    @FXML
    private Button confirmarButton;
    @FXML
    private Button cancelarButton;

    @FXML
    private ComboBox<Pecas> pecasComboBox;
    @FXML
    private TextField quantidadePecasTextField;
    @FXML
    private TableView<OrcamentoPecaDisplay> pecasTableView;
    @FXML
    private TableColumn<OrcamentoPecaDisplay, String> pecaNomeColumn;
    @FXML
    private TableColumn<OrcamentoPecaDisplay, Integer> pecaQuantidadeColumn;
    @FXML
    private TableColumn<OrcamentoPecaDisplay, String> pecaPrecoUnitarioColumn;
    @FXML
    private TableColumn<OrcamentoPecaDisplay, String> pecaSubtotalColumn;
    @FXML
    private TableColumn<OrcamentoPecaDisplay, Void> pecaAcoesColumn;

    @FXML
    private ComboBox<Servico> servicosComboBox;
    @FXML
    private TextField quantidadeServicosTextField;
    @FXML
    private TableView<OrcamentoServicoDisplay> servicosTableView;
    @FXML
    private TableColumn<OrcamentoServicoDisplay, String> servicoNomeColumn;
    @FXML
    private TableColumn<OrcamentoServicoDisplay, Integer> servicoQuantidadeColumn;
    @FXML
    private TableColumn<OrcamentoServicoDisplay, String> servicoPrecoUnitarioColumn;
    @FXML
    private TableColumn<OrcamentoServicoDisplay, String> servicoSubtotalColumn;
    @FXML
    private TableColumn<OrcamentoServicoDisplay, Void> servicoAcoesColumn;

    @FXML
    private Label totalOrcamentoLabel;

    private OrcamentoService orcamentoService;
    private ClientesService clientesService;
    private AutomoveisService automovelService;
    private PecasService pecasService;
    private ServicoService servicoService;

    private Orcamento orcamentoToAlter;

    private ObservableList<OrcamentoPecaDisplay> listaPecasOrcamento = FXCollections.observableArrayList();
    private ObservableList<OrcamentoServicoDisplay> listaServicosOrcamento = FXCollections.observableArrayList();
    private Map<Long, OrcamentoPecaDisplay> pecasNoOrcamentoMap = new HashMap<>();
    private Map<Long, OrcamentoServicoDisplay> servicosNoOrcamentoMap = new HashMap<>();


    public AlterarOrcamentoController() {
        this.orcamentoService = new OrcamentoService();
        this.clientesService = new ClientesService();
        this.automovelService = new AutomoveisService();
        this.pecasService = new PecasService();
        this.servicoService = new ServicoService();
    }

    @FXML
    public void initialize() {
        carregarClientesComboBox();
        carregarPecasComboBox();
        carregarServicosComboBox();
        setupPecasTableView();
        setupServicosTableView();

        // REMOVIDO: Desabilitar campos pago e status aqui
        // statusCheckBox.setDisable(true);
        // pagoCheckBox.setDisable(true);

        valorVeiculoField.textProperty().addListener((observable, oldValue, newValue) -> calcularTotalOrcamento());

        listaPecasOrcamento.addListener((javafx.collections.ListChangeListener<OrcamentoPecaDisplay>) c -> calcularTotalOrcamento());
        listaServicosOrcamento.addListener((javafx.collections.ListChangeListener<OrcamentoServicoDisplay>) c -> calcularTotalOrcamento());

        pecasComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                quantidadePecasTextField.setDisable(false);
                quantidadePecasTextField.setText("1");
            } else {
                quantidadePecasTextField.setDisable(true);
                quantidadePecasTextField.setText("");
            }
        });
        servicosComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                quantidadeServicosTextField.setDisable(false);
                quantidadeServicosTextField.setText("1");
            } else {
                quantidadeServicosTextField.setDisable(true);
                quantidadeServicosTextField.setText("");
            }
        });

        calcularTotalOrcamento();
    }

    public void setOrcamentoParaAlterar(Orcamento orcamento) {
        this.orcamentoToAlter = orcamento;
        if (orcamentoToAlter != null) {
            veiculoField.setText(orcamentoToAlter.getVeiculo());
            dataPicker.setValue(orcamentoToAlter.getData());
            valorVeiculoField.setText(String.format("%.2f", orcamentoToAlter.getValorVeiculo()));

            // REMOVIDO: Preencher status e pago
            // statusCheckBox.setSelected(orcamentoToAlter.isStatus());
            // pagoCheckBox.setSelected(orcamentoToAlter.isPago());

            if (orcamentoToAlter.getCliente() != null) {
                clienteComboBox.getSelectionModel().select(orcamentoToAlter.getCliente());
            } else {
                clienteComboBox.getSelectionModel().clearSelection();
            }

            listaPecasOrcamento.clear();
            pecasNoOrcamentoMap.clear();
            if (orcamentoToAlter.getOrcamentoPecas() != null) {
                for (OrcamentoPeca op : orcamentoToAlter.getOrcamentoPecas()) {
                    OrcamentoPecaDisplay displayItem = new OrcamentoPecaDisplay(op.getPeca(), op.getQuantidade(), op.getValorUnitario());
                    listaPecasOrcamento.add(displayItem);
                    pecasNoOrcamentoMap.put(op.getPeca().getId(), displayItem);
                }
            }

            listaServicosOrcamento.clear();
            servicosNoOrcamentoMap.clear();
            if (orcamentoToAlter.getOrcamentoServicos() != null) {
                for (OrcamentoServico os : orcamentoToAlter.getOrcamentoServicos()) {
                    OrcamentoServicoDisplay displayItem = new OrcamentoServicoDisplay(os.getServico(), os.getQuantidade(), os.getValorUnitario());
                    listaServicosOrcamento.add(displayItem);
                    servicosNoOrcamentoMap.put(os.getServico().getId(), displayItem);
                }
            }
            pecasTableView.setItems(listaPecasOrcamento);
            servicosTableView.setItems(listaServicosOrcamento);
            calcularTotalOrcamento();
        }
    }

    private void carregarClientesComboBox() {
        List<Clientes> clientes = clientesService.buscarTodos();
        clienteComboBox.setItems(FXCollections.observableArrayList(clientes));
        clienteComboBox.setConverter(new StringConverter<Clientes>() {
            @Override
            public String toString(Clientes cliente) {
                return cliente != null ? cliente.getNome() + " (CPF: " + cliente.getCpf() + ")" : "";
            }

            @Override
            public Clientes fromString(String string) {
                return null;
            }
        });
    }

    private void carregarPecasComboBox() {
        List<Pecas> pecas = pecasService.buscarTodas();
        pecasComboBox.setItems(FXCollections.observableArrayList(pecas));
        pecasComboBox.setConverter(new StringConverter<Pecas>() {
            @Override
            public String toString(Pecas peca) {
                return peca != null ? peca.getNome() + " (R$ " + String.format("%.2f", peca.getPreco()) + ")" : "";
            }
            @Override
            public Pecas fromString(String string) { return null; }
        });
    }

    private void carregarServicosComboBox() {
        List<Servico> servicos = servicoService.listarTodosServicos();
        servicosComboBox.setItems(FXCollections.observableArrayList(servicos));
        servicosComboBox.setConverter(new StringConverter<Servico>() {
            @Override
            public String toString(Servico servico) {
                return servico != null ? servico.getNome() + " (R$ " + String.format("%.2f", servico.getValor()) + ")" : "";
            }
            @Override
            public Servico fromString(String string) { return null; }
        });
    }

    private void setupPecasTableView() {
        pecaNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nomePeca"));
        pecaQuantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        pecaPrecoUnitarioColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getValorUnitario())));
        pecaSubtotalColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getSubtotal())));

        pecaAcoesColumn.setCellFactory(param -> new TableCell<OrcamentoPecaDisplay, Void>() {
            private final Button removeButton = new Button("Remover");
            private final Button editButton = new Button("Editar");

            {
                removeButton.getStyleClass().add("cancel-button");
                removeButton.setPadding(new Insets(3, 5, 3, 5));

                editButton.getStyleClass().add("confirm-button");
                editButton.setPadding(new Insets(3, 5, 3, 5));

                HBox pane = new HBox(5, editButton, removeButton);
                pane.setAlignment(Pos.CENTER);

                removeButton.setOnAction(event -> {
                    OrcamentoPecaDisplay item = getTableView().getItems().get(getIndex());
                    listaPecasOrcamento.remove(item);
                    pecasNoOrcamentoMap.remove(item.getPeca().getId());
                    calcularTotalOrcamento();
                });

                editButton.setOnAction(event -> {
                    OrcamentoPecaDisplay item = getTableView().getItems().get(getIndex());
                    TextInputDialog dialog = new TextInputDialog(String.valueOf(item.getQuantidade()));
                    dialog.setTitle("Editar Quantidade da Peça");
                    dialog.setHeaderText("Altere a quantidade para '" + item.getNomePeca() + "'");
                    dialog.setContentText("Nova Quantidade:");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(newQtyStr -> {
                        try {
                            int newQty = Integer.parseInt(newQtyStr);
                            if (newQty <= 0) {
                                showAlert(Alert.AlertType.WARNING, "Quantidade Inválida", "A quantidade deve ser maior que zero.");
                                return;
                            }
                            if (item.getPeca().getQuantidade() < newQty) {
                                showAlert(Alert.AlertType.ERROR, "Estoque Insuficiente", "Não há estoque suficiente para a peça.");
                                return;
                            }
                            item.setQuantidade(newQty);
                            pecasTableView.refresh();
                            calcularTotalOrcamento();
                        } catch (NumberFormatException e) {
                            showAlert(Alert.AlertType.ERROR, "Erro", "Por favor, insira um número válido.");
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, removeButton));
                }
            }
        });
        pecasTableView.setItems(listaPecasOrcamento);
    }

    private void setupServicosTableView() {
        servicoNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nomeServico"));
        servicoQuantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        servicoPrecoUnitarioColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getValorUnitario())));
        servicoSubtotalColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getSubtotal())));

        servicoAcoesColumn.setCellFactory(param -> new TableCell<OrcamentoServicoDisplay, Void>() {
            private final Button removeButton = new Button("Remover");
            private final Button editButton = new Button("Editar");

            {
                removeButton.getStyleClass().add("cancel-button");
                removeButton.setPadding(new Insets(3, 5, 3, 5));

                editButton.getStyleClass().add("confirm-button");
                editButton.setPadding(new Insets(3, 5, 3, 5));

                HBox pane = new HBox(5, editButton, removeButton);
                pane.setAlignment(Pos.CENTER);


                removeButton.setOnAction(event -> {
                    OrcamentoServicoDisplay item = getTableView().getItems().get(getIndex());
                    listaServicosOrcamento.remove(item);
                    servicosNoOrcamentoMap.remove(item.getServico().getId());
                    calcularTotalOrcamento();
                });

                editButton.setOnAction(event -> {
                    OrcamentoServicoDisplay item = getTableView().getItems().get(getIndex());
                    TextInputDialog dialog = new TextInputDialog(String.valueOf(item.getQuantidade()));
                    dialog.setTitle("Editar Quantidade do Serviço");
                    dialog.setHeaderText("Altere a quantidade para '" + item.getNomeServico() + "'");
                    dialog.setContentText("Nova Quantidade:");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(newQtyStr -> {
                        try {
                            int newQty = Integer.parseInt(newQtyStr);
                            if (newQty <= 0) {
                                showAlert(Alert.AlertType.WARNING, "Quantidade Inválida", "A quantidade deve ser maior que zero.");
                                return;
                            }
                            item.setQuantidade(newQty);
                            servicosTableView.refresh();
                            calcularTotalOrcamento();
                        } catch (NumberFormatException e) {
                            showAlert(Alert.AlertType.ERROR, "Erro", "Por favor, insira um número válido.");
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, removeButton));
                }
            }
        });
        servicosTableView.setItems(listaServicosOrcamento);
    }

    @FXML
    private void adicionarPecaAoOrcamento() {
        Pecas pecaSelecionada = pecasComboBox.getSelectionModel().getSelectedItem();
        String quantidadeText = quantidadePecasTextField.getText();

        if (pecaSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione uma peça.");
            return;
        }
        if (quantidadeText.isEmpty() || !quantidadeText.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Quantidade Inválida", "Por favor, insira uma quantidade válida para a peça.");
            return;
        }

        int quantidade = Integer.parseInt(quantidadeText);
        if (quantidade <= 0) {
            showAlert(Alert.AlertType.WARNING, "Quantidade Inválida", "A quantidade da peça deve ser maior que zero.");
            return;
        }

        if (pecaSelecionada.getQuantidade() < quantidade) {
            showAlert(Alert.AlertType.ERROR, "Estoque Insuficiente", "Não há estoque suficiente para a peça selecionada.");
            return;
        }

        OrcamentoPecaDisplay existingItem = pecasNoOrcamentoMap.get(pecaSelecionada.getId());
        if (existingItem != null) {
            int novaQuantidadeTotal = existingItem.getQuantidade() + quantidade;
            if (pecaSelecionada.getQuantidade() < novaQuantidadeTotal) {
                showAlert(Alert.AlertType.ERROR, "Estoque Insuficiente", "Aumentar a quantidade excederia o estoque disponível.");
                return;
            }
            existingItem.setQuantidade(novaQuantidadeTotal);
            pecasTableView.refresh();
        } else {
            OrcamentoPecaDisplay newDisplayItem = new OrcamentoPecaDisplay(pecaSelecionada, quantidade, pecaSelecionada.getPreco());
            listaPecasOrcamento.add(newDisplayItem);
            pecasNoOrcamentoMap.put(pecaSelecionada.getId(), newDisplayItem);
        }

        calcularTotalOrcamento();
        pecasComboBox.getSelectionModel().clearSelection();
        quantidadePecasTextField.setText("1");
        quantidadePecasTextField.setDisable(true);
    }

    @FXML
    private void adicionarServicoAoOrcamento() {
        Servico servicoSelecionado = servicosComboBox.getSelectionModel().getSelectedItem();
        String quantidadeText = quantidadeServicosTextField.getText();

        if (servicoSelecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione um serviço.");
            return;
        }
        if (quantidadeText.isEmpty() || !quantidadeText.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Quantidade Inválida", "Por favor, insira uma quantidade válida para o serviço.");
            return;
        }

        int quantidade = Integer.parseInt(quantidadeText);
        if (quantidade <= 0) {
            showAlert(Alert.AlertType.WARNING, "Quantidade Inválida", "A quantidade do serviço deve ser maior que zero.");
            return;
        }

        OrcamentoServicoDisplay existingItem = servicosNoOrcamentoMap.get(servicoSelecionado.getId());
        if (existingItem != null) {
            existingItem.setQuantidade(existingItem.getQuantidade() + quantidade);
            servicosTableView.refresh();
        } else {
            OrcamentoServicoDisplay newDisplayItem = new OrcamentoServicoDisplay(servicoSelecionado, quantidade, servicoSelecionado.getValor());
            listaServicosOrcamento.add(newDisplayItem);
            servicosNoOrcamentoMap.put(servicoSelecionado.getId(), newDisplayItem);
        }

        calcularTotalOrcamento();
        servicosComboBox.getSelectionModel().clearSelection();
        quantidadeServicosTextField.setText("1");
        quantidadeServicosTextField.setDisable(true);
    }

    private void calcularTotalOrcamento() {
        double totalPecas = listaPecasOrcamento.stream()
                .mapToDouble(OrcamentoPecaDisplay::getSubtotal)
                .sum();

        double totalServicos = listaServicosOrcamento.stream()
                .mapToDouble(OrcamentoServicoDisplay::getSubtotal)
                .sum();

        double valorVeiculo = 0.0;
        String valorVeiculoText = valorVeiculoField.getText();
        if (valorVeiculoText != null && !valorVeiculoText.trim().isEmpty()) {
            try {
                valorVeiculo = Double.parseDouble(valorVeiculoText.replace(",", "."));
            } catch (NumberFormatException e) {
                System.err.println("Valor do veículo inválido: " + valorVeiculoText);
            }
        }

        double totalGeral = totalPecas + totalServicos + valorVeiculo;
        // Verifica se totalOrcamentoLabel não é nulo antes de tentar usar
        if (totalOrcamentoLabel != null) {
            totalOrcamentoLabel.setText(String.format("Total do Orçamento: R$ %.2f", totalGeral));
        }
    }


    @FXML
    private void handleConfirmar(ActionEvent event) {
        if (orcamentoToAlter == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Nenhum orçamento selecionado para alteração.");
            return;
        }

        try {
            if (veiculoField.getText().trim().isEmpty() || dataPicker.getValue() == null || clienteComboBox.getSelectionModel().getSelectedItem() == null) {
                showAlert(Alert.AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha todos os campos obrigatórios (Veículo, Cliente, Data).");
                return;
            }

            double valorVeiculo = 0.0;
            String valorVeiculoText = valorVeiculoField.getText();
            if (valorVeiculoText != null && !valorVeiculoText.trim().isEmpty()) {
                try {
                    valorVeiculo = Double.parseDouble(valorVeiculoText.replace(",", "."));
                    if (valorVeiculo < 0) {
                        showAlert(Alert.AlertType.ERROR, "Erro de Validação", "O valor mínimo do veículo não pode ser negativo.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Por favor, insira um valor numérico válido para o 'Valor Mínimo do Veículo'.");
                    return;
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Atenção", "O 'Valor Mínimo do Veículo' está vazio. Será considerado R$ 0.00.");
                valorVeiculo = 0.0;
            }

            orcamentoToAlter.setVeiculo(veiculoField.getText());
            orcamentoToAlter.setCliente(clienteComboBox.getSelectionModel().getSelectedItem());
            orcamentoToAlter.setData(dataPicker.getValue());
            orcamentoToAlter.setValorVeiculo(valorVeiculo);

            // REMOVIDO: Não alterar status e pago
            // orcamentoToAlter.setStatus(statusCheckBox.isSelected());
            // orcamentoToAlter.setPago(pagoCheckBox.isSelected());

            List<OrcamentoPeca> orcamentoPecasAtualizadas = listaPecasOrcamento.stream()
                    .map(displayItem -> new OrcamentoPeca(orcamentoToAlter, displayItem.getPeca(), displayItem.getQuantidade(), displayItem.getValorUnitario()))
                    .collect(Collectors.toList());
            orcamentoToAlter.setOrcamentoPecas(orcamentoPecasAtualizadas);

            List<OrcamentoServico> orcamentoServicosAtualizados = listaServicosOrcamento.stream()
                    .map(displayItem -> new OrcamentoServico(orcamentoToAlter, displayItem.getServico(), displayItem.getQuantidade(), displayItem.getValorUnitario()))
                    .collect(Collectors.toList());
            orcamentoToAlter.setOrcamentoServicos(orcamentoServicosAtualizados);


            orcamentoService.atualizarOrcamento(orcamentoToAlter);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Orçamento alterado com sucesso!");
            returnToOrcamentoView(event);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Alterar Orçamento", "Ocorreu um erro ao tentar alterar o orçamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        returnToOrcamentoView(event);
    }

    private void returnToOrcamentoView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/OrcamentoView.fxml"));
            Parent orcamentoView = loader.load();

            Scene scene = ((Button) event.getSource()).getScene();
            scene.setRoot(orcamentoView);

            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Lista de Orçamentos");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de orçamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage owner = (Stage) (veiculoField != null ? veiculoField.getScene().getWindow() : null);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }

    // --- Classes Display (mantidas, são cruciais para as TableViews) ---
    public static class OrcamentoPecaDisplay {
        private final Pecas peca;
        private javafx.beans.property.IntegerProperty quantidade;
        private javafx.beans.property.StringProperty nomePeca;
        private double valorUnitario;

        public OrcamentoPecaDisplay(Pecas peca, int quantidade, double valorUnitario) {
            this.peca = peca;
            this.quantidade = new javafx.beans.property.SimpleIntegerProperty(quantidade);
            this.nomePeca = new javafx.beans.property.SimpleStringProperty(peca.getNome());
            this.valorUnitario = valorUnitario;
        }

        public Pecas getPeca() { return peca; }
        public int getQuantidade() { return quantidade.get(); }
        public javafx.beans.property.IntegerProperty quantidadeProperty() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade.set(quantidade); }

        public String getNomePeca() { return nomePeca.get(); }
        public javafx.beans.property.StringProperty nomePecaProperty() { return nomePeca; }

        public double getValorUnitario() { return valorUnitario; }

        public double getSubtotal() { return this.valorUnitario * quantidade.get(); }
    }

    public static class OrcamentoServicoDisplay {
        private final Servico servico;
        private javafx.beans.property.IntegerProperty quantidade;
        private javafx.beans.property.StringProperty nomeServico;
        private double valorUnitario;

        public OrcamentoServicoDisplay(Servico servico, int quantidade, double valorUnitario) {
            this.servico = servico;
            this.quantidade = new javafx.beans.property.SimpleIntegerProperty(quantidade);
            this.nomeServico = new javafx.beans.property.SimpleStringProperty(servico.getNome());
            this.valorUnitario = valorUnitario;
        }

        public Servico getServico() { return servico; }
        public int getQuantidade() { return quantidade.get(); }
        public javafx.beans.property.IntegerProperty quantidadeProperty() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade.set(quantidade); }

        public String getNomeServico() { return nomeServico.get(); }
        public javafx.beans.property.StringProperty nomeServicoProperty() { return nomeServico; }

        public double getValorUnitario() { return valorUnitario; }

        public double getSubtotal() { return this.valorUnitario * quantidade.get(); }
    }
}