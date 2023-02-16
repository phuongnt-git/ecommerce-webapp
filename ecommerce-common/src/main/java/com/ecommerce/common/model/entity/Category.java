package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.ecommerce.common.constant.ImagePath.CATEGORY_IMAGES_DIR;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "CATEGORY", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"CATEGORY_NAME"}, name = "UQ_CATEGORY_NAME"),
        @UniqueConstraint(columnNames = {"CATEGORY_ALIAS"}, name = "UQ_CATEGORY_ALIAS")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CATEGORY_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CATEGORY_GEN")
    @TableGenerator(name = "CATEGORY_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "CATEGORY_SEQ_NEXT_VAL",
            allocationSize = 1)
    private Integer id;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 128)
    private String name;

    @Column(name = "CATEGORY_ALIAS", nullable = false, length = 128)
    private String alias;

    @Column(name = "CATEGORY_IMAGE", nullable = false, length = 256)
    private String image;

    @Column(name = "IS_ACTIVE")
    private boolean enabled;

    @Column(name = "ALL_PARENT_IDS", length = 256)
    private String allParentIds;

    @OneToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    @Transient
    private boolean hasChildren;

    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "image-thumbnail.png";
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public static @NonNull Category copyIdAndName(@NonNull Category category) {
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());

        return copyCategory;
    }

    public static @NonNull Category copyIdAndName(Integer id, String name) {
        Category copyCategory = new Category();
        copyCategory.setId(id);
        copyCategory.setName(name);

        return copyCategory;
    }

    public static @NonNull Category copyFull(@NonNull Category category) {
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        copyCategory.setImage(category.getImage());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setHasChildren(category.getChildren().size() > 0);

        return copyCategory;
    }

    public static @NonNull Category copyFull(Category category, String name) {
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);

        return copyCategory;
    }

    @Transient
    public String getImagePath() {
        if (this.id == null) {
            return "/img/image-thumbnail.png";
        }
        return CATEGORY_IMAGES_DIR + this.id + "/" + this.image;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Category category = (Category) o;

        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
