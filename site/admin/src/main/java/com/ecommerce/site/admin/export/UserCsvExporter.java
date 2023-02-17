package com.ecommerce.site.admin.export;

import com.ecommerce.common.model.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
public class UserCsvExporter extends AbstractExporter {

    public void export(@NotNull List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "users_");

        ICsvBeanWriter iCsvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

        iCsvBeanWriter.writeHeader(csvHeader);

        for (User user : listUsers) {
            iCsvBeanWriter.write(user, fieldMapping);
        }

        iCsvBeanWriter.close();
    }

}
