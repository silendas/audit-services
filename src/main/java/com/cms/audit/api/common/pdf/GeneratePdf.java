package com.cms.audit.api.Common.pdf;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Date;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.models.EPriority;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.constant.randomValueNumber;
import com.cms.audit.api.Common.response.PDFResponse;
import com.cms.audit.api.FollowUp.models.FollowUp;
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
        public static PDFResponse generateClarificationPDF(Clarification response, String formulir)
                        throws FileNotFoundException, MalformedURLException {
                String fileName = randomValueNumber.randomNumberGenerator() + response.getUser().getInitial_name()+ "-" + response.getReport_number()+ "-clarification.pdf";
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
                                new Cell().add(formulir).setFontSize(5)
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

        public static PDFResponse generateFollowUpPDF(FollowUp response) throws FileNotFoundException, MalformedURLException {
                String fileName = randomValueNumber.randomNumberGenerator() + response.getClarification().getUser().getInitial_name()
                + "-followup.pdf";
                String path = FolderPath.FOLDER_PATH_UPLOAD_FOLLOW_UP + fileName;
                PdfWriter pdfWriter = new PdfWriter(path);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                pdfDocument.setDefaultPageSize(PageSize.A4);
                Document document = new Document(pdfDocument);

                // body
                float parentLenght[] = { 600f };
                Table parent = new Table(parentLenght).setHorizontalAlignment(HorizontalAlignment.CENTER).setPadding(10)
                                .setBorder(Border.NO_BORDER).setMarginTop(0);
                // body

                // body
                float bodyLenght[] = { 600f };
                Table body = new Table(bodyLenght).setHorizontalAlignment(HorizontalAlignment.CENTER).setPadding(10)
                                .setBorder(Border.NO_BORDER);
                // body

                // section 1
                float headerLenght[] = { 540f };
                Table header = new Table(headerLenght).setHorizontalAlignment(HorizontalAlignment.CENTER);
                header.addCell(new Cell().add("FORM TINDAK LANJUT REKOMENDASI AUDIT").setBold().setFontSize(9)
                                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                body.addCell(new Cell().add(header).setBorder(Border.NO_BORDER));
                // section 1

                // section 2
                float blank1lenght[] = { 540f };
                Table blank1 = new Table(blank1lenght).setHorizontalAlignment(HorizontalAlignment.CENTER)
                                .setBorder(Border.NO_BORDER);
                blank1.addCell(new Cell().add("").setPadding(5).setBorder(Border.NO_BORDER));
                body.addCell(new Cell().add(blank1).setBorder(Border.NO_BORDER));
                // section 2

                // section 3
                float body1Lenght[] = { 540f };
                Table body1 = new Table(body1Lenght).setHorizontalAlignment(HorizontalAlignment.LEFT);
                body1.addCell(new Cell().add(
                                "Menindaklanjuti dari hasil Hasil temuan Auditor Internal, sesuai dengan klarifikasi Nomor : "
                                                + response.getClarification().getCode() + " tanggal " + convertDateToRoman.convertDateToString(new Date()) + " " + response.getClarification().getLocation() + " perihal : ")
                                .setFontSize(8).setBorder(Border.NO_BORDER));
                body.addCell(new Cell().add(body1).setBorder(Border.NO_BORDER));

                // section 3

                // section 4
                float body2Lenght[] = { 540f };
                Table body2 = new Table(body2Lenght).setHorizontalAlignment(HorizontalAlignment.LEFT);
                body2.addCell(new Cell().add(
                                response.getDescription())
                                .setFontSize(8).setBorder(Border.NO_BORDER).setPaddingLeft(10).setHeight(160));
                body.addCell(new Cell().add(body2).setBorder(Border.NO_BORDER));
                // section 4

                // section 5
                float body3Lenght[] = { 540f };
                Table body3 = new Table(body3Lenght).setHorizontalAlignment(HorizontalAlignment.LEFT);
                body3.addCell(new Cell().add("*Data terlampir").setFontSize(8).setBorder(Border.NO_BORDER).setBold()
                                .setPaddingLeft(10));
                body3.addCell(new Cell().add("maka kami menerangkan bahwa :").setBorder(Border.NO_BORDER).setFontSize(7)
                                .setPaddingLeft(15));
                body.addCell(new Cell().add(body3).setBorder(Border.NO_BORDER));
                // section 5

                // blank line
                document.add(blank1);
                // blank line

                // section 6
                float body4Lenght[] = { 18f, 120f, 390f };
                Table body4 = new Table(body4Lenght).setHorizontalAlignment(HorizontalAlignment.LEFT);
                body4.addCell(new Cell().add("").setFontSize(8).setBold().setMargin(5));
                body4.addCell(new Cell().add("Meberikan Sanksi ").setBorder(Border.NO_BORDER).setFontSize(7)
                                .setPaddingLeft(15));
                body4.addCell(new Cell().add(
                                ": ........................................................................................................................................................")
                                .setBorder(Border.NO_BORDER).setFontSize(7).setPaddingLeft(15));
                body4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                body4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                body4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                body4.addCell(new Cell().add("").setFontSize(8).setBold().setMargin(5));
                body4.addCell(new Cell().add("TIdak Memberikan Sanksi ").setBorder(Border.NO_BORDER).setFontSize(7)
                                .setPaddingLeft(15));
                body4.addCell(new Cell().add(
                                ": ........................................................................................................................................................")
                                .setBorder(Border.NO_BORDER).setFontSize(7).setPaddingLeft(15));
                body.addCell(new Cell().add(body4).setBorder(Border.NO_BORDER));
                // section 6

                // blank line
                document.add(blank1);
                // blank line

                //section 7
                float body5Lenght[] = { 540f };
                Table body5 = new Table(body5Lenght).setHorizontalAlignment(HorizontalAlignment.LEFT);
                body5.addCell(new Cell().add("Dengan penjelasan :").setBold().setBorder(Border.NO_BORDER).setFontSize(7));
                body5.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setHeight(140).setFontSize(7));
                body5.addCell(new Cell().add(".............. , ...... ........... 2024").setBorder(Border.NO_BORDER).setFontSize(7));
                body5.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setHeight(40));
                body5.addCell(new Cell().add("(...........................)").setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setBorderTop(Border.NO_BORDER).setFontSize(7));
                body5.addCell(new Cell().add("" + "").setBorder(Border.NO_BORDER).setFontSize(7).setHeight(40));
                body.addCell(new Cell().add(body5).setBorder(Border.NO_BORDER));
                //section 7

                float body6Lenght[] = { 540f };
                Table body6 = new Table(body6Lenght).setHorizontalAlignment(HorizontalAlignment.LEFT);
                body6.addCell(new Cell().add("Lampiran :").setBold().setFontSize(7).setBorder(Border.NO_BORDER));
                body6.addCell(new Cell().add("Photo Copy sangsi administrasi berupa :").setBold().setFontSize(7).setBorder(Border.NO_BORDER));
                float nestedbody6llenght[] = { 18f, 25f, 18f, 25f, 18f, 25f, 18f, 25f, 18f, 350f };
                Table nested6 = new Table(nestedbody6llenght);
                if(response.getIsPenalty() == 1){
                        nested6.addCell(new Cell().add("").setBackgroundColor(Color.RED));
                } else {
                        nested6.addCell(new Cell().add(""));
                }
                nested6.addCell(new Cell().add("ST").setBorder(Border.NO_BORDER).setFontSize(7).setBold());
                if(response.getIsPenalty() == 2){
                        nested6.addCell(new Cell().add("").setBackgroundColor(Color.RED));
                } else {
                        nested6.addCell(new Cell().add(""));
                }
                nested6.addCell(new Cell().add("SP 1").setBorder(Border.NO_BORDER).setFontSize(7).setBold());
                if(response.getIsPenalty() == 3){
                        nested6.addCell(new Cell().add("").setBackgroundColor(Color.RED));
                } else {
                        nested6.addCell(new Cell().add(""));
                }
                nested6.addCell(new Cell().add("SP 2").setBorder(Border.NO_BORDER).setFontSize(7).setBold());
                if(response.getIsPenalty() == 4){
                        nested6.addCell(new Cell().add("").setBackgroundColor(Color.RED));
                } else {
                        nested6.addCell(new Cell().add(""));
                }
                nested6.addCell(new Cell().add("SP 3").setBorder(Border.NO_BORDER).setFontSize(7).setBold());
                if(response.getIsPenalty() == 5){
                        nested6.addCell(new Cell().add("").setBackgroundColor(Color.RED));
                } else {
                        nested6.addCell(new Cell().add(""));
                }
                nested6.addCell(new Cell().add("Surat Pembebanan/PG").setBorder(Border.NO_BORDER).setFontSize(7).setBold());
                body6.addCell(new Cell().add(nested6).setBorder(Border.NO_BORDER));
                body6.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setHeight(10));
                body6.addCell(new Cell().add("( Form ini agar diserahkan kembali ke Divisi Pengawasan paling lambat tanggal "+convertDateToRoman.convertDateToString(response.getClarification().getEvaluation_limitation())+" )").setBold().setFontSize(7).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                body.addCell(new Cell().add(body6).setBorder(Border.NO_BORDER));

                parent.addCell(new Cell().add(body).setPadding(5));
                document.add(parent);

                document.close();

                return PDFResponse.builder().fileName(fileName).filePath(path).build();
        }

}
