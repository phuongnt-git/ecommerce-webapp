package com.ecommerce.site.admin.repository;

import com.ecommerce.common.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}