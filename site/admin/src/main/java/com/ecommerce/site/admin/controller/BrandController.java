package com.ecommerce.site.admin.controller;

import com.ecommerce.common.exception.BrandNotFoundException;
import com.ecommerce.common.model.entity.Brand;
import com.ecommerce.common.model.entity.Category;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.paging.PagingAndSortingParam;
import com.ecommerce.site.admin.service.BrandService;
import com.ecommerce.site.admin.service.CategoryService;
import com.ecommerce.site.admin.utils.FileUploadUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.ecommerce.site.admin.constant.ApplicationConstant.BRAND_LOGOS_DIR;

/**
 * @author Nguyen Thanh Phuong
 */
@Controller
public class BrandController {

    private final String defaultRedirectUrl = "redirect:/brands/page/1?sortField=name&sortDir=asc";

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listFirstPage() {
        return defaultRedirectUrl;
    }

    @GetMapping("/brands/page/{pageNumber}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listBrands", moduleUrl = "/brands") PagingAndSortingHelper helper,
            @PathVariable("pageNumber") int pageNumber) {
        brandService.listByPage(pageNumber, helper);

        return "brand/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(@NotNull Model model) {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");

        return "brand/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand,
                            @NotNull @RequestParam("fileImage") MultipartFile multipartFile,
                            RedirectAttributes attributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            brand.setLogo(fileName);

            Brand savedBrand = brandService.save(brand);

            String uploadDir = BRAND_LOGOS_DIR + "/" + savedBrand.getId();

            FileUploadUtils.cleanDir(uploadDir);
            FileUploadUtils.saveFile(uploadDir, fileName, multipartFile);
        } else {
            brandService.save(brand);
        }

        attributes.addFlashAttribute("message", "The brand has been saved successfully");
        return String.format("redirect:/brands/page/1?sortField=id&sortDir=asc&keyword=%s", brand.getName());
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable("id") Integer id,
                            @NotNull Model model,
                            RedirectAttributes attributes) {
        try {
            Brand brand = brandService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("brand", brand);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", String.format("Edit Brand (ID: %s)", id));

            return "brand/brand_form";
        } catch (BrandNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable("id") Integer id,
                              @NotNull RedirectAttributes attributes,
                              @NotNull HttpServletRequest request) {
        try {
            brandService.delete(id);
            String brandDir = BRAND_LOGOS_DIR + "/" + id;
            FileUploadUtils.removeDir(brandDir);

            attributes.addFlashAttribute("message", String.format("The brand ID %s has been deleted successfully", id));
        } catch (BrandNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return String.format("redirect:%s", request.getHeader("Referer"));
    }

}
