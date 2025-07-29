package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Clientes;
import com.ufersa.OFIZE.model.service.ClientesService;
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

public class PesquisarClienteController {

    @FXML private TextField searchField;
    @FXML private VBox clientesContainer;
    @FXML private Button cadastrarClienteButton;
    @FXML private VBox menuHamburguer;

    private final ClientesService clientesService = new ClientesService();

    @FXML
    public void initialize() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarClientes(newValue);
        });
        menuHamburguer.setOnMouseClicked(this::handleVoltarAoMenu);
        buscarClientes("");
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

    private void buscarClientes(String nome) {
        clientesContainer.getChildren().clear();
        List<Clientes> clientesEncontrados = clientesService.pesquisarPorNome(nome);
        for (Clientes cliente : clientesEncontrados) {
            clientesContainer.getChildren().add(criarLinhaCliente(cliente));
        }
    }

    private HBox criarLinhaCliente(Clientes cliente) {
        Label nomeLabel = new Label(cliente.getNome());
        nomeLabel.setPrefWidth(250.0);
        nomeLabel.getStyleClass().add("client-data");

        Label cpfLabel = new Label(cliente.getCpf());
        cpfLabel.setPrefWidth(150.0);
        cpfLabel.getStyleClass().add("client-data");
        cpfLabel.setAlignment(Pos.CENTER);

        Label enderecoLabel = new Label(cliente.getEndereco());
        enderecoLabel.setPrefWidth(300.0);
        enderecoLabel.getStyleClass().add("client-data");

        Button editButton = new Button();
        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/alterar.png")));
        editIcon.setFitHeight(20);
        editIcon.setFitWidth(20);
        editButton.setGraphic(editIcon);
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(event -> abrirTelaDeAlteracao(cliente));

        Button deleteButton = new Button();
        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/deletar.png")));
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(20);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> deletarCliente(cliente));

        HBox botoesBox = new HBox(10, editButton, deleteButton);
        botoesBox.setAlignment(Pos.CENTER);
        botoesBox.setPrefWidth(100.0);

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        HBox linha = new HBox(nomeLabel, cpfLabel, enderecoLabel, espacador, botoesBox);
        linha.getStyleClass().add("client-row");
        return linha;
    }

    private void deletarCliente(Clientes cliente) {
        boolean possuiAutomoveis = clientesService.clientePossuiAutomoveis(cliente.getId());
        String tituloAlerta = "Você realmente quer apagar este Cliente?";
        String conteudoAlerta = "Cliente: " + cliente.getNome();

        if (possuiAutomoveis) {
            tituloAlerta = "Atenção: Cliente possui automóveis!";
            conteudoAlerta = "Se continuar, o cliente E TODOS os seus automóveis serão apagados permanentemente. Deseja continuar?";
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText(tituloAlerta);
        alert.setContentText(conteudoAlerta);

        ButtonType simButton = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
        ButtonType naoButton = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(simButton, naoButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == simButton) {
            try {
                clientesService.removerCliente(cliente);
                buscarClientes(searchField.getText());
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erro ao deletar: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void abrirTelaDeAlteracao(Clientes cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/alterar_cliente.fxml"));
            Parent root = loader.load();

            AlterarClienteController controller = loader.getController();
            controller.setClienteParaAlterar(cliente);

            Scene scene = searchField.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCadastrarCliente() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ufersa/OFIZE/view/cadastrar_cliente.fxml"));
            Scene scene = cadastrarClienteButton.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}