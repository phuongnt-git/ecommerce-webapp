package com.ecommerce.common.model.entity;

import com.ecommerce.common.model.AbstractCountry;
import com.ecommerce.common.model.enums.AuthenticationType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "CUSTOMER",
        uniqueConstraints = {@UniqueConstraint(columnNames = "CUSTOMER_EMAIL", name = "UQ_CUSTOMER_EMAIL")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer extends AbstractCountry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMER_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CUSTOMER_GEN")
    @TableGenerator(name = "CUSTOMER_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL",
            allocationSize = 1)
    private Integer id;

    @Column(name = "CUSTOMER_EMAIL", nullable = false, length = 128)
    private String email;

    @Column(name = "CUSTOMER_PASSWORD", nullable = false, length = 64)
    private String password;

    @Column(name = "VERIFICATION_CODE", length = 64)
    private String verificationCode;

    @Column(name = "IS_ACTIVE", nullable = false, columnDefinition = "number(1, 0) default 1")
    private boolean enabled;

    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHENTICATION_TYPE", length = 16)
    private AuthenticationType authenticationType;

    @Column(name = "RESET_PASSWORD_TOKEN", length = 32)
    private String resetPasswordToken;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
