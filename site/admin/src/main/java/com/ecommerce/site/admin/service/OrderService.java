package com.ecommerce.site.admin.service;

import com.ecommerce.common.exception.OrderNotFoundException;
import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.Order;
import com.ecommerce.common.model.entity.OrderTrack;
import com.ecommerce.common.model.enums.OrderStatus;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.repository.CountryRepository;
import com.ecommerce.site.admin.repository.OrderRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ecommerce.site.admin.constant.ApplicationConstant.ORDERS_PER_PAGE;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CountryRepository countryRepository;

    public void listByPage(int pageNum, @NotNull PagingAndSortingHelper helper) {
        String sortField = helper.getSortField();
        String sortDir = helper.getSortDir();
        String keyword = helper.getKeyword();

        Sort sort;

        if ("destination".equals(sortField)) {
            sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
        } else {
            sort = Sort.by(sortField);
        }

        sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        Page<Order> page;

        if (keyword != null) {
            page = orderRepository.findAll(keyword, pageable);
        } else {
            page = orderRepository.findAll(pageable);
        }

        helper.updateModelAttributes(pageNum, page);
    }

    public Order get(Integer id) throws OrderNotFoundException {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new OrderNotFoundException(String.format("Could not find any orders with ID %s", id));
        }
    }

    public void delete(Integer id) throws OrderNotFoundException {
        Long count = orderRepository.countById(id);
        if (count == null || count == 0) {
            throw new OrderNotFoundException(String.format("Could not find any orders with ID %s", id));
        }

        orderRepository.deleteById(id);
    }

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public void save(@NotNull Order orderInForm) {
        Order orderInDB = orderRepository.findById(orderInForm.getId()).get();
        orderInForm.setOrderTime(orderInDB.getOrderTime());
        orderInForm.setCustomer(orderInDB.getCustomer());

        orderRepository.save(orderInForm);
    }

}
