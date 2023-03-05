package com.ecommerce.common.model.entity;

import com.ecommerce.common.model.AbstractAddress;
import com.ecommerce.common.model.enums.OrderStatus;
import com.ecommerce.common.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@NoArgsConstructor
public class Order extends AbstractAddress implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ORDER_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ORDER_GEN")
    @TableGenerator(name = "ORDER_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "ORDER_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(nullable = false, length = 64)
    private String country;

    private Date orderTime;

    private float shippingCost;

    private float productCost;

    private float subtotal;

    private float tax;

    private float total;

    private int deliverDays;

    private Date deliverDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", foreignKey = @ForeignKey(name = "FK_ORDER_CUSTOMER"))
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("updatedTime ASC")
    private List<OrderTrack> orderTracks = new ArrayList<>();

    public Order(Integer id, Date orderTime, float productCost, float subtotal, float total) {
        this.id = id;
        this.orderTime = orderTime;
        this.productCost = productCost;
        this.subtotal = subtotal;
        this.total = total;
    }

    public void copyAddressFromCustomer() {
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setPhoneNumber(customer.getPhoneNumber());
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setCity(customer.getCity());
        setCountry(customer.getCountry().getName());
        setPostalCode(customer.getPostalCode());
        setState(customer.getState());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", subtotal=" + subtotal +
                ", paymentMethod=" + paymentMethod +
                ", status=" + status +
                ", customer=" + customer.getFullName() +
                '}';
    }


    @Transient
    public String getDestination() {
        String destination =  city + ", ";
        if (state != null && !state.isEmpty()) {
            destination += state + ", ";
        }
        destination += country;

        return destination;
    }

}
