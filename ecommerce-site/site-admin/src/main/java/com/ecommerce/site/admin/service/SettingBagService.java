package com.ecommerce.site.admin.service;

import com.ecommerce.common.model.entity.Setting;
import com.ecommerce.common.model.SettingBag;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
public class SettingBagService extends SettingBag {

    public SettingBagService(List<Setting> listSettings) {
        super(listSettings);
    }

    public void updateCurrencySymbol(String value) {
        super.update("CURRENCY_SYMBOL", value);
    }

    public void updateSiteLogo(String value) {
        super.update("SITE_LOGO", value);
    }

}
