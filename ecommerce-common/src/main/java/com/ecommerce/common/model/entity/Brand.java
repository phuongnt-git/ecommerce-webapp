package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.ecommerce.common.constant.ImagePath.BRAND_LOGOS_DIR;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "BRAND",
        uniqueConstraints = {@UniqueConstraint(name = "UQ_BRAND_NAME", columnNames = "BRAND_NAME")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"logo"})
public class Brand implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BRAND_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BRAND_GEN")
    @TableGenerator(name = "BRAND_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "BRAND_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "BRAND_NAME", nullable = false, length = 64)
    private String name;

    @Column(name = "BRAND_LOGO", nullable = false, length = 128)
    private String logo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "BRAND_CATEGORY",
            joinColumns = @JoinColumn(name = "BRAND_ID", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false),
            foreignKey = @ForeignKey(name = "FK_BRAND_CATEGORY_BRAND"),
            inverseForeignKey = @ForeignKey(name = "FK_BRAND_CATEGORY_CATEGORY"),
            uniqueConstraints = @UniqueConstraint(name = "UQ_BRAND_CATEGORY", columnNames = {"BRAND_ID", "CATEGORY_ID"})
    )
    private Set<Category> categories = new HashSet<>();

    public Brand(Integer id) {
        this.id = id;
    }

    public Brand(String name) {
        this.name = name;
        this.logo = "image-thumbnail.png";
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    public String getLogoPath() {
        if (this.id == null) {
            return "/img/image-thumbnail.png";
        }
        return BRAND_LOGOS_DIR + this.id + "/" + this.logo;
    }

}
