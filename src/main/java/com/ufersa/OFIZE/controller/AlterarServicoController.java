package com.ufersa.OFIZE.controller;

import com.ufersa.OFIZE.model.entitie.Servico;
import com.ufersa.OFIZE.model.service.ServicoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination; // Importar KeyCombination

import java.io.IOException;

public class AlterarServicoController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField valorField;
    @FXML
    private Button confirmarButton;
    @FXML
    private Button cancelarButton;

    private ServicoService servicoService;
    private Servico servicoParaAlterar;

    public AlterarServicoController() {
        this.servicoService = new ServicoService();
    }

    public void setServico(Servico servico) {
        this.servicoParaAlterar = servico;
        // Assegure-se de que os campos não são nulos antes de tentar definir o texto
        // initialize() é chamado antes de setServico() quando o FXML é carregado.
        // Então, nomeField e valorField já estarão injetados aqui.
        if (servico != null) {
            if (nomeField != null) { // Adicionado verificação extra para robustez
                nomeField.setText(servico.getNome());
            }
            if (valorField != null) { // Adicionado verificação extra para robustez
                valorField.setText(String.valueOf(servico.getValor()));
            }
        }
    }

    @FXML
    public void initialize() {
        // Lógica de inicialização específica da UI aqui, se houver.
        // Campos FXML como nomeField e valorField já estão injetados neste ponto.
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        // Obtenha a Stage atual para ser o owner dos alertas
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        if (servicoParaAlterar == null) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro", "Nenhum serviço selecionado para alteração.");
            return;
        }

        try {
            servicoParaAlterar.setNome(nomeField.getText());
            servicoParaAlterar.setValor(Double.parseDouble(valorField.getText()));

            servicoService.atualizarServico(servicoParaAlterar);
            showAlert(currentStage, Alert.AlertType.INFORMATION, "Sucesso", "Serviço alterado com sucesso!");

            returnToServicoListView(event);

        } catch (NumberFormatException e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Entrada", "O valor do serviço deve ser um número válido.");
        } catch (IllegalArgumentException e) {
            showAlert(currentStage, Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro ao alterar serviço ", "Não foi possível alterar o serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        returnToServicoListView(event);
    }

    private void returnToServicoListView(ActionEvent event) {
        try {
            // Obtenha a Stage atual para a transição
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Carregue o FXML da tela de Serviços. CONFIRME O NOME EXATO DO ARQUIVO FXML.
            // Se for 'servico_view.fxml', ajuste aqui.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufersa/OFIZE/view/ServicoView.fxml")); // Ex: alterado para 'servico_view.fxml'
            Parent servicoListView = loader.load();

            // Configure a nova cena
            Scene scene = new Scene(servicoListView);
            stage.setScene(scene);
            stage.setTitle("Pesquisar Serviços");

            // *** Configurações de Tela Cheia ***
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("Pressione ESC para sair da tela cheia"); // Dica para o usuário
            stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("ESC")); // Combinação de tecla para sair

            stage.show();

        } catch (IOException e) {
            // Se houver erro ao carregar a tela, ainda mostre o alerta na stage atual.
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            showAlert(currentStage, Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de pesquisa de serviços.");
            e.printStackTrace();
        }
    }

    // *** MÉTODO showAlert MODIFICADO ***
    // Agora aceita a Stage owner como primeiro parâmetro
    private void showAlert(Stage owner, Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Define o owner do alerta para a Stage principal, para que ele apareça sobre ela
        if (owner != null) {
            alert.initOwner(owner);
        } else {
            System.err.println("AVISO: Stage owner do alerta é nula. O alerta pode não ser exibido corretamente.");
        }
        alert.showAndWait();
    }
}