package com.cms.audit.api.Common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.convertDateToRoman;

public class reportDashboard {
    public static String SHEET_NAME_SOP = "Kategori SOP";
    public static String SHEET_NAME_DIVISI = "Divisi";
    public static String SHEET_NAME_AUDIT = "Audit";

    public static ByteArrayInputStream dataToExcel(List<Clarification> clarificationsList, Long month) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            createKategoriSopSheet(workbook, clarificationsList, month);
            createDivisiSheet(workbook, clarificationsList, month);
            createAuditSheet(workbook, clarificationsList, month);

            workbook.write(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } finally {
            workbook.close();
            byteArrayOutputStream.close();
        }
    }

    private static void createKategoriSopSheet(Workbook workbook, List<Clarification> clarificationsList, Long month) {
        Sheet sheet = workbook.createSheet(SHEET_NAME_SOP);

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Bulan");
        headerRow.createCell(1).setCellValue("Kriteria Temuan");
        headerRow.createCell(2).setCellValue("Jumlah Temuan");
        headerRow.createCell(3).setCellValue("Nominal Temuan");

        // Group clarifications by case category
        Map<String, AggregatedData> aggregatedDataMap = new HashMap<>();
        for (Clarification clarification : clarificationsList) {
            String categoryName = clarification.getCaseCategory().getName();
            aggregatedDataMap.putIfAbsent(categoryName, new AggregatedData());

            AggregatedData data = aggregatedDataMap.get(categoryName);
            data.count++;
            if (clarification.getNominal_loss() != null) {
                data.totalNominalLoss += clarification.getNominal_loss();
            }
        }

        // Populate data
        int rowIndex = 1;
        for (Map.Entry<String, AggregatedData> entry : aggregatedDataMap.entrySet()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(convertDateToRoman.getMonthName(month));
            row.createCell(1).setCellValue(entry.getKey());
            row.createCell(2).setCellValue(entry.getValue().count);
            row.createCell(3).setCellValue(entry.getValue().totalNominalLoss);
        }
    }

    private static void createDivisiSheet(Workbook workbook, List<Clarification> clarificationsList, Long month) {
        Sheet sheet = workbook.createSheet(SHEET_NAME_DIVISI);

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Bulan");
        headerRow.createCell(1).setCellValue("Divisi");
        headerRow.createCell(2).setCellValue("Jumlah Temuan");
        headerRow.createCell(3).setCellValue("Nominal Temuan");

        // Group clarifications by case division
        Map<String, AggregatedData> aggregatedDataMap = new HashMap<>();
        for (Clarification clarification : clarificationsList) {
            String divisionName = clarification.getCases().getName(); // Assuming cases.getName() returns the division name
            aggregatedDataMap.putIfAbsent(divisionName, new AggregatedData());

            AggregatedData data = aggregatedDataMap.get(divisionName);
            data.count++;
            if (clarification.getNominal_loss() != null) {
                data.totalNominalLoss += clarification.getNominal_loss();
            }
        }

        // Populate data
        int rowIndex = 1;
        for (Map.Entry<String, AggregatedData> entry : aggregatedDataMap.entrySet()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(convertDateToRoman.getMonthName(month));
            row.createCell(1).setCellValue(entry.getKey());
            row.createCell(2).setCellValue(entry.getValue().count);
            row.createCell(3).setCellValue(entry.getValue().totalNominalLoss);
        }
    }

    private static void createAuditSheet(Workbook workbook, List<Clarification> clarificationsList, Long month) {
        Sheet sheet = workbook.createSheet(SHEET_NAME_AUDIT);

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Bulan");
        headerRow.createCell(1).setCellValue("Auditor");
        headerRow.createCell(2).setCellValue("Jumlah Temuan");

        // Group clarifications by auditor
        Map<String, Integer> auditCountMap = new HashMap<>();
        for (Clarification clarification : clarificationsList) {
            String auditorName = clarification.getUser().getFullname(); // Assuming user.getFullname() returns the auditor's name
            auditCountMap.putIfAbsent(auditorName, 0);

            int currentCount = auditCountMap.get(auditorName);
            auditCountMap.put(auditorName, currentCount + 1);
        }

        // Populate data
        int rowIndex = 1;
        for (Map.Entry<String, Integer> entry : auditCountMap.entrySet()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(convertDateToRoman.getMonthName(month));
            row.createCell(1).setCellValue(entry.getKey());
            row.createCell(2).setCellValue(entry.getValue());
        }
    }

    // Helper class to hold aggregated data
    private static class AggregatedData {
        int count = 0;
        long totalNominalLoss = 0;
    }
}
