package com.cms.audit.api.Common.pdf;

import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Report.dto.LhaReportDTO;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class LHAReport {
        public static ByteArrayInputStream generateLHAPDF(LhaReportDTO dto)
                        throws FileNotFoundException, MalformedURLException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter pdfWriter = new PdfWriter(baos);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                pdfDocument.setDefaultPageSize(PageSize.A4);
                Document document = new Document(pdfDocument);

                float headerLenght[] = { 600f };
                Table header = new Table(headerLenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                header.addCell(new Cell().add("LAPORAN HARIAN AUDIT").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER)
                                .setPaddingBottom(1));
                header.addCell(new Cell().add("PT. CMS MAJU SEJAHTRA").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setBorderTop(Border.NO_BORDER)
                                .setPaddingTop(1));
                document.add(header);

                float header2Lenght[] = { 107f, 80f, 400f };
                Table header2 = new Table(header2Lenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                header2.addCell(new Cell().add("TANGGAL").setBackgroundColor(Color.YELLOW).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER));
                header2.addCell(new Cell().add(dto.getDate()).setBackgroundColor(Color.WHITE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER));
                header2.addCell(new Cell().add(dto.getArea_name()).setBackgroundColor(Color.YELLOW).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER));
                document.add(header2);

                Table blank = new Table(headerLenght).setBorder(Border.NO_BORDER).setPadding(3);
                blank.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                document.add(blank);

                float header3Lenght[] = { 45f, 65f, 50f, 30f, 100f, 100f, 100f, 100f };
                Table header3 = new Table(header3Lenght);
                header3.addCell(new Cell().add("AREA").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("NAMA AUDITOR").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("KANTOR CABANG").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("KASUS").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("KATEGORI SOP").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("URAIAN TEMUAN").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("REKOMENDASI PERBAIKAN SEMENTARA").setBackgroundColor(Color.ORANGE)
                                .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("REKOMENDASI PERBAIKAN PERMANEN").setBackgroundColor(Color.ORANGE)
                                .setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                document.add(header3);

                float bodyLenght[] = { 45f, 555f };
                Table body = new Table(bodyLenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                body.addCell(new Cell().add(dto.getArea_name()).setBackgroundColor(Color.YELLOW).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                // float bodyNestedLenght[] = { 65f, 50f, 440f };
                Table bodyNested = new Table(new float[] { 64f, 50f, 440f });
                float bodyNested2Lenght[] = { 30f, 97f, 96f, 99f, 100f };
                for (int i = 0; i < dto.getLha_detail().size(); i++) {
                        bodyNested.addCell(new Cell().add(dto.getLha_detail().get(i).getFullname())
                                        .setBackgroundColor(Color.YELLOW)
                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                        .setVerticalAlignment(VerticalAlignment.MIDDLE));
                        bodyNested.addCell(new Cell().add(dto.getLha_detail().get(i).getBranch())
                                        .setBackgroundColor(Color.YELLOW)
                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                        .setVerticalAlignment(VerticalAlignment.MIDDLE));
                        Table bodyNested2 = new Table(bodyNested2Lenght)
                                        .setHorizontalAlignment(HorizontalAlignment.CENTER);
                        for (int u = 0; u < dto.getLha_detail().get(i).getDetails().size(); u++) {
                                bodyNested2.addCell(new Cell()
                                                .add(dto.getLha_detail().get(i).getDetails().get(u).getCases()
                                                                .getCode())
                                                .setBackgroundColor(Color.WHITE)
                                                .setFontSize(5).setTextAlignment(TextAlignment.CENTER));
                                bodyNested2.addCell(new Cell()
                                                .add(dto.getLha_detail().get(i).getDetails().get(u).getCaseCategory()
                                                                .getName())
                                                .setBackgroundColor(Color.WHITE)
                                                .setFontSize(5).setTextAlignment(TextAlignment.CENTER));
                                if (dto.getLha_detail().get(i).getDetails().get(u).getDescription() != null) {
                                        bodyNested2.addCell(new Cell()
                                                        .add(dto.getLha_detail().get(i).getDetails().get(u)
                                                                        .getDescription())
                                                        .setBackgroundColor(Color.WHITE)
                                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER));
                                } else {
                                        bodyNested2.addCell(new Cell()
                                                        .add("-")
                                                        .setBackgroundColor(Color.WHITE)
                                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER));
                                }
                                if (dto.getLha_detail().get(i).getDetails().get(u)
                                                .getTemporary_recommendations() != null) {
                                        bodyNested2.addCell(new Cell()
                                                        .add(dto.getLha_detail().get(i).getDetails().get(u)
                                                                        .getTemporary_recommendations())
                                                        .setBackgroundColor(Color.WHITE)
                                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER));
                                } else {
                                        bodyNested2.addCell(new Cell()
                                                        .add("-")
                                                        .setBackgroundColor(Color.WHITE)
                                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER));
                                }
                                if (dto.getLha_detail().get(i).getDetails().get(u)
                                                .getPermanent_recommendations() != null) {
                                        bodyNested2.addCell(new Cell()
                                                        .add(dto.getLha_detail().get(i).getDetails().get(u)
                                                                        .getPermanent_recommendations())
                                                        .setBackgroundColor(Color.WHITE)
                                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER));
                                } else {
                                        bodyNested2.addCell(new Cell()
                                                        .add("-")
                                                        .setBackgroundColor(Color.WHITE)
                                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER));
                                }
                        }
                        bodyNested.addCell(
                                        new Cell().add(bodyNested2).setBorder(Border.NO_BORDER).setPadding(0));
                }
                body.addCell(new Cell().add(bodyNested).setBorder(Border.NO_BORDER).setPadding(0));

                document.add(body);

                document.close();

                return new ByteArrayInputStream(baos.toByteArray());
        }

        public static ByteArrayInputStream generateIfNoData()
                        throws FileNotFoundException, MalformedURLException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter pdfWriter = new PdfWriter(baos);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                pdfDocument.setDefaultPageSize(PageSize.A4);
                Document document = new Document(pdfDocument);

                float headerLenght[] = { 600f };
                Table header = new Table(headerLenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                header.addCell(new Cell().add("LAPORAN HARIAN AUDIT").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER)
                                .setPaddingBottom(1));
                header.addCell(new Cell().add("PT. CMS MAJU SEJAHTRA").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setBorderTop(Border.NO_BORDER)
                                .setPaddingTop(1));
                document.add(header);

                float header2Lenght[] = { 107f, 80f, 400f };
                Table header2 = new Table(header2Lenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                header2.addCell(new Cell().add("TANGGAL").setBackgroundColor(Color.YELLOW).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER));
                header2.addCell(new Cell().add(convertDateToRoman.convertDateHehe(new Date()))
                                .setBackgroundColor(Color.WHITE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER));
                header2.addCell(new Cell().add("").setBackgroundColor(Color.YELLOW).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER));
                document.add(header2);

                Table blank = new Table(headerLenght).setBorder(Border.NO_BORDER).setPadding(3);
                blank.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                document.add(blank);

                float header3Lenght[] = { 45f, 65f, 50f, 30f, 100f, 100f, 100f, 100f };
                Table header3 = new Table(header3Lenght);
                header3.addCell(new Cell().add("AREA").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("NAMA AUDITOR").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("KANTOR CABANG").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("KASUS").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("KATEGORI SOP").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("URAIAN TEMUAN").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("REKOMENDASI PERBAIKAN SEMENTARA").setBackgroundColor(Color.ORANGE)
                                .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                header3.addCell(new Cell().add("REKOMENDASI PERBAIKAN PERMANEN").setBackgroundColor(Color.ORANGE)
                                .setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                document.add(header3);

                float bodyLenght[] = { 600f };
                Table body = new Table(bodyLenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                body.addCell(new Cell().add("No Data Found").setBackgroundColor(Color.YELLOW).setFontSize(5)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

                document.add(body);

                document.close();

                return new ByteArrayInputStream(baos.toByteArray());
        }

        public static ByteArrayInputStream generateAllLHAPDF(List<LhaReportDTO> dto)
                        throws FileNotFoundException, MalformedURLException {
                if (!dto.isEmpty()) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PdfWriter pdfWriter = new PdfWriter(baos);
                        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                        pdfDocument.setDefaultPageSize(PageSize.A4);
                        Document document = new Document(pdfDocument);
                        for (int o = 0; o < dto.size(); o++) {
                                float headerLenght[] = { 600f };
                                Table header = new Table(headerLenght)
                                                .setHorizontalAlignment(HorizontalAlignment.CENTER);
                                header.addCell(new Cell().add("LAPORAN HARIAN AUDIT").setBackgroundColor(Color.ORANGE)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setBorderBottom(Border.NO_BORDER)
                                                .setPaddingBottom(1));
                                header.addCell(new Cell().add("PT. CMS MAJU SEJAHTRA").setBackgroundColor(Color.ORANGE)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER).setBorderTop(Border.NO_BORDER)
                                                .setPaddingTop(1));
                                document.add(header);

                                float header2Lenght[] = { 107f, 80f, 400f };
                                Table header2 = new Table(header2Lenght)
                                                .setHorizontalAlignment(HorizontalAlignment.CENTER);
                                header2.addCell(new Cell().add("TANGGAL").setBackgroundColor(Color.YELLOW)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER));
                                header2.addCell(new Cell().add(dto.get(o).getDate()).setBackgroundColor(Color.WHITE)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER));
                                header2.addCell(new Cell().add(dto.get(o).getArea_name())
                                                .setBackgroundColor(Color.YELLOW)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER));
                                document.add(header2);

                                Table blank = new Table(headerLenght).setBorder(Border.NO_BORDER).setPadding(3);
                                blank.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                                document.add(blank);

                                float header3Lenght[] = { 45f, 65f, 50f, 30f, 100f, 100f, 100f, 100f };
                                Table header3 = new Table(header3Lenght);
                                header3.addCell(new Cell().add("AREA").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                header3.addCell(new Cell().add("NAMA AUDITOR").setBackgroundColor(Color.ORANGE)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                header3.addCell(new Cell().add("KANTOR CABANG").setBackgroundColor(Color.ORANGE)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                header3.addCell(new Cell().add("KASUS").setBackgroundColor(Color.ORANGE).setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                header3.addCell(new Cell().add("KATEGORI SOP").setBackgroundColor(Color.ORANGE)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                header3.addCell(new Cell().add("URAIAN TEMUAN").setBackgroundColor(Color.ORANGE)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                header3.addCell(new Cell().add("REKOMENDASI PERBAIKAN SEMENTARA")
                                                .setBackgroundColor(Color.ORANGE)
                                                .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                header3.addCell(new Cell().add("REKOMENDASI PERBAIKAN PERMANEN")
                                                .setBackgroundColor(Color.ORANGE)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                document.add(header3);

                                float bodyLenght[] = { 45f, 555f };
                                Table body = new Table(bodyLenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                                body.addCell(new Cell().add(dto.get(o).getArea_name()).setBackgroundColor(Color.YELLOW)
                                                .setFontSize(5)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                // float bodyNestedLenght[] = { 65f, 50f, 440f };
                                Table bodyNested = new Table(new float[] { 70f, 54f, 440f });
                                float bodyNested2Lenght[] = { 33f, 97f, 96f, 99f, 100f };
                                for (int i = 0; i < dto.get(o).getLha_detail().size(); i++) {
                                        bodyNested.addCell(new Cell()
                                                        .add(dto.get(o).getLha_detail().get(i).getFullname())
                                                        .setBackgroundColor(Color.YELLOW)
                                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                                        .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                        bodyNested.addCell(new Cell().add(dto.get(o).getLha_detail().get(i).getBranch())
                                                        .setBackgroundColor(Color.YELLOW)
                                                        .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                                        .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                        Table bodyNested2 = new Table(bodyNested2Lenght)
                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER);
                                        for (int u = 0; u < dto.get(o).getLha_detail().get(i).getDetails()
                                                        .size(); u++) {
                                                bodyNested2.addCell(new Cell()
                                                                .add(dto.get(o).getLha_detail().get(i).getDetails()
                                                                                .get(u)
                                                                                .getCases()
                                                                                .getCode())
                                                                .setBackgroundColor(Color.WHITE)
                                                                .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                                bodyNested2.addCell(new Cell()
                                                                .add(dto.get(o).getLha_detail().get(i).getDetails()
                                                                                .get(u)
                                                                                .getCaseCategory()
                                                                                .getName())
                                                                .setBackgroundColor(Color.WHITE)
                                                                .setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                                                if (dto.get(o).getLha_detail().get(i).getDetails().get(u)
                                                                .getDescription() != null) {
                                                        bodyNested2.addCell(new Cell()
                                                                        .add(dto.get(o).getLha_detail().get(i)
                                                                                        .getDetails()
                                                                                        .get(u)
                                                                                        .getDescription())
                                                                        .setBackgroundColor(Color.WHITE)
                                                                        .setFontSize(5)
                                                                        .setTextAlignment(TextAlignment.CENTER)
                                                                        .setVerticalAlignment(
                                                                                        VerticalAlignment.MIDDLE));
                                                } else {
                                                        bodyNested2.addCell(new Cell()
                                                                        .add("-")
                                                                        .setBackgroundColor(Color.WHITE)
                                                                        .setFontSize(5)
                                                                        .setTextAlignment(TextAlignment.CENTER)
                                                                        .setVerticalAlignment(
                                                                                        VerticalAlignment.MIDDLE));
                                                }
                                                if (dto.get(o).getLha_detail().get(i).getDetails().get(u)
                                                                .getTemporary_recommendations() != null) {
                                                        bodyNested2.addCell(new Cell()
                                                                        .add(dto.get(o).getLha_detail().get(i)
                                                                                        .getDetails()
                                                                                        .get(u)
                                                                                        .getTemporary_recommendations())
                                                                        .setBackgroundColor(Color.WHITE)
                                                                        .setFontSize(5)
                                                                        .setTextAlignment(TextAlignment.CENTER)
                                                                        .setVerticalAlignment(
                                                                                        VerticalAlignment.MIDDLE));
                                                } else {
                                                        bodyNested2.addCell(new Cell()
                                                                        .add("-")
                                                                        .setBackgroundColor(Color.WHITE)
                                                                        .setFontSize(5)
                                                                        .setTextAlignment(TextAlignment.CENTER)
                                                                        .setVerticalAlignment(
                                                                                        VerticalAlignment.MIDDLE));
                                                }
                                                if (dto.get(o).getLha_detail().get(i).getDetails().get(u)
                                                                .getPermanent_recommendations() != null) {
                                                        bodyNested2.addCell(new Cell()
                                                                        .add(dto.get(o).getLha_detail().get(i)
                                                                                        .getDetails()
                                                                                        .get(u)
                                                                                        .getPermanent_recommendations())
                                                                        .setBackgroundColor(Color.WHITE)
                                                                        .setFontSize(5)
                                                                        .setTextAlignment(TextAlignment.CENTER)
                                                                        .setVerticalAlignment(
                                                                                        VerticalAlignment.MIDDLE));
                                                } else {
                                                        bodyNested2.addCell(new Cell()
                                                                        .add("-")
                                                                        .setBackgroundColor(Color.WHITE)
                                                                        .setFontSize(5)
                                                                        .setTextAlignment(TextAlignment.CENTER)
                                                                        .setVerticalAlignment(
                                                                                        VerticalAlignment.MIDDLE));
                                                }
                                        }
                                        bodyNested.addCell(
                                                        new Cell().add(bodyNested2).setBorder(Border.NO_BORDER)
                                                                        .setPadding(0));
                                }
                                body.addCell(new Cell().add(bodyNested).setBorder(Border.NO_BORDER).setPadding(0));

                                document.add(body);

                                if (o < dto.size() && o != dto.size()) {
                                        document.add(new AreaBreak());
                                }
                        }
                        document.close();
                        return new ByteArrayInputStream(baos.toByteArray());
                } else {
                        return generateIfNoData();
                }
        }
}
