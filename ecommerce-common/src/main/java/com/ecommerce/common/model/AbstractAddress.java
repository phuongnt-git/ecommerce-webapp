package com.ecommerce.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nguyen Thanh Phuong
 */
@MappedSuperclass
@Getter
@Setter
public class AbstractAddress {

    @Column(name = "FIRST_NAME", nullable = false, length = 64)
    protected String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 64)
    protected String lastName;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 16)
    protected String phoneNumber;

    @Column(name = "ADDRESS_LINE_1", nullable = false, length = 128)
    protected String addressLine1;

    @Column(name = "ADDRESS_LINE_2", length = 128)
    protected String addressLine2;

    @Column(name = "CITY", nullable = false, length = 64)
    protected String city;

    @Column(name = "STATE", nullable = false, length = 64)
    protected String state;

    @Column(name = "POSTAL_CODE", nullable = false, length = 16)
    protected String postalCode;

}
