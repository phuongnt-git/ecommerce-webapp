package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "PRODUCT_DETAIL",
        indexes = {@Index(columnList = "ID", name = "PRODUCT_DETAIL_IDX", unique = true)})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PRODUCT_DETAIL_GEN")
    @TableGenerator(name = "PRODUCT_DETAIL_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "PRODUCT_DETAIL_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "DETAIL_NAME", nullable = false, length = 256)
    private String name;

    @Column(name = "DETAIL_VALUE", nullable = false, length = 256)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Product.class)
    @JoinColumn(name = "PRODUCT_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_DETAIL_PRODUCT"))
    private Product product;

    public ProductDetail(String name, String value, Product product) {
        this.name = name;
        this.value = value;
        this.product = product;
    }

}

