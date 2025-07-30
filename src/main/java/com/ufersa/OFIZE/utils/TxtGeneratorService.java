package com.ufersa.OFIZE.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.ufersa.OFIZE.model.entitie.Orcamento; // Sua classe de orçamento

public class TxtGeneratorService {

    public static void gerarRelatorioOrcamentosTxt(
            List<Orcamento> orcamentos,
            String filePath) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Escrever um cabeçalho para o relatório
            writer.write("Relatório de Orçamentos\n");
            writer.write("========================\n\n");

            // Escrever o cabeçalho da 'tabela' no TXT
            writer.write(String.format("%-12s %-25s %-20s %-15s %-10s %-10s\n",
                    "Data", "Cliente", "Veículo", "Valor Total", "Status", "Pago"));
            writer.write("------------------------------------------------------------------------------------------\n");

            // Iterar sobre a lista de orçamentos e escrever cada um
            for (Orcamento orcamento : orcamentos) {
                String data = orcamento.getData() != null ? orcamento.getData().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
                String cliente = orcamento.getCliente() != null ? orcamento.getCliente().getNome() : "";
                String veiculo = orcamento.getVeiculo() != null ? orcamento.getVeiculo() : "";
                String valorTotal = String.format("R$ %.2f", orcamento.getValorTotal());
                String status = orcamento.isStatus() ? "Concluído" : "Pendente";
                String pago = orcamento.isPago() ? "Concluído" : "Pendente";

                writer.write(String.format("%-12s %-25s %-20s %-15s %-10s %-10s\n",
                        data, cliente, veiculo, valorTotal, status, pago));
            }

            // Calcular e escrever o total geral
            double totalGeral = orcamentos.stream().mapToDouble(Orcamento::getValorTotal).sum();
            writer.write("\n------------------------------------------------------------------------------------------\n");
            writer.write(String.format("Total Geral dos Orçamentos Filtrados: R$ %.2f\n", totalGeral));

            writer.flush(); // Garante que todos os dados sejam gravados
        }
    }
}
