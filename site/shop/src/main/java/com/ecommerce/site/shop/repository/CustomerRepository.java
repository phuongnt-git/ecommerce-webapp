package com.ecommerce.site.shop.repository;

import com.ecommerce.common.model.entity.Customer;
import com.ecommerce.common.model.enums.AuthenticationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByVerificationCode(String verificationCode);

    @Query("UPDATE Customer c SET c.verificationCode = NULL, c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean b);

    @Modifying
    @Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
    void updateAuthenticationType(Integer customerId, AuthenticationType type);

    Optional<Customer> findByResetPasswordToken(String resetToken);

}
