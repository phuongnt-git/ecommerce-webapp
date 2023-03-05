package com.ecommerce.site.admin.controller;

import com.ecommerce.common.exception.OrderNotFoundException;
import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.Product;
import com.ecommerce.common.model.entity.Order;
import com.ecommerce.common.model.entity.OrderDetail;
import com.ecommerce.common.model.entity.Setting;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.paging.PagingAndSortingParam;
import com.ecommerce.site.admin.security.UserDetailsImpl;
import com.ecommerce.site.admin.service.OrderService;
import com.ecommerce.site.admin.service.SettingService;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

/**
 * @author Nguyen Thanh Phuong
 */
@Controller
public class OrderController {
    
    private String defaultRedirectUrl = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

    @Autowired
    private OrderService orderService;
    
    @Autowired 
    private SettingService settingService;

    @GetMapping("/orders")
    public String listFirstPage() {
        return defaultRedirectUrl;
    }

    @GetMapping("/orders/page/{pageNumber}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listOrders", moduleUrl = "/orders") PagingAndSortingHelper helper,
            @PathVariable("pageNumber") int pageNumber,
            HttpServletRequest request,
            @AuthenticationPrincipal @NotNull UserDetailsImpl loggedUser) {

        orderService.listByPage(pageNumber, helper);
        loadCurrencySetting(request);

        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Salesperson") && loggedUser.hasRole("Shipper")) {
            return "order/order_shipper";
        }

        return "order/orders";
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes attributes, HttpServletRequest request,
                                   @AuthenticationPrincipal @NotNull UserDetailsImpl loggedUser) {
        try {
            Order order = orderService.get(id);
            loadCurrencySetting(request);

            boolean isVisibleForAdminOrSalesperson = false;

            if (loggedUser.hasRole("Admin") || loggedUser.hasRole("Salesperson")) {
                isVisibleForAdminOrSalesperson = true;
            }

            model.addAttribute("isVisibleForAdminOrSalesperson", isVisibleForAdminOrSalesperson);
            model.addAttribute("order", order);

            return "order/order_detail_modal";
        } catch (OrderNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model,
                              @NotNull RedirectAttributes attributes) {
        try {
            orderService.delete(id);;
            attributes.addFlashAttribute("message", String.format("The order ID %s has been deleted", id));
        } catch (OrderNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return defaultRedirectUrl;
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable("id") Integer id, @NotNull Model model,
                            RedirectAttributes attributes, HttpServletRequest request) {
        try {
            Order order = orderService.get(id);;
            List<Country> listCountries = orderService.listAllCountries();

            model.addAttribute("pageTitle", String.format("Edit Order (ID: %s)", id));
            model.addAttribute("order", order);
            model.addAttribute("listCountries", listCountries);

            return "order/order_form";
        } catch (OrderNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

    private void updateProductDetails(@NotNull Order order, @NotNull HttpServletRequest request) {
        String[] detailIds = request.getParameterValues("detailId");
        String[] productIds = request.getParameterValues("productId");
        String[] productPrices = request.getParameterValues("productPrice");
        String[] productDetailCosts = request.getParameterValues("productDetailCost");
        String[] quantities = request.getParameterValues("quantity");
        String[] productSubtotals = request.getParameterValues("productSubtotal");
        String[] productShipCosts = request.getParameterValues("productShipCost");

        Set<OrderDetail> orderDetails = order.getOrderDetails();

        for (int i = 0; i < detailIds.length; i++) {
            OrderDetail orderDetail = new OrderDetail();
            Integer detailId = Integer.parseInt(detailIds[i]);
            if (detailId > 0) {
                orderDetail.setId(detailId);
            }

            orderDetail.setOrder(order);
            orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
            orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
            orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
            orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
            orderDetail.setQuantity(Integer.parseInt(quantities[i]));
            orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));

            orderDetails.add(orderDetail);
        }
    }

}
