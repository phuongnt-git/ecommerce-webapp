package com.ecommerce.site.admin.helper;

import com.ecommerce.common.model.entity.Product;
import com.ecommerce.common.model.entity.ProductImage;
import com.ecommerce.site.admin.utils.FileUploadUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Nguyen Thanh Phuong
 */
public class ProductSaveHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

    public static void deleteExtraImagesRemovedOnForm(@NotNull Product product) {
        String extraImageDir = "../images/product-images/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String filename = file.toFile().getName();

                if (!product.containsImageName(filename)) {
                    try {
                        Files.delete(file);
                        LOGGER.info("Deleted extra image: " + filename);
                    } catch (IOException e) {
                        LOGGER.error("Could not delete extra image: " + filename);
                    }
                }
            });
        } catch (IOException ex) {
            LOGGER.error("Could not list directory: " + dirPath);
        }
    }

    public static void setExistingExtraImageNames(String[] imageIds, String[] imageNames, Product product) {
        if (imageIds == null || imageIds.length == 0) {
            return;
        }

        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count < imageIds.length; count++) {
            Integer id = Integer.parseInt(imageIds[count]);
            String name = imageNames[count];

            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    public static void setProductDetails(String[] detailIds, String[] detailNames,
                                         String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) {
            return;
        }

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            int id = Integer.parseInt(detailIds[count]);

            if (id != 0) {
                product.addDetail(id, name, value);
            } else if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }
    }

    public static void saveUploadedImages(@NotNull MultipartFile mainImageMultipart,
                                          MultipartFile[] extraImageMultipart,
                                          Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            String uploadDir = "../images/product-images/" + savedProduct.getId();

            FileUploadUtils.cleanDir(uploadDir);
            FileUploadUtils.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        if (extraImageMultipart.length > 0) {
            String uploadDir = "../images/product-images/" + savedProduct.getId() + "/extras";

            for (MultipartFile multipartFile : extraImageMultipart) {
                if (multipartFile.isEmpty()) {
                    continue;
                }

                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUploadUtils.saveFile(uploadDir, fileName, multipartFile);
            }
        }

    }

    public static void setNewExtraImageNames(MultipartFile @NotNull [] extraImageMultipart, Product product) {
        for (MultipartFile multipartFile : extraImageMultipart) {
            if (!multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

                if (!product.containsImageName(fileName)) {
                    product.addExtraImage(fileName);
                }
            }
        }
    }

    public static void setMainImageName(@NotNull MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            product.setMainImage(fileName);
        }
    }

}
