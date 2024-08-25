package com.buenrostroasociados.gestion_clientes.service.export;

import com.buenrostroasociados.gestion_clientes.exception.ExportException;
import com.buenrostroasociados.gestion_clientes.service.export.csv.CsvExportService;
import com.buenrostroasociados.gestion_clientes.service.export.pdf.PdfExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ExportServiceImpl implements ExportService{


    private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    private final CsvExportService csvExportService;
    private final PdfExportService pdfExportService;

    @Autowired
    public ExportServiceImpl(CsvExportService csvExportService, PdfExportService pdfExportService) {
        this.csvExportService = csvExportService;
        this.pdfExportService = pdfExportService;
    }

    @Override
    public Resource exportToCSV(List<String> headers, List<List<String>> data) {
        try {
            return csvExportService.exportToCSV(headers, data);
        } catch (Exception e) {
            logger.error("Error al exportar a CSV", e);
            throw new ExportException("Error al exportar a CSV", e);
        }
    }

    @Override
    public Resource exportToPDF(String title, List<String> headers, List<List<String>> data) {
        try {
            return pdfExportService.exportToPDF(title, headers, data);
        } catch (Exception e) {
            logger.error("Error al exportar a PDF", e);
            throw new ExportException("Error al exportar a PDF", e);
        }
    }

}
