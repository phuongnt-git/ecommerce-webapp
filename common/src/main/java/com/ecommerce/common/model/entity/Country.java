package com.ecommerce.common.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "COUNTRY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Country implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COUNTRY_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COUNTRY_GEN")
    @TableGenerator(name = "COUNTRY_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "COUNTRY_SEQ_NEXT_VAL",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "COUNTRY_NAME", nullable = false, length = 64)
    private String name;

    @Column(name = "COUNTRY_CODE", nullable = false, length = 5)
    private String code;

    @OneToMany(mappedBy = "country")
    @ToString.Exclude
    @JsonIgnore
    private Set<State> states;

}
