package com.ecommerce.common.model.entity;

import com.ecommerce.common.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "ORDER_TRACK")
@Getter
@Setter
public class OrderTrack implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ORDER_TRACK_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ORDER_TRACK_GEN")
    @TableGenerator(name = "ORDER_TRACK_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "ORDER_TRACK_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(length = 256)
    private String notes;

    private Date updatedTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 64, nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", foreignKey = @ForeignKey(name = "FK_ORDER_TRACK_ORDER"))
    private Order order;

    @Transient
    public String getUpdatedTimeOnForm() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        return dateFormatter.format(this.updatedTime);
    }

    public void setUpdatedTimeOnForm(String dateString) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        try {
            this.updatedTime = dateFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
