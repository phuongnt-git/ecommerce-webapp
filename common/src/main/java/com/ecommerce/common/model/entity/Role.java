package com.ecommerce.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "ROLE",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"ROLE_NAME"}, name = "UQ_ROLE_NAME")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROLE_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ROLE_GEN")
    @TableGenerator(name = "ROLE_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "ROLE_SEQ_NEXT_VAL",
            allocationSize = 1)
    private Integer id;

    @Column(name = "ROLE_NAME", nullable = false, length = 64)
    private String name;

    @Column(name = "ROLE_DESCRIPTION", nullable = false, length = 512)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Role role = (Role) o;

        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
