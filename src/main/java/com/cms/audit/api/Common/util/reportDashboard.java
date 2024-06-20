package com.cms.audit.api.Common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.convertDateToRoman;

public class reportDashboard {
    public static String SHEET_NAME = "Laporan Klarifikasi";

    public static ByteArrayInputStream dataToExcel(List<Clarification> clarificationsList, Long month) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            // Create header row
            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Bulan");
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Kriteria Temuan");
            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("Jumlah Temuan");
            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("Nominal Temuan");

            // Group clarifications by case category
            Map<String, AggregatedData> aggregatedDataMap = new HashMap<>();
            for (Clarification clarification : clarificationsList) {
                String categoryName = clarification.getCaseCategory().getName();
                if (!aggregatedDataMap.containsKey(categoryName)) {
                    aggregatedDataMap.put(categoryName, new AggregatedData());
                }

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
                row.createCell(0).setCellValue(convertDateToRoman.getMonthName(month)); // Assuming the month is January, change as needed
                row.createCell(1).setCellValue(entry.getKey());
                row.createCell(2).setCellValue(entry.getValue().count);
                row.createCell(3).setCellValue(entry.getValue().totalNominalLoss);
            }

            workbook.write(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } finally {
            workbook.close();
            byteArrayOutputStream.close();
        }
    }

    // Helper class to hold aggregated data
    private static class AggregatedData {
        int count = 0;
        long totalNominalLoss = 0;
    }
}
