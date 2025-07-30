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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class AlterarOrcamentoController {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ComboBox<Automoveis> automovelComboBox;
    @FXML
    private ComboBox<Clientes> clienteComboBox;
    @FXML
    private DatePicker dataPicker;
    @FXML
    private TextField valorVeiculoField;

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

    // NOVO: Mapa para armazenar as quantidades originais das peças no orçamento carregado
    private Map<Long, Integer> originalPecasQuantitiesSnapshot = new HashMap<>();


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

        // NOVO: Desabilita o ComboBox de automóveis inicialmente
        automovelComboBox.setDisable(true);
        // NOVO: Adiciona um listener para a seleção do cliente
        clienteComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Cliente selecionado: habilita e carrega os automóveis
                automovelComboBox.setDisable(false);
                carregarAutomoveisPorCliente(newVal);
            } else {
                // Nenhum cliente selecionado: desabilita e limpa os automóveis
                automovelComboBox.setDisable(true);
                automovelComboBox.setItems(FXCollections.observableArrayList()); // Limpa os itens
                automovelComboBox.getSelectionModel().clearSelection(); // Limpa a seleção
            }
        });

        // Configuração do converter para automovelComboBox
        automovelComboBox.setConverter(new StringConverter<Automoveis>() {
            @Override
            public String toString(Automoveis automovel) {
                return automovel != null ? automovel.getMarca() + " - " + automovel.getPlaca() : "";
            }

            @Override
            public Automoveis fromString(String string) {
                // Não é usado para seleção via ComboBox, mas necessário para a interface StringConverter
                // Se você for permitir digitação livre no ComboBox, precisaria implementar isso
                return null;
            }
        });

        calcularTotalOrcamento();
    }

    public void setOrcamentoParaAlterar(Orcamento orcamento) {
        this.orcamentoToAlter = orcamento;
        if (orcamentoToAlter != null) {
            dataPicker.setValue(orcamentoToAlter.getData());
            valorVeiculoField.setText(String.format("%.2f", orcamentoToAlter.getValorVeiculo()));

            if (orcamentoToAlter.getCliente() != null) {
                clienteComboBox.getSelectionModel().select(orcamentoToAlter.getCliente());

                // NOVO: Após selecionar o cliente, carregue os automóveis
                // Isso é chamado pelo listener do clienteComboBox, mas pode ser chamado explicitamente também
                carregarAutomoveisPorCliente(orcamentoToAlter.getCliente());

                // Tenta selecionar o automóvel correto no ComboBox
                String veiculoOrcamentoStr = orcamentoToAlter.getVeiculo();
                System.out.println("Veículo do Orçamento (string): " + veiculoOrcamentoStr); // Para depuração

                if (veiculoOrcamentoStr != null && !veiculoOrcamentoStr.isEmpty()) {
                    // Normaliza a string do orçamento para comparação
                    String normalizedOrcamentoVeiculo = veiculoOrcamentoStr.toLowerCase().replaceAll("[^a-z0-9]", ""); // Remove não alfanuméricos

                    Automoveis autoParaSelecionar = null;
                    for (Automoveis auto : automovelComboBox.getItems()) {
                        // Normaliza a string do automóvel para comparação
                        String normalizedAutoString = (auto.getMarca() + auto.getPlaca()).toLowerCase().replaceAll("[^a-z0-9]", ""); // Concatena e normaliza

                        if (normalizedAutoString.equals(normalizedOrcamentoVeiculo)) {
                            autoParaSelecionar = auto;
                            break;
                        }
                    }
                    if (autoParaSelecionar != null) {
                        automovelComboBox.getSelectionModel().select(autoParaSelecionar);
                        System.out.println("Veículo selecionado no ComboBox: " + autoParaSelecionar.getMarca() + " - " + autoParaSelecionar.getPlaca()); // Para depuração
                    } else {
                        System.out.println("Automóvel do orçamento não encontrado na lista de veículos do cliente."); // Para depuração
                    }
                }
            } else {
                clienteComboBox.getSelectionModel().clearSelection();
                automovelComboBox.getSelectionModel().clearSelection(); // Limpa também o automóvel
                automovelComboBox.setDisable(true); // Desabilita se não houver cliente
                automovelComboBox.setItems(FXCollections.observableArrayList()); // Limpa os itens
            }

            listaPecasOrcamento.clear();
            pecasNoOrcamentoMap.clear();
            originalPecasQuantitiesSnapshot.clear(); // Limpa o snapshot anterior a cada carregamento

            if (orcamentoToAlter.getOrcamentoPecas() != null) {
                for (OrcamentoPeca op : orcamentoToAlter.getOrcamentoPecas()) {
                    OrcamentoPecaDisplay displayItem = new OrcamentoPecaDisplay(op.getPeca(), op.getQuantidade(), op.getValorUnitario());
                    listaPecasOrcamento.add(displayItem);
                    pecasNoOrcamentoMap.put(op.getPeca().getId(), displayItem);
                    // NOVO: Armazena a quantidade original desta peça
                    originalPecasQuantitiesSnapshot.put(op.getPeca().getId(), op.getQuantidade());
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

    private void carregarAutomoveisPorCliente(Clientes cliente) {
        if (cliente != null) {
            List<Automoveis> automoveisDoCliente = automovelService.buscarAutomoveisPorCliente(cliente);
            automovelComboBox.setItems(FXCollections.observableArrayList(automoveisDoCliente));
            System.out.println("Automóveis carregados para o cliente " + cliente.getNome() + ": " + automoveisDoCliente.size()); // Para depuração
        } else {
            automovelComboBox.setItems(FXCollections.observableArrayList()); // Limpa se nenhum cliente
            System.out.println("Nenhum cliente selecionado, limpando automóveis."); // Para depuração
        }
        automovelComboBox.getSelectionModel().clearSelection(); // Sempre limpa a seleção anterior antes de carregar novos
    }

    private void carregarPecasComboBox() {
        List<Pecas> pecas = pecasService.buscarTodasPecas(); // Nome do método corrigido
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
            // REMOVIDO: private final Button editButton = new Button("Editar");

            {
                removeButton.getStyleClass().add("cancel-button");
                removeButton.setPadding(new Insets(3, 5, 3, 5));

                // REMOVIDO: editButton.getStyleClass().add("edit-button");
                // REMOVIDO: editButton.setPadding(new Insets(3, 5, 3, 5));

                HBox pane = new HBox(5, removeButton); // REMOVIDO: editButton
                pane.setAlignment(Pos.CENTER);

                removeButton.setOnAction(event -> {
                    OrcamentoPecaDisplay item = getTableView().getItems().get(getIndex());
                    listaPecasOrcamento.remove(item);
                    pecasNoOrcamentoMap.remove(item.getPeca().getId());
                    calcularTotalOrcamento();
                    // Nenhuma atualização de estoque aqui no controller
                });

                // REMOVIDO: editButton.setOnAction(event -> { ... }); // Removida a lógica de edição da célula
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, removeButton)); // REMOVIDO: editButton
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

            {
                removeButton.getStyleClass().add("cancel-button");
                removeButton.setPadding(new Insets(3, 5, 3, 5));

                HBox pane = new HBox(5, removeButton);
                pane.setAlignment(Pos.CENTER);

                removeButton.setOnAction(event -> {
                    OrcamentoServicoDisplay item = getTableView().getItems().get(getIndex());
                    listaServicosOrcamento.remove(item);
                    servicosNoOrcamentoMap.remove(item.getServico().getId());
                    calcularTotalOrcamento();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, removeButton));
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

        Pecas pecaReal = pecasService.buscarPeca(pecaSelecionada.getId()); // Sempre busca a peça mais atualizada do banco
        if (pecaReal == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Peça não encontrada no estoque.");
            return;
        }

        // Soma a quantidade já existente no orçamento para a peça
        int currentQuantityInOrcamento = pecasNoOrcamentoMap.containsKey(pecaReal.getId()) ? pecasNoOrcamentoMap.get(pecaReal.getId()).getQuantidade() : 0;
        int totalQuantityRequested = currentQuantityInOrcamento + quantidade;

        if (pecaReal.getQuantidade() < totalQuantityRequested) { // Verifica se a SOMA excede o estoque
            showAlert(Alert.AlertType.ERROR, "Estoque Insuficiente", "Não há estoque suficiente para a peça selecionada. Em estoque: " + pecaReal.getQuantidade() + ". Quantidade já no orçamento: " + currentQuantityInOrcamento + ".");
            return;
        }

        OrcamentoPecaDisplay existingItem = pecasNoOrcamentoMap.get(pecaReal.getId());
        if (existingItem != null) {
            existingItem.setQuantidade(totalQuantityRequested); // Atualiza com a quantidade total
            pecasTableView.refresh();
        } else {
            OrcamentoPecaDisplay newDisplayItem = new OrcamentoPecaDisplay(pecaReal, quantidade, pecaReal.getPreco());
            listaPecasOrcamento.add(newDisplayItem);
            pecasNoOrcamentoMap.put(pecaReal.getId(), newDisplayItem);
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
            Clientes clienteSelecionado = clienteComboBox.getSelectionModel().getSelectedItem();
            Automoveis automovelSelecionado = automovelComboBox.getSelectionModel().getSelectedItem();

            if (automovelSelecionado == null || dataPicker.getValue() == null || clienteSelecionado == null) {
                showAlert(Alert.AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha todos os campos obrigatórios (Automóvel, Cliente, Data).");
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

            // Lógica para aplicar as alterações de estoque de peças
            // Esta lógica garante que o estoque seja atualizado de acordo com as diferenças
            // entre o orçamento original e o orçamento atualizado.
            // É crucial que esta parte seja executada dentro de uma transação no seu Service/DAO.
            try {
                // Crie um mapa das quantidades atuais para fácil acesso
                Map<Long, Integer> currentPecasQuantities = new HashMap<>();
                for (OrcamentoPecaDisplay displayItem : listaPecasOrcamento) {
                    currentPecasQuantities.put(displayItem.getPeca().getId(), displayItem.getQuantidade());
                }

                // 1. Processar peças que estavam no orçamento original
                for (Map.Entry<Long, Integer> entry : originalPecasQuantitiesSnapshot.entrySet()) {
                    Long pecaId = entry.getKey();
                    Integer originalQuantity = entry.getValue();
                    Integer currentQuantity = currentPecasQuantities.get(pecaId);

                    Pecas pecaEmEstoque = pecasService.buscarPeca(pecaId); // Busca a peça para ter a quantidade atual do DB
                    if (pecaEmEstoque == null) {
                        System.err.println("Erro: Peça com ID " + pecaId + " não encontrada no estoque durante a confirmação.");
                        // Considere lançar uma exceção ou exibir um erro fatal aqui
                        continue;
                    }

                    if (currentQuantity == null) { // Peça foi removida completamente do orçamento
                        // Incrementa o estoque com a quantidade original
                        pecasService.incrementarQuantidade(pecaId, originalQuantity);
                    } else { // Peça ainda está no orçamento, pode ter tido a quantidade alterada
                        int diff = currentQuantity - originalQuantity;
                        if (diff > 0) { // Quantidade da peça no orçamento aumentou -> decrementa o estoque
                            pecasService.decrementarQuantidade(pecaId, diff);
                        } else if (diff < 0) { // Quantidade da peça no orçamento diminuiu -> incrementa o estoque
                            pecasService.incrementarQuantidade(pecaId, Math.abs(diff));
                        }
                        // Se diff é 0, não houve mudança, não faz nada.
                    }
                }

                // 2. Processar peças que foram *novamente adicionadas* ao orçamento (e não estavam no original)
                for (OrcamentoPecaDisplay displayItem : listaPecasOrcamento) {
                    Long pecaId = displayItem.getPeca().getId();
                    if (!originalPecasQuantitiesSnapshot.containsKey(pecaId)) { // É uma peça que não estava no orçamento original
                        // Decrementa o estoque com a quantidade adicionada
                        pecasService.decrementarQuantidade(pecaId, displayItem.getQuantidade());
                    }
                }

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Estoque", "Não foi possível atualizar o estoque de peças: " + e.getMessage());
                e.printStackTrace();
                // É fundamental que o serviço de orçamento gerencie a transação para um rollback em caso de falha no estoque
                return;
            }


            // IMPORTANTE: Ao confirmar, salve a string do veículo no formato padronizado!
            // Isso evita futuros problemas de formatação.
            orcamentoToAlter.setVeiculo(automovelSelecionado.getMarca() + " - " + automovelSelecionado.getPlaca());
            orcamentoToAlter.setCliente(clienteSelecionado);
            orcamentoToAlter.setData(dataPicker.getValue());
            orcamentoToAlter.setValorVeiculo(valorVeiculo);

            List<OrcamentoPeca> orcamentoPecasAtualizadas = listaPecasOrcamento.stream()
                    .map(displayItem -> {
                        // Certifique-se de que o OrcamentoPeca recém-criado tenha o Orcamento pai associado
                        OrcamentoPeca op = new OrcamentoPeca(orcamentoToAlter, displayItem.getPeca(), displayItem.getQuantidade(), displayItem.getValorUnitario());
                        // Se houver um ID existente para a OrcamentoPeca original (para manter associações JPA)
                        // Você pode tentar recuperá-lo aqui ou deixar o JPA gerenciar novas e existentes.
                        // Por simplicidade, assumindo que OrcamentoPeca é recriado a cada atualização.
                        return op;
                    })
                    .collect(Collectors.toList());
            orcamentoToAlter.setOrcamentoPecas(orcamentoPecasAtualizadas);

            List<OrcamentoServico> orcamentoServicosAtualizadas = listaServicosOrcamento.stream()
                    .map(displayItem -> {
                        OrcamentoServico os = new OrcamentoServico(orcamentoToAlter, displayItem.getServico(), displayItem.getQuantidade(), displayItem.getValorUnitario());
                        return os;
                    })
                    .collect(Collectors.toList());
            orcamentoToAlter.setOrcamentoServicos(orcamentoServicosAtualizadas);

            // Chame o serviço para atualizar o orçamento no banco de dados
            orcamentoService.atualizarOrcamento(orcamentoToAlter);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Orçamento atualizado com sucesso!");
            voltarParaPesquisa(event); // Retorna à tela anterior
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar orçamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        // Ao cancelar, simplesmente retorna para a tela de pesquisa.
        // A lógica de reversão do estoque (se algo tivesse sido pré-reservado)
        // deveria ser tratada no serviço ou na camada de persistência em uma transação.
        voltarParaPesquisa(event);
    }

    private void voltarParaPesquisa(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/OrcamentoView.fxml"));
            Parent orcamentoView = loader.load();

            Scene scene = mainPane.getScene();
            Stage stage = (Stage) scene.getWindow();

            scene.setRoot(orcamentoView);
            stage.setTitle("Lista de Orçamentos");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de pesquisa de orçamento.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Classes Display (mantidas como antes)
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
        public void setQuantidade(int quantidade) {
            this.quantidade.set(quantidade);
        }

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
        public void setQuantidade(int quantidade) {
            this.quantidade.set(quantidade);
        }

        public String getNomeServico() { return nomeServico.get(); }
        public javafx.beans.property.StringProperty nomeServicoProperty() { return nomeServico; }

        public double getValorUnitario() { return valorUnitario; }

        public double getSubtotal() { return this.valorUnitario * quantidade.get(); }
    }
}