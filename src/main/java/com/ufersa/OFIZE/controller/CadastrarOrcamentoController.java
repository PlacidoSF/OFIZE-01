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
import com.ufersa.OFIZE.model.service.OrcamentoService;
import com.ufersa.OFIZE.model.service.ClientesService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.layout.AnchorPane; // Remova esta importação
import javafx.scene.layout.BorderPane; // Adicione esta importação
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CadastrarOrcamentoController {

    @FXML
    private BorderPane mainPane; // ALTERADO: Tipo de AnchorPane para BorderPane
    @FXML
    private ComboBox<Automoveis> automovelComboBox;
    @FXML
    private ComboBox<Clientes> clienteComboBox;
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private TextField valorVeiculoTextField; // Novo campo para o valor mínimo do veículo

    // Peças
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
    private TableColumn<OrcamentoPecaDisplay, Button> pecaAcoesColumn;


    // Serviços
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
    private TableColumn<OrcamentoServicoDisplay, Button> servicoAcoesColumn;

    @FXML
    private Label totalOrcamentoLabel;

    // Serviços (Camada de Negócio)
    private OrcamentoService orcamentoService;
    private AutomoveisService automovelService;
    private PecasService pecasService;
    private ServicoService servicoService;
    private ClientesService clientesService;

    // Listas para a TableView (agora baseadas nas classes Display)
    private ObservableList<OrcamentoPecaDisplay> listaPecasOrcamento = FXCollections.observableArrayList();
    private ObservableList<OrcamentoServicoDisplay> listaServicosOrcamento = FXCollections.observableArrayList();

    // Mapas para controle de unicidade/quantidade (opcional, pode ser feito iterando nas listas)
    private Map<Long, OrcamentoPecaDisplay> pecasNoOrcamentoMap = new HashMap<>();
    private Map<Long, OrcamentoServicoDisplay> servicosNoOrcamentoMap = new HashMap<>();

    @FXML
    public void initialize() {
        orcamentoService = new OrcamentoService();
        automovelService = new AutomoveisService();
        pecasService = new PecasService();
        servicoService = new ServicoService();
        clientesService = new ClientesService();

        dataDatePicker.setValue(LocalDate.now());

        // Listener para o campo valorVeiculoTextField
        valorVeiculoTextField.textProperty().addListener((observable, oldValue, newValue) -> calcularTotalOrcamento());


        // --- Configuração dos ComboBoxes ---

        // Automóveis
        List<Automoveis> automoveis = automovelService.buscarTodos();
        automovelComboBox.setItems(FXCollections.observableArrayList(automoveis));
        automovelComboBox.setConverter(new StringConverter<Automoveis>() {
            @Override
            public String toString(Automoveis automovel) {
                return automovel != null ? automovel.getMarca() + " - " + automovel.getPlaca() : "";
            }

            @Override
            public Automoveis fromString(String string) {
                return null;
            }
        });

        // Clientes
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


        // Peças
        List<Pecas> pecas = pecasService.buscarTodas();
        pecasComboBox.setItems(FXCollections.observableArrayList(pecas));
        pecasComboBox.setConverter(new StringConverter<Pecas>() {
            @Override
            public String toString(Pecas peca) {
                return peca != null ? peca.getNome() + " (R$ " + String.format("%.2f", peca.getPreco()) + ")" : "";
            }

            @Override
            public Pecas fromString(String string) {
                return null;
            }
        });

        pecasComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                quantidadePecasTextField.setDisable(false);
                quantidadePecasTextField.setText("1");
            } else {
                quantidadePecasTextField.setDisable(true);
                quantidadePecasTextField.setText("");
            }
        });


        // Serviços
        List<Servico> servicos = servicoService.listarTodosServicos();
        servicosComboBox.setItems(FXCollections.observableArrayList(servicos));
        servicosComboBox.setConverter(new StringConverter<Servico>() {
            @Override
            public String toString(Servico servico) {
                return servico != null ? servico.getNome() + " (R$ " + String.format("%.2f", servico.getValor()) + ")" : "";
            }

            @Override
            public Servico fromString(String string) {
                return null;
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

        // --- Configuração das TableViews ---
        setupPecasTableView();
        setupServicosTableView();

        // Listener para atualizar o total do orçamento
        listaPecasOrcamento.addListener((javafx.collections.ListChangeListener<OrcamentoPecaDisplay>) c -> calcularTotalOrcamento());
        listaServicosOrcamento.addListener((javafx.collections.ListChangeListener<OrcamentoServicoDisplay>) c -> calcularTotalOrcamento());

        calcularTotalOrcamento(); // Chama para iniciar com 0.00
    }

    private void setupPecasTableView() {
        pecaNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nomePeca"));
        pecaQuantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        pecaPrecoUnitarioColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getValorUnitario())));
        pecaSubtotalColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getSubtotal())));

        pecaAcoesColumn.setCellFactory(param -> new TableCell<OrcamentoPecaDisplay, Button>() {
            final Button deleteButton = new Button("Remover");
            {
                // ALTERADO: Usando getStyleClass().add() em vez de setStyle()
                deleteButton.getStyleClass().add("cancel-button"); // Adiciona a classe CSS
                // Opcional: ajustar padding ou tamanho se o "cancel-button" for muito grande para a célula
                // deleteButton.setStyle("-fx-padding: 2 5 2 5;");
                deleteButton.setOnAction(event -> {
                    OrcamentoPecaDisplay item = getTableView().getItems().get(getIndex());
                    listaPecasOrcamento.remove(item);
                    pecasNoOrcamentoMap.remove(item.getPeca().getId());
                    calcularTotalOrcamento();
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
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

        servicoAcoesColumn.setCellFactory(param -> new TableCell<OrcamentoServicoDisplay, Button>() {
            final Button deleteButton = new Button("Remover");
            {
                // ALTERADO: Usando getStyleClass().add() em vez de setStyle()
                deleteButton.getStyleClass().add("cancel-button"); // Adiciona a classe CSS
                // Opcional: ajustar padding ou tamanho se o "cancel-button" for muito grande para a célula
                // deleteButton.setStyle("-fx-padding: 2 5 2 5;");
                deleteButton.setOnAction(event -> {
                    OrcamentoServicoDisplay item = getTableView().getItems().get(getIndex());
                    listaServicosOrcamento.remove(item);
                    servicosNoOrcamentoMap.remove(item.getServico().getId());
                    calcularTotalOrcamento();
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
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
            existingItem.setQuantidade(existingItem.getQuantidade() + quantidade);
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
        String valorVeiculoText = valorVeiculoTextField.getText();
        if (valorVeiculoText != null && !valorVeiculoText.trim().isEmpty()) {
            try {
                valorVeiculo = Double.parseDouble(valorVeiculoText.replace(",", ".")); // Handle comma as decimal separator
            } catch (NumberFormatException e) {
                // Optionally show a warning, but don't stop calculation
                System.err.println("Valor do veículo inválido: " + valorVeiculoText);
            }
        }

        double totalGeral = totalPecas + totalServicos + valorVeiculo;
        totalOrcamentoLabel.setText(String.format("Total do Orçamento: R$ %.2f", totalGeral));
    }

    @FXML
    private void confirmarCadastro() {
        Clientes cliente = clienteComboBox.getSelectionModel().getSelectedItem();
        Automoveis automovel = automovelComboBox.getSelectionModel().getSelectedItem();
        LocalDate data = dataDatePicker.getValue();
        double valorVeiculo = 0.0; // Initialize to default value

        if (cliente == null) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Por favor, selecione um cliente.");
            return;
        }

        if (automovel == null) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Por favor, selecione um automóvel.");
            return;
        }

        if (data == null) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Por favor, selecione uma data.");
            return;
        }

        // Validate valorVeiculoTextField
        String valorVeiculoText = valorVeiculoTextField.getText();
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
            // If the field is empty, assume 0.0 or prompt user
            showAlert(Alert.AlertType.WARNING, "Atenção", "O 'Valor Mínimo do Veículo' está vazio. Será considerado R$ 0.00.");
            valorVeiculo = 0.0;
        }

        if (listaPecasOrcamento.isEmpty() && listaServicosOrcamento.isEmpty() && valorVeiculo == 0.0) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "O orçamento deve conter pelo menos uma peça, um serviço ou um valor mínimo de veículo.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cadastro");
        alert.setHeaderText("Confirmar a criação do orçamento?");
        alert.setContentText("Deseja realmente cadastrar este orçamento?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Orcamento novoOrcamento = new Orcamento();
                novoOrcamento.setVeiculo(automovel.getMarca() + " " + automovel.getPlaca());
                novoOrcamento.setValorVeiculo(valorVeiculo); // Set the valorVeiculo
                novoOrcamento.setData(data);
                novoOrcamento.setCliente(cliente);

                List<OrcamentoPeca> orcamentoPecas = listaPecasOrcamento.stream()
                        .map(displayItem -> new OrcamentoPeca(novoOrcamento, displayItem.getPeca(), displayItem.getQuantidade(), displayItem.getValorUnitario()))
                        .collect(Collectors.toList());
                novoOrcamento.setOrcamentoPecas(orcamentoPecas);

                List<OrcamentoServico> orcamentoServicos = listaServicosOrcamento.stream()
                        .map(displayItem -> new OrcamentoServico(novoOrcamento, displayItem.getServico(), displayItem.getQuantidade(), displayItem.getValorUnitario()))
                        .collect(Collectors.toList());
                novoOrcamento.setOrcamentoServicos(orcamentoServicos);

                orcamentoService.salvarOrcamento(novoOrcamento);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Orçamento cadastrado com sucesso!");
                limparFormulario(); // Limpa o formulário após o cadastro
                returnToOrcamentoView(); // Volta para a tela de orçamentos
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar orçamento: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cancelarCadastro() {
        limparFormulario();

        returnToOrcamentoView(); // Adicionado: Chama a função para retornar à tela de pesquisa
    }

    // Adicionado: Método para retornar à tela de orçamentos
    private void returnToOrcamentoView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/OrcamentoView.fxml"));
            Parent orcamentoView = loader.load();

            // Usa o mainPane para obter a cena e a janela atual
            Scene scene = mainPane.getScene();
            Stage stage = (Stage) scene.getWindow();

            scene.setRoot(orcamentoView);
            stage.setTitle("Lista de Orçamentos"); // Defina o título da tela de listagem
            // stage.show(); // Não é necessário chamar show() novamente se já estiver na mesma stage
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de orçamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparFormulario() {
        clienteComboBox.getSelectionModel().clearSelection();
        automovelComboBox.getSelectionModel().clearSelection();
        dataDatePicker.setValue(LocalDate.now());
        valorVeiculoTextField.setText(""); // Clear the new text field
        listaPecasOrcamento.clear();
        listaServicosOrcamento.clear();
        pecasNoOrcamentoMap.clear();
        servicosNoOrcamentoMap.clear();
        calcularTotalOrcamento();
        pecasComboBox.getSelectionModel().clearSelection();
        quantidadePecasTextField.setText("1");
        quantidadePecasTextField.setDisable(true);
        servicosComboBox.getSelectionModel().clearSelection();
        quantidadeServicosTextField.setText("1");
        quantidadeServicosTextField.setDisable(true);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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