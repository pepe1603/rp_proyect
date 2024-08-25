package com.buenrostroasociados.gestion_clientes.service.export.pdf;

import org.springframework.core.io.Resource;

import java.util.List;

public interface PdfExportService {

    Resource exportToPDF(String title, List<String> headers, List<List<String>> data);
}
