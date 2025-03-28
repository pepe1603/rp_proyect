package com.buenrostroasociados.gestion_clientes.service.export;

import org.springframework.core.io.Resource;

import java.util.List;

public interface ExportService {
    Resource exportToCSV(List<String> headers, List<List<String>> data);

    Resource exportToPDF(String title, List<String> headers, List<List<String>> data);
}
