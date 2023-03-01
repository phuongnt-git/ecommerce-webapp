package com.ecommerce.common.model.entity;

import com.ecommerce.common.annotation.FieldsValueMatch;
import com.ecommerce.common.annotation.PasswordValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "USERS",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_EMAIL"}, name = "UQ_USER_EMAIL")})
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "User password do not match"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"photos", "password"})
@Builder(toBuilder = true)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "USER_GEN")
    @TableGenerator(name = "USER_GEN",
            table = "SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "USER_SEQ_NEXT_VAL",
            allocationSize = 1)
    private Integer id;

    @Column(name = "USER_EMAIL", nullable = false, length = 128)
    @NotBlank(message = "User email is required")
    @Email(message = "User email must be a valid email address")
    private String email;

    @Column(name = "USER_PASSWORD", nullable = false, length = 64)
    @NotBlank(message = "User password is required")
    @Size(min = 6, max = 64, message = "User password must be between 6 and 64 characters")
    @PasswordValidator
    @JsonIgnore
    private String password;

    @Transient
    @JsonIgnore
    @NotBlank(message = "Confirm password is required")
    @Size(min = 6, max = 64, message = "Confirm password must be between 6 and 64 characters")
    private String confirmPassword;

    @Column(name = "FIRST_NAME", nullable = false, length = 64)
    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 64, message = "First name must be between 3 and 64 characters")
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 64)
    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 64, message = "Last name must be between 3 and 64 characters")
    private String lastName;

    @Transient
    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    @Column(name = "USER_PHOTO", length = 64)
    private String photos;

    @Column(name = "IS_ACTIVE", nullable = false, columnDefinition = "NUMBER(1, 0) DEFAULT 1")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", nullable = false, referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", nullable = false, referencedColumnName = "ROLE_ID"),
            foreignKey = @ForeignKey(name = "FK_USER_ROLE_USER"),
            inverseForeignKey = @ForeignKey(name = "FK_USER_ROLE_ROLE"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"}, name = "UQ_USER_ROLE")})
    private Set<Role> roles = new HashSet<>();

    @Column(name = "ACCOUNT_NON_EXPIRED", nullable = false, insertable = false, columnDefinition = "NUMBER(1, 0) DEFAULT 1")
    private boolean accountNonExpired;

    @Column(name = "ACCOUNT_NON_LOCKED", nullable = false, insertable = false, columnDefinition = "NUMBER(1, 0) DEFAULT 1")
    private boolean accountNonLocked;

    @Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false, insertable = false, columnDefinition = "NUMBER(1, 0) DEFAULT 1")
    private boolean credentialsNonExpired;

    @Column(name = "FAILED_ATTEMPT", nullable = false, insertable = false, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private int failedAttempt;

    @Column(name = "LAST_MODIFIED")
    private Date lastModified;

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean hasRole(String roleName) {
        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public String getPhotosPath() {
        if (id == null || photos == null) {
            return "/images/default-user.png";
        }
        return "/images/user-photos/" + this.id + "/" + this.photos;
    }

}