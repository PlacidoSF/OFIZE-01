// src/main/java/com/ufersa.OFIZE.model.controller/OrcamentoController.java
/*package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Automoveis;
import com.ufersa.OFIZE.model.entitie.Orcamento;
import com.ufersa.OFIZE.model.entitie.OrcamentoPeca;
import com.ufersa.OFIZE.model.entitie.OrcamentoServico;
import com.ufersa.OFIZE.model.entitie.Pecas;
import com.ufersa.OFIZE.model.entitie.Servico;

import com.ufersa.OFIZE.model.service.AutomoveisService;
import com.ufersa.OFIZE.model.service.PecasService;
import com.ufersa.OFIZE.model.service.ServicoService;
import com.ufersa.OFIZE.model.service.OrcamentoService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrcamentoController {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private ComboBox<Automoveis> automovelComboBox;
    @FXML
    private DatePicker dataDatePicker;

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

        dataDatePicker.setValue(LocalDate.now());

        // --- Configuração dos ComboBoxes ---
        // Automóveis
        List<Automoveis> automoveis = automovelService.buscarTodos();
        automovelComboBox.setItems(FXCollections.observableArrayList(automoveis));
        automovelComboBox.setConverter(new StringConverter<Automoveis>() {
            @Override
            public String toString(Automoveis automovel) {
                return automovel != null ? automovel.getPlaca() + " - " + automovel.getMarca() : "";
            }
            @Override
            public Automoveis fromString(String string) { return null; }
        });

        // Peças
        List<Pecas> todasPecas = pecasService.buscarTodas();
        pecasComboBox.setItems(FXCollections.observableArrayList(todasPecas));
        pecasComboBox.setConverter(new StringConverter<Pecas>() {
            @Override
            public String toString(Pecas pecas) {
                return pecas != null ? pecas.getNome() + " (R$" + String.format("%.2f", pecas.getPreco()) + ")" : "";
            }
            @Override
            public Pecas fromString(String string) { return null; }
        });

        // Serviços
        List<Servico> todosServicos = servicoService.listarTodosServicos();
        servicosComboBox.setItems(FXCollections.observableArrayList(todosServicos));
        servicosComboBox.setConverter(new StringConverter<Servico>() {
            @Override
            public String toString(Servico servico) {
                return servico != null ? servico.getNome() + " (R$" + String.format("%.2f", servico.getValor()) + ")" : "";
            }
            @Override
            public Servico fromString(String string) { return null; }
        });

        // --- Configuração das TableView ---
        // Tabela de Peças
        pecaNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nomePeca"));
        pecaQuantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        pecaPrecoUnitarioColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getValorUnitario())));
        pecaSubtotalColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getSubtotal())));

        pecaAcoesColumn.setCellFactory(param -> new TableCell<OrcamentoPecaDisplay, Button>() {
            final Button deleteButton = new Button("Remover");
            {
                deleteButton.setOnAction(event -> {
                    OrcamentoPecaDisplay item = getTableView().getItems().get(getIndex());
                    removerPecaDoOrcamento(item);
                });
            }
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(deleteButton);
                    setText(null);
                }
            }
        });
        pecasTableView.setItems(listaPecasOrcamento);

        // Tabela de Serviços
        servicoNomeColumn.setCellValueFactory(new PropertyValueFactory<>("nomeServico"));
        servicoQuantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        servicoPrecoUnitarioColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getValorUnitario())));
        servicoSubtotalColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getSubtotal())));

        servicoAcoesColumn.setCellFactory(param -> new TableCell<OrcamentoServicoDisplay, Button>() {
            final Button deleteButton = new Button("Remover");
            {
                deleteButton.setOnAction(event -> {
                    OrcamentoServicoDisplay item = getTableView().getItems().get(getIndex());
                    removerServicoDoOrcamento(item);
                });
            }
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(deleteButton);
                    setText(null);
                }
            }
        });
        servicosTableView.setItems(listaServicosOrcamento);

        quantidadeServicosTextField.setText("1");
        atualizarTotalOrcamento();
    }

    @FXML
    private void adicionarPecaAoOrcamento() {
        Pecas pecaSelecionada = pecasComboBox.getSelectionModel().getSelectedItem();
        String quantidadeText = quantidadePecasTextField.getText();

        if (pecaSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione uma peça.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeText);
            if (quantidade <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Quantidade Inválida", "Por favor, insira uma quantidade válida (número inteiro positivo).");
            return;
        }

        OrcamentoPecaDisplay itemExistente = pecasNoOrcamentoMap.get(pecaSelecionada.getId());

        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
            showAlert(Alert.AlertType.INFORMATION, "Peça já adicionada", "Quantidade da peça '" + pecaSelecionada.getNome() + "' atualizada para " + itemExistente.getQuantidade() + ".");
            pecasTableView.refresh();
        } else {
            OrcamentoPecaDisplay novoItem = new OrcamentoPecaDisplay(pecaSelecionada, quantidade, pecaSelecionada.getPreco());
            listaPecasOrcamento.add(novoItem);
            pecasNoOrcamentoMap.put(pecaSelecionada.getId(), novoItem);
        }

        quantidadePecasTextField.clear();
        pecasComboBox.getSelectionModel().clearSelection();
        atualizarTotalOrcamento();
    }

    private void removerPecaDoOrcamento(OrcamentoPecaDisplay item) {
        listaPecasOrcamento.remove(item);
        pecasNoOrcamentoMap.remove(item.getPecas().getId());
        atualizarTotalOrcamento();
    }

    @FXML
    private void adicionarServicoAoOrcamento() {
        Servico servicoSelecionado = servicosComboBox.getSelectionModel().getSelectedItem();
        String quantidadeText = quantidadeServicosTextField.getText();

        if (servicoSelecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione um serviço.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeText);
            if (quantidade <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Quantidade Inválida", "Por favor, insira uma quantidade válida (número inteiro positivo).");
            return;
        }

        OrcamentoServicoDisplay itemExistente = servicosNoOrcamentoMap.get(servicoSelecionado.getId());

        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
            showAlert(Alert.AlertType.INFORMATION, "Serviço já adicionado", "Quantidade do serviço '" + servicoSelecionado.getNome() + "' atualizada para " + itemExistente.getQuantidade() + ".");
            servicosTableView.refresh();
        } else {
            OrcamentoServicoDisplay novoItem = new OrcamentoServicoDisplay(servicoSelecionado, quantidade, servicoSelecionado.getValor());
            listaServicosOrcamento.add(novoItem);
            servicosNoOrcamentoMap.put(servicoSelecionado.getId(), novoItem);
        }

        quantidadeServicosTextField.setText("1");
        servicosComboBox.getSelectionModel().clearSelection();
        atualizarTotalOrcamento();
    }

    private void removerServicoDoOrcamento(OrcamentoServicoDisplay item) {
        listaServicosOrcamento.remove(item);
        servicosNoOrcamentoMap.remove(item.getServico().getId());
        atualizarTotalOrcamento();
    }

    private void atualizarTotalOrcamento() {
        double total = 0.0;
        for (OrcamentoPecaDisplay item : listaPecasOrcamento) {
            total += item.getSubtotal();
        }
        for (OrcamentoServicoDisplay item : listaServicosOrcamento) {
            total += item.getSubtotal();
        }
        totalOrcamentoLabel.setText("Total do Orçamento: R$ " + String.format("%.2f", total));
    }

    @FXML
    private void confirmarCadastro() {
        Automoveis automovelSelecionado = automovelComboBox.getSelectionModel().getSelectedItem();
        LocalDate dataSelecionada = dataDatePicker.getValue();

        if (automovelSelecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Dados Inválidos", "Por favor, selecione um automóvel.");
            return;
        }
        if (dataSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Dados Inválidos", "Por favor, selecione uma data para o orçamento.");
            return;
        }
        if (listaPecasOrcamento.isEmpty() && listaServicosOrcamento.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Orçamento Vazio", "Adicione pelo menos uma peça ou um serviço ao orçamento.");
            return;
        }

        Orcamento novoOrcamento = new Orcamento();
        novoOrcamento.setAutomovel(automovelSelecionado);
        novoOrcamento.setDataOrcamento(dataSelecionada);
        // O cliente do orçamento será o proprietário do automóvel (assumindo que Automoveis tem getProprietario())
        // novoOrcamento.setCliente(automovelSelecionado.getProprietario());
        novoOrcamento.setStatusPago(false);

        // Converte OrcamentoPecaDisplay para OrcamentoPeca
        List<OrcamentoPeca> itensPecasParaSalvar = listaPecasOrcamento.stream()
                .map(opd -> new OrcamentoPeca(novoOrcamento, opd.getPecas(), opd.getQuantidade(), opd.getValorUnitario()))
                .collect(Collectors.toList());

        // Converte OrcamentoServicoDisplay para OrcamentoServico
        List<OrcamentoServico> itensServicosParaSalvar = listaServicosOrcamento.stream()
                .map(osd -> new OrcamentoServico(novoOrcamento, osd.getServico(), osd.getQuantidade(), osd.getValorUnitario()))
                .collect(Collectors.toList());

        try {
            orcamentoService.salvarOrcamento(novoOrcamento, itensPecasParaSalvar, itensServicosParaSalvar);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Orçamento cadastrado com sucesso!");
            limparFormulario();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        }
        catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Salvar", "Não foi possível cadastrar o orçamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelarCadastro() {
        limparFormulario();
        showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Cadastro de orçamento cancelado.");
    }

    private void limparFormulario() {
        automovelComboBox.getSelectionModel().clearSelection();
        dataDatePicker.setValue(LocalDate.now());
        pecasComboBox.getSelectionModel().clearSelection();
        quantidadePecasTextField.clear();
        servicosComboBox.getSelectionModel().clearSelection();
        quantidadeServicosTextField.setText("1");
        listaPecasOrcamento.clear();
        listaServicosOrcamento.clear();
        pecasNoOrcamentoMap.clear();
        servicosNoOrcamentoMap.clear();
        atualizarTotalOrcamento();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Classes Helper para TableView ---
    public static class OrcamentoPecaDisplay {
        private final Pecas pecas;
        private javafx.beans.property.IntegerProperty quantidade;
        private javafx.beans.property.StringProperty nomePeca;
        private double valorUnitario;

        public OrcamentoPecaDisplay(Pecas pecas, int quantidade, double valorUnitario) {
            this.pecas = pecas;
            this.quantidade = new javafx.beans.property.SimpleIntegerProperty(quantidade);
            this.nomePeca = new javafx.beans.property.SimpleStringProperty(pecas.getNome());
            this.valorUnitario = valorUnitario;
        }

        public Pecas getPecas() { return pecas; }
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
}*/