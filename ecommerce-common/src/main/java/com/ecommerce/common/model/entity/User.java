package com.ecommerce.common.model.entity;

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

import static com.ecommerce.common.constant.ImagePath.USER_PHOTOS_DIR;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "USERS",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_EMAIL"}, name = "UQ_USER_EMAIL")})
/*@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Password do not match"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "Email do not match"
        )
})*/
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
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String email;

    /*@Transient
    @JsonIgnore
    @NotBlank(message = "Confirm email must not be blank")
    @Email(message = "Please provide a valid confirm email address" )
    private String confirmEmail;*/

    @Column(name = "USER_PASSWORD", nullable = false, length = 64)
    @NotBlank(message = "Password must not be blank")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    @PasswordValidator
    @JsonIgnore
    private String password;

    /*@Transient
    @JsonIgnore
    @NotBlank(message = "Confirm Password must not be blank")
    @Size(min = 5, message = "Confirm Password must be at least 5 characters long")
    private String confirmPassword;*/

    @Column(name = "FIRST_NAME", nullable = false, length = 64)
    @NotBlank(message = "User name must not be blank")
    @Size(min = 3, message = "User name must be at least 3 characters long")
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 64)
    @NotBlank(message = "User last name must not be blank")
    @Size(min = 3, message = "User last name must be at least 3 characters long")
    private String lastName;

    @Transient
    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    @Column(name = "USER_PHOTO", length = 64)
    private String photos;

    @Column(name = "IS_ACTIVE")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", nullable = false, referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", nullable = false, referencedColumnName = "ROLE_ID"),
            foreignKey = @ForeignKey(name = "FK_USER_ROLE_USER"),
            inverseForeignKey = @ForeignKey(name = "FK_USER_ROLE_ROLE"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"}, name = "UQ_USER_ROLE")})
    private Set<Role> roles = new HashSet<>();

    @Column(name = "ACCOUNT_NON_EXPIRED", nullable = false, insertable = false, columnDefinition = "number(1, 0) default 1")
    private boolean accountNonExpired;

    @Column(name = "ACCOUNT_NON_LOCKED", nullable = false, insertable = false, columnDefinition = "number(1, 0) default 1")
    private boolean accountNonLocked;

    @Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false, insertable = false, columnDefinition = "number(1, 0) default 1")
    private boolean credentialsNonExpired;

    @Column(name = "FAILED_ATTEMPT", nullable = false, insertable = false, columnDefinition = "number(1, 0) default 0")
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
            return USER_PHOTOS_DIR + "default-user.png";
        }
        return USER_PHOTOS_DIR + this.id + "/" + this.photos;
    }

}