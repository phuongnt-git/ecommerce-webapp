package com.ecommerce.site.admin.export;

import com.ecommerce.common.model.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
public class UserExcelExporter extends AbstractExporter {

    private final XSSFWorkbook xssfWorkbook;

    private XSSFSheet xssfSheet;

    public UserExcelExporter() {
        xssfWorkbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        xssfSheet = xssfWorkbook.createSheet("Users");
        XSSFRow xssfRow = xssfSheet.createRow(0);

        XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setBold(true);
        xssfFont.setFontHeight(16);
        xssfCellStyle.setFont(xssfFont);

        createCell(xssfRow, 0, "User Id", xssfCellStyle);
        createCell(xssfRow, 1, "E-mail", xssfCellStyle);
        createCell(xssfRow, 2, "First Name", xssfCellStyle);
        createCell(xssfRow, 3, "Last Name", xssfCellStyle);
        createCell(xssfRow, 4, "Roles", xssfCellStyle);
        createCell(xssfRow, 5, "Enabled", xssfCellStyle);

    }

    private void createCell(@NotNull XSSFRow row, int columnIndex, Object value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        xssfSheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);
    }

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream", ".xlsx", "users_");

        writeHeaderLine();
        writeDataLines(listUsers);

        ServletOutputStream outputStream = response.getOutputStream();
        xssfWorkbook.write(outputStream);
        xssfWorkbook.close();
        outputStream.close();
    }

    private void writeDataLines(@NotNull List<User> listUsers) {
        int rowIndex = 1;

        XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setFontHeight(14);
        xssfCellStyle.setFont(xssfFont);

        for (User user : listUsers) {
            XSSFRow xssfRow = xssfSheet.createRow(rowIndex++);
            int columnIndex = 0;

            createCell(xssfRow, columnIndex++, user.getId(), xssfCellStyle);
            createCell(xssfRow, columnIndex++, user.getEmail(), xssfCellStyle);
            createCell(xssfRow, columnIndex++, user.getFirstName(), xssfCellStyle);
            createCell(xssfRow, columnIndex++, user.getLastName(), xssfCellStyle);
            createCell(xssfRow, columnIndex++, user.getRoles().toString(), xssfCellStyle);
            createCell(xssfRow, columnIndex, user.isEnabled(), xssfCellStyle);
        }
    }

}
