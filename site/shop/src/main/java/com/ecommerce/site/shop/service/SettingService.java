package com.ecommerce.site.shop.service;

import com.ecommerce.common.model.entity.Setting;
import com.ecommerce.common.model.enums.SettingCategory;
import com.ecommerce.site.shop.utils.EmailSettingBagUtils;
import com.ecommerce.site.shop.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    public EmailSettingBagUtils getEmailSettings() {
        List<Setting> emailSetting = settingRepository.findAllByCategory(SettingCategory.MAIL_SERVER);
        emailSetting.addAll(settingRepository.findAllByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBagUtils(emailSetting);
    }

}
