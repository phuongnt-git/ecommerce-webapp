package com.ecommerce.site.admin.export;

import com.ecommerce.common.model.entity.User;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
public class UserPdfExporter extends AbstractExporter {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf", ".pdf", "users_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.GRAY);

        Paragraph paragraph = new Paragraph("List of User", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 1.7f});

        writeTableHeader(table);
        writeTableData(table, listUsers);

        document.add(table);

        document.close();
    }

    private void writeTableData(PdfPTable table, @NotNull List<User> listUsers) {
        for (User user : listUsers) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.isEnabled()));
        }
    }

    private void writeTableHeader(@NotNull PdfPTable table) {
        PdfPCell pCell = new PdfPCell();
        pCell.setBackgroundColor(Color.GRAY);
        pCell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        pCell.setPhrase(new Phrase("ID", font));
        table.addCell(pCell);

        pCell.setPhrase(new Phrase("Email", font));
        table.addCell(pCell);

        pCell.setPhrase(new Phrase("First Name", font));
        table.addCell(pCell);

        pCell.setPhrase(new Phrase("Last Name", font));
        table.addCell(pCell);

        pCell.setPhrase(new Phrase("Roles ", font));
        table.addCell(pCell);

        pCell.setPhrase(new Phrase("Enabled", font));
        table.addCell(pCell);
    }

}
