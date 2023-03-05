package com.ecommerce.site.admin.repository;

import com.ecommerce.common.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface UserRepository extends CustomPagingAndSortingRepository<User, Integer>, JpaRepository<User, Integer>  {

    User findByEmail(String email);

    Long countById(Integer id);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);

    @Query("UPDATE User u SET u.failedAttempt = u.failedAttempt + 1, u.lastModified = ?2 WHERE u.email = ?1")
    @Modifying
    void updateFailedAttempts(String email, Date lastModified);

    @Query("UPDATE User AS u SET u.failedAttempt = 0, u.lastModified = NULL WHERE u.email = ?1")
    @Modifying
    void resetFailedAttempts(String email);

}