package com.ecommerce.site.admin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Nguyen Thanh Phuong
 */
public class FileUploadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtils.class);

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException(String.format("Could not save file %s", fileName), e);
        }
    }

    public static void cleanDir(String directory) {
        Path dirPath = Paths.get(directory);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        LOGGER.error(String.format("Could not delete file %s%n", file));
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error(String.format("Could not list directory %s%n", dirPath));
        }
    }

    public static void removeDir(String directory) {
        cleanDir(directory);

        try {
            Files.delete(Paths.get(directory));
        } catch (IOException e) {
            LOGGER.error(String.format("Could not remove directory %s", directory));
        }
    }

}
