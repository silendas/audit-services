package com.cms.audit.api.Common.util;

import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.itextpdf.io.source.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ExcelUtil {
    public static String HEADER[] = { "comment_clarification", "auditor", "kasus", "kategori", "kerugian",
            "batas_evaluasi", "lokasi", "auditee", "atasan", "file", "deskripsi", "prioritas", "status" };

    public static String SHEET_NAME = "Laporan";

    public static ByteArrayInputStream dataToExcel(List<Clarification> clarificationsList) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            Row row = sheet.createRow(0);
            for (int i = 0; i < HEADER.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(HEADER[i]);
            }

            int rowIndex = 1;
            for (Clarification c : clarificationsList) {
                Row row1 = sheet.createRow(rowIndex);
                rowIndex++;
                row1.createCell(0).setCellValue(c.getCode());
                row1.createCell(1).setCellValue(c.getUser().getFullname());
                row1.createCell(2).setCellValue(c.getCases().getName());
                row1.createCell(3).setCellValue(c.getCaseCategory().getName());
                row1.createCell(4).setCellValue(c.getNominal_loss());
                row1.createCell(5).setCellValue(c.getEvaluation_limitation());
                row1.createCell(6).setCellValue(c.getLocation());
                row1.createCell(7).setCellValue(c.getAuditee());
                row1.createCell(8).setCellValue(c.getAuditee_leader());
                row1.createCell(9).setCellValue(c.getFilename());
                row1.createCell(10).setCellValue(c.getDescription());
                row1.createCell(11).setCellValue(c.getPriority().name());
                row1.createCell(12).setCellValue(c.getStatus().name());
            }

            workbook.write(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
            byteArrayOutputStream.close();
        }
        return null;
    };
}
