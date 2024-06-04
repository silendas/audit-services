package com.cms.audit.api.Common.util;

import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.dto.GapDTO;
import com.itextpdf.io.source.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ExcelUtil {
    public static String HEADER[] = { "comment_clarification", "auditor", "kasus", "kategori", "kerugian",
            "batas_evaluasi", "tanggal_mulai_realisasi", "tanggal_selesai_realisasi", "waktu_penyelesaian",
            "tanggal_estimasi", "realisasi_sanksi", "lokasi", "auditee", "atasan_auditee", "file", "deskripsi", "prioritas", "tanggal_terbuat",
            "status" };

    public static String SHEET_NAME = "Laporan Klarifikasi";

    public static ByteArrayInputStream dataToExcel(List<Clarification> clarificationsList, String realizePenalty) throws IOException {

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
                if (c.getEvaluation_limitation() != null) {
                    row1.createCell(5)
                            .setCellValue(convertDateToRoman.convertDateToString(c.getEvaluation_limitation()));
                } else {
                    row1.createCell(5).setCellValue("-");
                }
                if (c.getStart_date_realization() != null) {
                    row1.createCell(6)
                            .setCellValue(convertDateToRoman.convertDateToString(c.getStart_date_realization()));
                } else {
                    row1.createCell(6).setCellValue("-");
                }
                if (c.getEnd_date_realization() != null) {
                    row1.createCell(7)
                            .setCellValue(convertDateToRoman.convertDateToString(c.getEnd_date_realization()));
                } else {
                    row1.createCell(7).setCellValue("-");
                }
                if (c.getEnd_date_realization() != null && c.getStart_date_realization() != null) {
                    GapDTO gap = convertDateToRoman.calculateDateDifference(c.getEnd_date_realization(),
                            c.getStart_date_realization());
                    String gapDay = "";
                    if (gap.getDay() > 0) {
                        gapDay = gap.getDay() + " hari ";
                    }
                    if (gap.getHour() != 0) {
                        gapDay = gapDay + gap.getHour() + " jam ";
                    }
                    if (gap.getMinute() != 0) {
                        gapDay = gapDay + gap.getMinute() + " menit ";
                    }
                    if (gap.getSecond() != 0) {
                        gapDay = gapDay + gap.getSecond() + " detik";
                    }
                    row1.createCell(8).setCellValue(gapDay);
                } else {
                    row1.createCell(8).setCellValue("-");
                }
                if (c.getEvaluation_limitation() != null && c.getEnd_date_realization() != null) {
                    GapDTO gap = convertDateToRoman.calculateDateDifference(c.getEnd_date_realization(),
                            c.getEvaluation_limitation());
                    String gapDay = "";
                    if (gap.getDay() > 0) {
                        gapDay = gap.getDay() + " hari ";
                    }
                    if (gap.getHour() != 0) {
                        gapDay = gapDay + gap.getHour() + " jam ";
                    }
                    if (gap.getMinute() != 0) {
                        gapDay = gapDay + gap.getMinute() + " menit ";
                    }
                    if (gap.getSecond() != 0) {
                        gapDay = gapDay + gap.getSecond() + " detik";
                    }
                    row1.createCell(9).setCellValue(gapDay);
                } else {
                    row1.createCell(9).setCellValue("-");
                }
                row1.createCell(10).setCellValue(realizePenalty);
                row1.createCell(11).setCellValue(c.getLocation());
                row1.createCell(12).setCellValue(c.getAuditee());
                row1.createCell(13).setCellValue(c.getAuditee_leader());
                row1.createCell(14).setCellValue(c.getFilename());
                row1.createCell(15).setCellValue(c.getDescription());
                if (c.getPriority() != null) {
                    row1.createCell(16).setCellValue(c.getPriority().name());
                } else {
                    row1.createCell(16).setCellValue("-");
                }
                row1.createCell(17).setCellValue(convertDateToRoman.convertDateToString(c.getCreated_at()));
                row1.createCell(18).setCellValue(c.getStatus().name());
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
