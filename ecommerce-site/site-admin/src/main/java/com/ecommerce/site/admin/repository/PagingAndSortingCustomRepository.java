package com.ecommerce.site.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Nguyen Thanh Phuong
 */
@NoRepositoryBean
public interface PagingAndSortingCustomRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

    Page<T> findAll(String keyword, Pageable pageable);

}