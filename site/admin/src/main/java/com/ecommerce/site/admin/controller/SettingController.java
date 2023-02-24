package com.ecommerce.site.admin.controller;

import com.ecommerce.site.admin.repository.CurrencyRepository;
import com.ecommerce.site.admin.service.SettingBagService;
import com.ecommerce.site.admin.service.SettingService;
import com.ecommerce.site.admin.utils.FileUploadUtils;
import com.ecommerce.common.model.entity.Currency;
import com.ecommerce.common.model.entity.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ecommerce.site.admin.constant.ApplicationConstant.SITE_LOGO_DIR;

/**
 * @author Nguyen Thanh Phuong
 */
@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/settings")
    public String listAll(@NotNull Model model) {
        List<Setting> listSettings = settingService.listAllSettings();
        List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);

        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "setting/settings";
    }

    @PostMapping("/settings/save-general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request,
                                      @NotNull RedirectAttributes attributes) throws IOException {
        SettingBagService settingBag = settingService.getGeneralSettings();

        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(request, settingBag);
        updateSettingValuesFromForm(request, settingBag.list());

        attributes.addFlashAttribute("message", "General settings have been saved");

        return "redirect:/settings";
    }

    private void saveSiteLogo(@NotNull MultipartFile multipartFile, SettingBagService settingBag) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String value = "/images/site-logo/" + fileName;
            settingBag.updateSiteLogo(value);

            FileUploadUtils.cleanDir(SITE_LOGO_DIR);
            FileUploadUtils.saveFile(SITE_LOGO_DIR, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(@NotNull HttpServletRequest request, SettingBagService settingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findById = currencyRepository.findById(currencyId);

        if (findById.isPresent()) {
            Currency currency = findById.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, @NotNull List<Setting> listSettings) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }

        settingService.saveAll(listSettings);
    }

    @PostMapping("/settings/save-mail-server")
    public String saveMailServerSettings(HttpServletRequest request, @NotNull RedirectAttributes attributes) {
        List<Setting> mailServerSettings = settingService.getMailServerSettings();
        updateSettingValuesFromForm(request, mailServerSettings);
        attributes.addFlashAttribute("message", "Mail server settings have been saved");

        return "redirect:/settings#mailServer";
    }

    @PostMapping("/settings/save-mail-templates")
    public String saveMailTemplateSettings(HttpServletRequest request, @NotNull RedirectAttributes attributes) {
        List<Setting> mailTemplateSettings = settingService.getMailTemplateSettings();
        updateSettingValuesFromForm(request, mailTemplateSettings);
        attributes.addFlashAttribute("message", "Mail template settings have been saved");

        return "redirect:/settings#mailTemplates";
    }

    @PostMapping("/settings/save-payment")
    public String savePaymentSettings(HttpServletRequest request, @NotNull RedirectAttributes attributes) {
        List<Setting> paymentSettings = settingService.getPaymentSettings();
        updateSettingValuesFromForm(request, paymentSettings);
        attributes.addFlashAttribute("message", "Payment settings have been saved");

        return "redirect:/settings#payment";
    }

}

