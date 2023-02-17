package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "TOKEN")
@Getter
@Setter
public class Token implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TOKEN_GEN")
    @TableGenerator(name = "TOKEN_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "TOKEN_SEQ_NEXT_VAL",
            allocationSize = 1)
    private Integer id;

    @Column(name = "IDENTIFIER", length = 256)
    private String identifier;

    @Column(name = "TOKEN", length = 256)
    private String token;

}
