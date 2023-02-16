package com.ecommerce.site.shop.repository;

import com.ecommerce.common.model.entity.Setting;
import com.ecommerce.common.model.enums.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, String> {

    List<Setting> findAllByCategory(SettingCategory category);
}
