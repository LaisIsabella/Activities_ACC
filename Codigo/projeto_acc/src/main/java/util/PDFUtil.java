package util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Activity;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static boolean generateUserReport(String filePath, List<Activity> activities) {
        Document document = null;
        try {
            // Cria o documento PDF
            document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Define fontes
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

            // Título do relatório
            Paragraph title = new Paragraph("Relatorio de Atividades Complementares", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Informações do estudante (pega da primeira atividade)
            if (!activities.isEmpty() && activities.get(0).getStudent() != null) {
                Paragraph studentInfo = new Paragraph();
                studentInfo.add(new Chunk("Aluno: ", headerFont));
                studentInfo.add(new Chunk(activities.get(0).getStudent().getName() + "\n", normalFont));
                studentInfo.add(new Chunk("Email: ", headerFont));
                studentInfo.add(new Chunk(activities.get(0).getStudent().getEmail() + "\n", normalFont));
                studentInfo.add(new Chunk("RA: ", headerFont));
                studentInfo.add(new Chunk(String.valueOf(activities.get(0).getStudent().getRa()) + "\n", normalFont));
                studentInfo.setSpacingAfter(20);
                document.add(studentInfo);
            }

            // Resumo de horas
            int totalHours = 0;
            for (Activity a : activities) {
                totalHours += a.getHours();
            }

            Paragraph summary = new Paragraph();
            summary.add(new Chunk("Total de Atividades Aprovadas: ", headerFont));
            summary.add(new Chunk(activities.size() + "\n", normalFont));
            summary.add(new Chunk("Total de Horas Aprovadas: ", headerFont));
            summary.add(new Chunk(totalHours + "h\n", normalFont));
            summary.setSpacingAfter(20);
            document.add(summary);

            // Tabela de atividades
            if (!activities.isEmpty()) {
                PdfPTable table = new PdfPTable(5); // 5 colunas
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setSpacingAfter(10);

                // Define larguras das colunas
                float[] columnWidths = {3f, 2f, 1.5f, 1.5f, 2f};
                table.setWidths(columnWidths);

                // Cabeçalho da tabela
                PdfPCell cell1 = new PdfPCell(new Phrase("Atividade", headerFont));
                cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setPadding(5);
                table.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(new Phrase("Tipo", headerFont));
                cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPadding(5);
                table.addCell(cell2);

                PdfPCell cell3 = new PdfPCell(new Phrase("Data", headerFont));
                cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setPadding(5);
                table.addCell(cell3);

                PdfPCell cell4 = new PdfPCell(new Phrase("Horas", headerFont));
                cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setPadding(5);
                table.addCell(cell4);

                PdfPCell cell5 = new PdfPCell(new Phrase("Status", headerFont));
                cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setPadding(5);
                table.addCell(cell5);

                // Adiciona as atividades
                for (Activity a : activities) {
                    // Nome
                    PdfPCell nameCell = new PdfPCell(new Phrase(a.getName(), normalFont));
                    nameCell.setPadding(5);
                    table.addCell(nameCell);

                    // Tipo
                    PdfPCell typeCell = new PdfPCell(new Phrase(a.getActivityType().getName(), normalFont));
                    typeCell.setPadding(5);
                    table.addCell(typeCell);

                    // Data
                    PdfPCell dateCell = new PdfPCell(new Phrase(sdf.format(a.getDate()), normalFont));
                    dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dateCell.setPadding(5);
                    table.addCell(dateCell);

                    // Horas
                    PdfPCell hoursCell = new PdfPCell(new Phrase(a.getHours() + "h", normalFont));
                    hoursCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    hoursCell.setPadding(5);
                    table.addCell(hoursCell);

                    // Status
                    PdfPCell statusCell = new PdfPCell(new Phrase(a.getStatus().toString(), normalFont));
                    statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    statusCell.setPadding(5);
                    table.addCell(statusCell);
                }

                document.add(table);
            } else {
                Paragraph noActivities = new Paragraph("Nenhuma atividade aprovada encontrada.", normalFont);
                noActivities.setSpacingBefore(20);
                document.add(noActivities);
            }

            // Rodapé
            Paragraph footer = new Paragraph();
            footer.setSpacingBefore(30);
            footer.add(new Chunk("Relatorio gerado em: ", normalFont));
            footer.add(new Chunk(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), normalFont));
            document.add(footer);

            document.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (document != null && document.isOpen()) {
                document.close();
            }
            return false;
        }
    }
}