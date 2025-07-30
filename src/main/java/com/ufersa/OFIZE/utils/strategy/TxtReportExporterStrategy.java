package com.ufersa.OFIZE.utils.strategy;

import com.ufersa.OFIZE.model.entitie.Orcamento;
import com.ufersa.OFIZE.utils.TxtGeneratorService;
import java.io.IOException;
import java.util.List;

public class TxtReportExporterStrategy implements ReportExporterStrategy {

    @Override
    public void export(List<Orcamento> data, String filePath) throws IOException {
        TxtGeneratorService.gerarRelatorioOrcamentosTxt(data, filePath);
    }
}