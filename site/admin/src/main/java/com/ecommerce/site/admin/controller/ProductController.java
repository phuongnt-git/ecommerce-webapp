package com.ecommerce.site.admin.controller;

import com.ecommerce.site.admin.helper.ProductSaveHelper;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.annotation.PagingAndSortingParam;
import com.ecommerce.site.admin.security.CustomUserDetailsImpl;
import com.ecommerce.site.admin.service.BrandService;
import com.ecommerce.site.admin.service.CategoryService;
import com.ecommerce.site.admin.service.ProductService;
import com.ecommerce.site.admin.utils.FileUploadUtils;
import com.ecommerce.common.exception.ProductNotFoundException;
import com.ecommerce.common.model.entity.Brand;
import com.ecommerce.common.model.entity.Category;
import com.ecommerce.common.model.entity.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.ecommerce.site.admin.constant.ApplicationConstant.PRODUCT_IMAGES_DIR;

/**
 * @author Nguyen Thanh Phuong
 */
@Controller
public class ProductController {

    private final String defaultRedirectUrl = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage() {
        return defaultRedirectUrl;
    }

    @GetMapping("/products/page/{pageNumber}")
    public String listByPage(@PagingAndSortingParam(listName = "listProducts", moduleUrl = "/products") PagingAndSortingHelper helper,
                             @PathVariable("pageNumber") int pageNumber,
                             @NotNull Model model,
                             Integer categoryId) {
        productService.listByPage(pageNumber, helper, categoryId);

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("listCategories", listCategories);

        return "product/products";
    }

    @GetMapping("/products/new")
    public String newProduct(@NotNull Model model) {
        List<Brand> listBrands = brandService.listAll();
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberOfExistingExtraImages", 0);

        return "product/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes attributes,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
                              @RequestParam(name = "detailIds", required = false) String[] detailIds,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIds", required = false) String[] imageIds,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal @NotNull CustomUserDetailsImpl loggedUser
    ) throws IOException {
        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Salesperson")) {
                productService.saveProductPrice(product);
                attributes.addFlashAttribute("message", "The product has been saved successfully.");
                return defaultRedirectUrl;
            }
        }
        ProductSaveHelper.setMainImageName(mainImageMultipart, product);
        ProductSaveHelper.setExistingExtraImageNames(imageIds, imageNames, product);
        ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
        ProductSaveHelper.setProductDetails(detailIds, detailNames, detailValues, product);

        Product savedProduct = productService.save(product);

        ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
        ProductSaveHelper.deleteExtraImagesRemovedOnForm(product);

        attributes.addFlashAttribute("message", "The product has been saved successfully");

        return defaultRedirectUrl;
    }


    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean enabled,
                                             @NotNull RedirectAttributes attributes,
                                             @NotNull HttpServletRequest request) {
        productService.updateEnabledStatus(id, enabled);

        attributes.addFlashAttribute("message", String.format("The product ID %s has been %s", id, enabled ? "enabled" : "disabled"));

        return String.format("redirect:%s", request.getHeader("Referer"));
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id,
                                @NotNull RedirectAttributes attributes,
                                @NotNull HttpServletRequest request) {
        try {
            productService.delete(id);
            String productExtraImagesDir = PRODUCT_IMAGES_DIR + "/" + id + "/extras";
            String productImagesDir = PRODUCT_IMAGES_DIR + "/" + id;

            FileUploadUtils.removeDir(productExtraImagesDir);
            FileUploadUtils.removeDir(productImagesDir);

            attributes.addFlashAttribute("message", String.format("The product ID %s has been deleted successfully", id));
        } catch (ProductNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return String.format("redirect:%s", request.getHeader("Referer"));
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes attributes,
                              @AuthenticationPrincipal @NotNull CustomUserDetailsImpl loggedUser) {
        try {
            Product product = productService.get(id);
            List<Brand> listBrands = brandService.listAll();
            Integer numberOfExistingExtraImages = product.getImages().size();

            boolean isReadOnlyForSalesperson = false;

            if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if (loggedUser.hasRole("Salesperson")) {
                    isReadOnlyForSalesperson = true;
                }
            }

            model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
            model.addAttribute("product", product);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("pageTitle", String.format("Edit Product (ID: %s)", id));
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

            return "product/product_form";
        } catch (ProductNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id,
                                     @NotNull Model model,
                                     RedirectAttributes attributes) {
        try {
            Product product = productService.get(id);
            model.addAttribute("product", product);

            return "product/product_detail_modal";
        } catch (ProductNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

}
