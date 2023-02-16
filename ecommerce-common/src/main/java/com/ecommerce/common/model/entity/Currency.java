package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "CURRENCY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Currency implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CURRENCY_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CURRENCY_GEN")
    @TableGenerator(name = "CURRENCY_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "CURRENCY_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "CURRENCY_NAME", nullable = false, length = 64)
    private String name;

    @Column(name = "CURRENCY_SYMBOL", nullable = false, length = 3)
    private String symbol;

    @Column(name = "CURRENCY_CODE", nullable = false, length = 4)
    private String code;

    @Override
    public String toString() {
        return name + " - " + code + " - " + symbol;
    }

}
