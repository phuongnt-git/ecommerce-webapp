package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "ORDER_DETAIL")
@Getter
@Setter
@NoArgsConstructor
public class OrderDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ORDER_DETAIL_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ORDER_DETAIL_GEN")
    @TableGenerator(name = "ORDER_DETAIL_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "ORDER_DETAIL_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    private int quantity;

    private float productCost;

    private float shippingCost;

    private float unitPrice;

    private float subtotal;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", foreignKey = @ForeignKey(name = "FK_ORDER_DETAIL_PRODUCT"))
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", foreignKey = @ForeignKey(name = "FK_ORDER_DETAIL_ORDER"))
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderDetail that = (OrderDetail) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public OrderDetail(String categoryName, int quantity, float productCost, float shippingCost, float subtotal) {
        this.product = new Product();
        this.product.setCategory(new Category(categoryName));
        this.quantity = quantity;
        this.productCost = productCost * quantity;
        this.shippingCost = shippingCost;
        this.subtotal = subtotal;
    }

    public OrderDetail(int quantity, String productName, float productCost, float shippingCost, float subtotal) {
        this.product = new Product(productName);
        this.quantity = quantity;
        this.productCost = productCost * quantity;
        this.shippingCost = shippingCost;
        this.subtotal = subtotal;
    }

}
