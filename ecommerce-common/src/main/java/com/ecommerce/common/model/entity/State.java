package com.ecommerce.common.model.entity;

import com.ecommerce.common.model.entity.Country;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "STATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class State implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "STATE_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "STATE_GEN")
    @TableGenerator(name = "STATE_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "STATE_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "STATE_NAME", nullable = false, length = 64)
    private String name;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID")
    @ToString.Exclude
    private Country country;

}
