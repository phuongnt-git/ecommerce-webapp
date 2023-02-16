package com.ecommerce.common.model;

import com.ecommerce.common.model.entity.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nguyen Thanh Phuong
 */
@MappedSuperclass
@Getter
@Setter
public class AbstractCountry extends AbstractAddress {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class)
    @JoinColumn(name = "COUNTRY_ID" ,foreignKey = @ForeignKey(name = "FK_CUSTOMER_COUNTRY"))
    protected Country country;

    @Override
    public String toString() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) {
            address += " " + lastName;
        }

        if (!addressLine1.isEmpty()) {
            address += ", " + addressLine1;
        }

        if (addressLine2 != null && !addressLine2.isEmpty()) {
            address += ", " + addressLine2;
        }

        if (!city.isEmpty()) {
            address += ", " + city;
        }

        if (state != null && !state.isEmpty()) {
            address += ", " + state;
        }

        address += ", " + country.getName();

        if (!postalCode.isEmpty()) {
            address += ". Postal Code: " + postalCode;
        }
        if (!phoneNumber.isEmpty()) {
            address += ". Phone Number: " + phoneNumber;
        }

        return address;
    }

}
