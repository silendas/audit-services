package com.cms.audit.api.Common.util;

import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.itextpdf.io.source.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ExcelUtil {
    public static String HEADER[] = { "comment_clarification", "auditor", "kasus", "kategori", "kerugian",
            "batas_evaluasi", "lokasi", "auditee", "atasan auditee", "file", "deskripsi", "prioritas", "created_at",
            "status" };

    public static String SHEET_NAME = "Laporan Klarifikasi";

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
                if (c.getNominal_loss() != null && c.getNominal_loss() != 0) {
                    row1.createCell(4).setCellValue(c.getNominal_loss());
                } else {
                    row1.createCell(4).setCellValue(0L);
                }
                if (c.getEvaluation() != null) {
                    row1.createCell(5)
                            .setCellValue(convertDateToRoman.convertDateToString(c.getEvaluation_limitation()));
                } else {
                    row1.createCell(5).setCellValue("");
                }
                row1.createCell(6).setCellValue(c.getLocation());
                row1.createCell(7).setCellValue(c.getAuditee());
                row1.createCell(8).setCellValue(c.getAuditee_leader());
                row1.createCell(9).setCellValue(c.getFilename());
                row1.createCell(10).setCellValue(c.getDescription());
                if (c.getPriority() != null) {
                    row1.createCell(11).setCellValue(c.getPriority().name());
                } else {
                    row1.createCell(11).setCellValue("");
                }
                row1.createCell(12).setCellValue(convertDateToRoman.convertDateToString(c.getCreated_at()));
                row1.createCell(13).setCellValue(c.getStatus().name());
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
