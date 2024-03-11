package com.cms.audit.api.common.pdf;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Date;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.models.EPriority;
import com.cms.audit.api.common.constant.FolderPath;
import com.cms.audit.api.common.constant.convertDateToRoman;
import com.cms.audit.api.common.constant.randomValueNumber;
import com.cms.audit.api.common.response.PDFResponse;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class GeneratePdf {
        public static PDFResponse generateClarificationPDF(Clarification response)
                        throws FileNotFoundException, MalformedURLException {
                String fileName = randomValueNumber.randomNumberGenerator() + response.getUser().getInitial_name()
                                + "-clarification.pdf";
                String path = FolderPath.FOLDER_PATH_CLARIFICATION + fileName;
                PdfWriter pdfWriter = new PdfWriter(path);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                pdfDocument.setDefaultPageSize(PageSize.A4);
                Document document = new Document(pdfDocument);

                // convert date to string
                String dateNow = convertDateToRoman.convertDateToString(new Date());

                String imagePath = "image\\logo.png";
                ImageData imageData = ImageDataFactory.create(imagePath);
                Image image = new Image(imageData);
                image.scaleAbsolute(70, 40);

                // section 1
                float headerLenght[] = { 80f, 367f, 180f };
                Table header = new Table(headerLenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                header.addCell(new Cell().add(image).setFontSize(7).setBorderRight(Border.NO_BORDER));
                header.addCell(new Cell().add("FORMULIR KLARIFIKASI").setTextAlignment(TextAlignment.CENTER)
                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                                .setBold().setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));

                Table nestedheader = new Table(new float[] { 100f / 2, 200f / 2 });
                // right header
                nestedheader.addCell(new Cell().add("No Formulir").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY));
                nestedheader.addCell(
                                new Cell().add(response.getCode()).setFontSize(5)
                                                .setHorizontalAlignment(HorizontalAlignment.CENTER));
                nestedheader.addCell(
                                new Cell().add("Mulai berlaku").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY));
                nestedheader.addCell(
                                new Cell().add(dateNow).setFontSize(5)
                                                .setHorizontalAlignment(HorizontalAlignment.CENTER));
                nestedheader.addCell(new Cell().add("No Revisi").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY));
                nestedheader.addCell(
                                new Cell().add("-").setFontSize(5).setHorizontalAlignment(HorizontalAlignment.CENTER));
                nestedheader.addCell(
                                new Cell().add("Tanggal Revisi").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY));
                nestedheader.addCell(
                                new Cell().add("-").setFontSize(5).setHorizontalAlignment(HorizontalAlignment.CENTER));

                header.addCell(new Cell().add(nestedheader).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0));

                document.add(header);
                // section 1

                // section 2
                float blank1lenght[] = { 600 };
                Table blank1 = new Table(blank1lenght).setHorizontalAlignment(HorizontalAlignment.CENTER)
                                .setBorder(Border.NO_BORDER);
                blank1.addCell(new Cell().add("").setPadding(5).setBorder(Border.NO_BORDER));

                document.add(blank1);
                // section 2

                // section 3
                float subHeader1Lenght[] = { 506f, 180 };
                Table subheader1 = new Table(subHeader1Lenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                subheader1.addCell(new Cell().add("").setBorderRight(Border.NO_BORDER));

                Table nestedsubheader1 = new Table(new float[] { 100f / 2, 200f / 2 });
                nestedsubheader1.addCell(
                                new Cell().add("No Dokumen").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY));
                nestedsubheader1.addCell(
                                new Cell().add(response.getCode()).setFontSize(5)
                                                .setHorizontalAlignment(HorizontalAlignment.CENTER));
                nestedsubheader1.addCell(new Cell().add("Tanggal").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY));
                nestedsubheader1.addCell(
                                new Cell().add(dateNow).setFontSize(5)
                                                .setHorizontalAlignment(HorizontalAlignment.CENTER));
                subheader1.addCell(new Cell().add(nestedsubheader1).setBorder(Border.NO_BORDER).setPadding(0)
                                .setMargin(0));

                document.add(subheader1);
                // section 3

                // section 4
                float subHeader2Lenght[] = { 376f, 150f };
                Table subheader2 = new Table(subHeader2Lenght);

                Table nestedsubheader2 = new Table(new float[] { 376 / 2, 376 / 2 });
                nestedsubheader2.addCell(new Cell().add("Divisi Area yang di Audit").setFontSize(5)
                                .setBackgroundColor(Color.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
                nestedsubheader2.addCell(
                                new Cell().add("Atasan langsung").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY)
                                                .setTextAlignment(TextAlignment.CENTER));
                nestedsubheader2
                                .addCell(new Cell().add(response.getCases().getName()).setFontSize(10).setHeight(30)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                nestedsubheader2
                                .addCell(new Cell().add(response.getAuditee_leader()).setFontSize(10).setHeight(30)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                subheader2.addCell(new Cell().add(nestedsubheader2).setBorder(Border.NO_BORDER).setPadding(0)
                                .setMargin(0));

                subheader2.addCell(new Cell().add(response.getCaseCategory().getName()).setBorderLeft(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.CENTER).setFontSize(8)
                                .setVerticalAlignment(VerticalAlignment.MIDDLE));

                document.add(subheader2);
                // section 4

                // section 5
                Table subheader3 = new Table(subHeader2Lenght);

                Table nestedsubheader3 = new Table(new float[] { 376 / 2, 376 / 2 });
                nestedsubheader3.addCell(new Cell().add("Bagian").setFontSize(5)
                                .setBackgroundColor(Color.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
                nestedsubheader3
                                .addCell(new Cell().add("Kepada Yth / jabatan").setFontSize(5)
                                                .setBackgroundColor(Color.LIGHT_GRAY)
                                                .setTextAlignment(TextAlignment.CENTER));
                nestedsubheader3
                                .addCell(new Cell().add("").setFontSize(10).setHeight(30)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                nestedsubheader3
                                .addCell(new Cell().add(response.getAuditee()).setFontSize(10).setHeight(30)
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                subheader3.addCell(new Cell().add(nestedsubheader3).setBorder(Border.NO_BORDER).setPadding(0)
                                .setMargin(0));

                subheader3.addCell(new Cell().add("").setBorderLeft(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

                document.add(subheader3);
                // section 5

                // section 6
                document.add(blank1);
                // section 6

                // section 7
                float body1Lenght[] = { 540f };
                Table body1 = new Table(body1Lenght);
                body1.addCell(new Cell().add("Penjabaran temuan audit :").setFontSize(8)
                                .setBorderBottom(Border.NO_BORDER)
                                .setMargin(0).setPadding(2));
                body1.addCell(new Cell().add(response.getDescription()).setFontSize(8)
                                .setBorderBottom(Border.NO_BORDER).setBorderTop(Border.NO_BORDER).setHeight(120)
                                .setMargin(0).setPadding(2));

                document.add(body1);
                // section 7

                // section 8
                float body2Lenght[] = { 376f, 150f };
                Table body2 = new Table(body2Lenght);
                Table nestedbody2 = new Table(new float[] { 376 / 4, 376 / 4, 376 / 4, 376 / 4 });
                nestedbody2.addCell(
                                new Cell().add("").setFontSize(5).setHeight(60).setTextAlignment(TextAlignment.CENTER)
                                                .setBorder(Border.NO_BORDER));
                nestedbody2.addCell(
                                new Cell().add("").setFontSize(5).setHeight(60).setTextAlignment(TextAlignment.CENTER)
                                                .setBorder(Border.NO_BORDER));
                nestedbody2.addCell(
                                new Cell().add("").setFontSize(5).setHeight(60).setTextAlignment(TextAlignment.CENTER)
                                                .setBorder(Border.NO_BORDER));
                nestedbody2.addCell(
                                new Cell().add("").setFontSize(5).setHeight(60).setTextAlignment(TextAlignment.CENTER)
                                                .setBorder(Border.NO_BORDER));
                nestedbody2.addCell(
                                new Cell().add("Prioritas Temuan*").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY)
                                                .setTextAlignment(TextAlignment.CENTER).setBorderLeft(Border.NO_BORDER)
                                                .setBorderBottom(Border.NO_BORDER));
                if (response.getPriority() == EPriority.Major) {
                        nestedbody2.addCell(new Cell().add("Major").setFontSize(5)
                                        .setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER)
                                        .setBackgroundColor(Color.RED));
                } else {
                        nestedbody2.addCell(new Cell().add("Major").setFontSize(5)
                                        .setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER));
                }
                if (response.getPriority() == EPriority.Minor) {
                        nestedbody2.addCell(new Cell().add("Minor").setFontSize(5)
                                        .setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER)
                                        .setBackgroundColor(Color.RED));
                } else {
                        nestedbody2.addCell(new Cell().add("Minor").setFontSize(5)
                                        .setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER));
                }
                ;
                if (response.getPriority() == EPriority.OI) {
                        nestedbody2.addCell(new Cell().add("OI").setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                        .setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
                                        .setBackgroundColor(Color.RED));
                } else {
                        nestedbody2.addCell(new Cell().add("OI").setFontSize(5).setTextAlignment(TextAlignment.CENTER)
                                        .setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
                }

                body2.addCell(new Cell().add(nestedbody2).setBorderTop(Border.NO_BORDER).setMargin(0).setPadding(0));

                float mark1[] = { 150f };
                Table mark1Table = new Table(mark1);
                mark1Table.addCell(new Cell().add("Auditor").setTextAlignment(TextAlignment.CENTER).setFontSize(5)
                                .setBorder(Border.NO_BORDER));
                mark1Table.addCell(new Cell()
                                .add("(.........................................................................................)")
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.BOTTOM)
                                .setHeight(60)
                                .setFontSize(5).setBorder(Border.NO_BORDER));
                body2.addCell(new Cell().add(mark1Table).setMargin(0).setPadding(0));

                document.add(body2);
                // section 8

                // section 9
                document.add(blank1);
                // section 9

                // section 10
                Table body3 = new Table(body1Lenght).setHeight(130);
                body3.addCell(new Cell().add("Penjelasan dari auditor :").setFontSize(8)
                                .setBorderBottom(Border.NO_BORDER)
                                .setMargin(0).setPadding(2));

                document.add(body3);
                // section 10

                // section 11
                Table body4 = new Table(body2Lenght);
                Table nestedbody4 = new Table(new float[] { 376 });
                nestedbody4.addCell(new Cell().add("Langkah Perbaikan (Sementara / segera):").setFontSize(8)
                                .setPaddingLeft(3)
                                .setBorder(Border.NO_BORDER));
                nestedbody4.addCell(new Cell().add("").setFontSize(5).setHeight(15)
                                .setBorder(Border.NO_BORDER));
                nestedbody4.addCell(new Cell().add("Langkah Perbaikan (Permanen):").setFontSize(8).setPaddingLeft(3)
                                .setBorder(Border.NO_BORDER));
                nestedbody4.addCell(new Cell().add("").setFontSize(5).setHeight(15)
                                .setBorder(Border.NO_BORDER));

                body4.addCell(new Cell().add(nestedbody4).setBorderTop(Border.NO_BORDER).setMargin(0).setPadding(0));

                float mark2[] = { 150f };
                Table mark2Table = new Table(mark2);
                mark2Table.addCell(new Cell().add("Auditee").setTextAlignment(TextAlignment.CENTER).setFontSize(5)
                                .setBorder(Border.NO_BORDER));
                mark2Table.addCell(new Cell()
                                .add("(.........................................................................................)")
                                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.BOTTOM)
                                .setHeight(65)
                                .setFontSize(5).setBorder(Border.NO_BORDER));
                body4.addCell(new Cell().add(mark2Table).setMargin(0).setPadding(0));

                document.add(body4);
                // section 11

                // section 12
                document.add(blank1);
                // section 12

                // section 13
                Table body5 = new Table(body1Lenght);
                float nestedbody6Lenght[] = { 376f, 60f, 90f };
                Table nestedbody6 = new Table(nestedbody6Lenght);
                nestedbody6.addCell(new Cell().add("Evaluasi Klarifikasi dan Tindak Lanjut").setFontSize(8)
                                .setBorderBottom(Border.NO_BORDER)
                                .setMargin(0).setPadding(2));
                nestedbody6.addCell(
                                new Cell().add("Tanggal Evaluasi").setFontSize(5).setBackgroundColor(Color.LIGHT_GRAY)
                                                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                .setMargin(0).setPadding(2).setTextAlignment(TextAlignment.CENTER));
                nestedbody6.addCell(new Cell().add("").setFontSize(5)
                                .setMargin(0).setPadding(2));
                body5.addCell(new Cell().add(nestedbody6).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));

                String imagePath2 = "image\\checklist.png";
                ImageData imageData2 = ImageDataFactory.create(imagePath2);
                Image image2 = new Image(imageData2);
                image2.scaleAbsolute(50, 30);
                body5.addCell(new Cell().add(image2).setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
                                .setPaddingLeft(10));

                document.add(body5);
                // section 13

                // section 14
                Table body6 = new Table(body2Lenght);
                Table nestedbody7 = new Table(new float[] { 376 });
                nestedbody7.addCell(new Cell().add("Alasan penolakan adalah:").setFontSize(8).setPaddingLeft(3)
                                .setBorder(Border.NO_BORDER));
                nestedbody7.addCell(new Cell().add("").setFontSize(5).setHeight(40)
                                .setBorder(Border.NO_BORDER));

                body6.addCell(new Cell().add(nestedbody7).setBorderTop(Border.NO_BORDER).setMargin(0).setPadding(0));

                body6.addCell(new Cell().add("Auditor").setTextAlignment(TextAlignment.CENTER).setFontSize(5));

                document.add(body6);
                // section 14

                // section 15
                float body7Lenght[] = { 286f, 90f, 150f };
                Table body7 = new Table(body7Lenght);
                body7.addCell(new Cell().add("Batas Waktu Penyelesaian").setTextAlignment(TextAlignment.CENTER)
                                .setFontSize(5)
                                .setBackgroundColor(Color.LIGHT_GRAY));
                body7.addCell(new Cell()
                                .add(convertDateToRoman.convertDateToString(response.getEvaluation_limitation()))
                                .setTextAlignment(TextAlignment.CENTER).setFontSize(5));
                body7.addCell(new Cell().add("").setTextAlignment(TextAlignment.CENTER).setFontSize(5));

                document.add(body7);
                // section 15

                document.close();

                return PDFResponse
                                .builder()
                                .fileName(fileName)
                                .filePath(path)
                                .build();
        }

        public static void generateFollowUpPDF(String[] args) throws FileNotFoundException, MalformedURLException {
                String path = FolderPath.FOLDER_PATH_FOLLOW_UP + "follow_up.pdf";
                PdfWriter pdfWriter = new PdfWriter(path);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                pdfDocument.setDefaultPageSize(PageSize.A4);
                Document document = new Document(pdfDocument);

                float headerLenght[] = { 540f };
                Table header = new Table(headerLenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                header.addCell(new Cell().add("FORM TINDAK LANJUT REKOMENDASI AUDIT").setBold().setFontSize(8));
                document.add(header);

                document.close();
        }

}
