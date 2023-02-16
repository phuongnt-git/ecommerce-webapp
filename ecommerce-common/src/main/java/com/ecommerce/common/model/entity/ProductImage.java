package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static com.ecommerce.common.constant.ImagePath.PRODUCT_IMAGES_DIR;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "PRODUCT_IMAGE",
        indexes = {@Index(columnList = "PRODUCT_IMAGE_ID", name = "PRODUCT_IMAGE_IDX", unique = true)})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_IMAGE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PRODUCT_IMAGE_GEN")
    @TableGenerator(name = "PRODUCT_IMAGE_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "PRODUCT_IMAGE_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "IMAGE_NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Product.class)
    @JoinColumn(name = "PRODUCT_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_IMAGE_PRODUCT"))
    private Product product;

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    @Transient
    public String getImagePath() {
        return PRODUCT_IMAGES_DIR + product.getId() + "/extras/" + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductImage that = (ProductImage) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
