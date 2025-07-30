package com.ufersa.OFIZE.utils.strategy;

import com.ufersa.OFIZE.model.entitie.Orcamento;
import java.io.IOException;
import java.util.List;

public interface ReportExporterStrategy {
    void export(List<Orcamento> data, String filePath) throws IOException;
}