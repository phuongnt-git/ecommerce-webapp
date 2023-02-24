package com.ecommerce.common.model.entity;

import com.ecommerce.common.model.enums.SettingCategory;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "SETTING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(exclude = {"category"})
public class Setting implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SETTING_KEY", nullable = false, length = 128)
    private String key;

    @Column(name = "SETTING_VALUE", nullable = false, length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY", nullable = false, length = 64)
    private SettingCategory category;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Setting setting = (Setting) o;

        return Objects.equals(key, setting.key);
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

}
