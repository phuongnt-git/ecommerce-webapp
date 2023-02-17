package com.ecommerce.common.model;

import com.ecommerce.common.model.entity.Setting;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
public class SettingBag {

    private final List<Setting> listSettings;

    public SettingBag(List<Setting> listSettings) {
        this.listSettings = listSettings;
    }

    public Setting get(String key) {
        int index = listSettings.indexOf(new Setting().toBuilder().key(key).build());
        if (index >= 0) {
            return listSettings.get(index);
        }
        return null;
    }

    public String getValue(String key) {
        Setting setting = get(key);
        if (setting != null) {
            return setting.getValue();
        }
        return null;
    }

    public void update(String key, String value) {
        Setting setting = get(key);
        if (setting != null && value != null) {
            setting.setValue(value);
        }
    }

    public List<Setting> list() {
        return listSettings;
    }

}
