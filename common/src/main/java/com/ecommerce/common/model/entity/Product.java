package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.ecommerce.common.constant.ImagePath.PRODUCT_IMAGES_DIR;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "PRODUCT",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"PRODUCT_NAME"}, name = "UQ_PRODUCT_NAME"),
                @UniqueConstraint(columnNames = {"PRODUCT_ALIAS"}, name = "UQ_PRODUCT_ALIAS")},
        indexes = {
                @Index(columnList = "PRODUCT_ID", name = "PRODUCT_IDX", unique = true)})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PRODUCT_GEN")
    @TableGenerator(name = "PRODUCT_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "PRODUCT_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "PRODUCT_NAME", nullable = false, length = 256)
    private String name;

    @Column(name = "PRODUCT_ALIAS", nullable = false, length = 256)
    private String alias;

    @Column(name = "SHORT_DESCRIPTION", nullable = false, length = 512)
    private String shortDescription;

    @Column(name = "FULL_DESCRIPTION", nullable = false, length = 4096)
    private String fullDescription;

    @Column(name = "CREATED_TIME", nullable = false, updatable = false)
    private Date createdTime;

    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean enabled;

    @Column(name = "IN_STOCK")
    private boolean inStock;

    @Column(name = "PRODUCT_COST")
    private float cost;

    @Column(name = "PRODUCT_PRICE")
    private float price;

    @Column(name = "DISCOUNT_PERCENT")
    private float discountPercent;

    @Column(name = "PRODUCT_LENGTH")
    private float length;

    @Column(name = "PRODUCT_WIDTH")
    private float width;

    @Column(name = "PRODUCT_HEIGHT")
    private float height;

    @Column(name = "PRODUCT_WEIGHT")
    private float weight;

    @Column(name = "MAIN_IMAGE", nullable = false)
    private String mainImage;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Category.class)
    @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_CATEGORY"))
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Brand.class)
    @JoinColumn(name = "BRAND_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_BRAND"))
    private Brand brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ProductImage.class)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ProductDetail.class)
    private List<ProductDetail> details = new ArrayList<>();

    @Column(name = "REVIEW_COUNT")
    private int reviewCount;

    @Column(name = "AVERAGE_RATING")
    private float averageRating;

    @Transient
    private boolean customerCanReview;

    @Transient
    private boolean reviewedByCustomer;

    public void addExtraImage(String imageName) {
        this.images.add(new ProductImage(imageName, this));
    }

    @Transient
    public String getMainImagePath() {
        if (id == null || mainImage == null) {
            return "/img/image-thumbnail.png";
        }
        return PRODUCT_IMAGES_DIR + this.id + "/" + this.mainImage;
    }

    public boolean containsImageName(String imageName) {
        for (ProductImage image : images) {
            if (image.getName().equals(imageName)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @Transient
    public String getShortName() {
        if (name.length() > 70) {
            return name.substring(0, 70).concat("...");
        }
        return name;
    }

    @Transient
    public float getDiscountPrice() {
        if (discountPercent > 0) {
            return price * ((100 - discountPercent) / 100);
        }
        return this.price;
    }
    @Transient
    public String getUri() {
        return "/p/" + this.alias + "/";
    }

    public void addDetail(String name, String value) {
        this.details.add(new ProductDetail(name, value, this));
    }

    public void addDetail(Integer id, String name, String value) {
        this.details.add(new ProductDetail(id, name, value, this));
    }

}
