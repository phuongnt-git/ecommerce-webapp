package com.ecommerce.site.admin.controller;

import com.ecommerce.common.exception.CategoryNotFoundException;
import com.ecommerce.common.model.entity.Category;
import com.ecommerce.site.admin.export.CategoryCsvExporter;
import com.ecommerce.site.admin.service.CategoryService;
import com.ecommerce.site.admin.utils.FileUploadUtils;
import com.ecommerce.site.admin.utils.PagingAndSortingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import static com.ecommerce.site.admin.constant.ApplicationConstant.CATEGORY_IMAGES_DIR;
import static com.ecommerce.site.admin.constant.ApplicationConstant.ROOT_CATEGORIES_PER_PAGE;

/**
 * @author Nguyen Thanh Phuong
 */
@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public String listFirstPage(String sortDir, Model model) {
        return listByPage(1, sortDir, null, model);
    }

    @GetMapping("/categories/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") int pageNumber,
                             String sortDir,
                             String keyword,
                             @NotNull Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        PagingAndSortingUtils info = new PagingAndSortingUtils();
        List<Category> listCategories = service.listByPage(info, pageNumber, sortDir, keyword);

        long startPage = (long) (pageNumber - 1) * ROOT_CATEGORIES_PER_PAGE + 1;
        long endPage = startPage + ROOT_CATEGORIES_PER_PAGE - 1;
        if (endPage > info.getTotalElements()) {
            endPage = info.getTotalElements();
        }

        String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

        model.addAttribute("totalPages", info.getTotalPages());
        model.addAttribute("totalItems", info.getTotalElements());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("moduleUrl", "/categories");

        return "category/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(@NotNull Model model) {
        List<Category> listCategories = service.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        return "category/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage") @NotNull MultipartFile multipartFile,
                               RedirectAttributes attributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            category.setImage(fileName);

            Category savedCategory = service.save(category);
            String uploadDir = CATEGORY_IMAGES_DIR + "/" + savedCategory.getId();

            FileUploadUtils.cleanDir(uploadDir);
            FileUploadUtils.saveFile(uploadDir, fileName, multipartFile);
        } else {
            service.save(category);
        }

        attributes.addFlashAttribute("message", "The category has been saved successfully");

        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id,
                               @NotNull Model model,
                               RedirectAttributes attributes) {
        try {
            Category category = service.get(id);
            List<Category> listCategories = service.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", String.format("Edit Category (ID: %s)", id));

            return "category/category_form";
        } catch (CategoryNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("id") Integer id,
                                      @PathVariable("status") boolean enabled,
                                      @NotNull RedirectAttributes attributes,
                                      @NotNull HttpServletRequest request) {
        service.updateEnabledStatus(id, enabled);
        attributes.addFlashAttribute("message", String.format("The category ID %s has been %s", id, enabled ? "enabled" : "disabled"));

        return String.format("redirect:%s", request.getHeader("Referer"));
    }

    @GetMapping("categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id,
                                 @NotNull RedirectAttributes attributes,
                                 @NotNull HttpServletRequest request) {
        try {
            service.delete(id);
            FileUploadUtils.removeDir(CATEGORY_IMAGES_DIR + "/" + id);

            attributes.addFlashAttribute("message", String.format("The category ID %s has been deleted successfully", id));
        } catch (CategoryNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return String.format("redirect:%s", request.getHeader("Referer"));
    }

    @GetMapping("/categories/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Category> listCategories = service.listCategoriesUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(listCategories, response);
    }

}
