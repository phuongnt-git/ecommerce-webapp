package com.ecommerce.site.shop.service;

import com.ecommerce.common.model.entity.Setting;
import com.ecommerce.common.model.enums.SettingCategory;
import com.ecommerce.site.shop.repository.SettingRepository;
import com.ecommerce.site.shop.utils.EmailSettingBagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> getGeneralSettings() {
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public EmailSettingBagUtils getEmailSettings() {
        List<Setting> emailSetting = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        emailSetting.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBagUtils(emailSetting);
    }

}
