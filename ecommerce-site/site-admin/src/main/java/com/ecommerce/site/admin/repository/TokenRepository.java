package com.ecommerce.site.admin.repository;

import com.ecommerce.common.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByIdentifier(String identifier);

}