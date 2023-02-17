package com.ecommerce.site.admin.helper;

import com.ecommerce.site.admin.repository.PagingAndSortingCustomRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
public class PagingAndSortingHelper {

    private final ModelAndViewContainer container;

    private final String listName;

    private final String sortField;

    private final String sortDir;

    private final String keyword;

    public PagingAndSortingHelper(ModelAndViewContainer container, String listName, String sortField, String sortDir, String keyword) {
        this.container = container;
        this.listName = listName;
        this.sortField = sortField;
        this.sortDir = sortDir;
        this.keyword = keyword;
    }

    public void updateModelAttributes(int pageNumber, @NotNull Page<?> page) {
        List<?> listItems = page.getContent();
        int pageSize = page.getSize();

        long startPage = (long) (pageNumber - 1) * pageSize + 1;
        long endPage = startPage + pageSize - 1;
        if (endPage > page.getTotalElements()) {
            endPage = page.getTotalElements();
        }

        container.addAttribute("startPage", startPage);
        container.addAttribute("endPage", endPage);
        container.addAttribute("currentPage", pageNumber);
        container.addAttribute("totalItems", page.getTotalElements());
        container.addAttribute("totalPages", page.getTotalPages());
        container.addAttribute(listName, listItems);
    }

    public void listEntities(int pageNumber, int pageSize, PagingAndSortingCustomRepository<?, Integer> repository) {
        Pageable pageable = createPageable(pageSize, pageNumber);
        Page<?> page;

        if (keyword != null) {
            page = repository.findAll(keyword, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        updateModelAttributes(pageNumber, page);
    }

    public Pageable createPageable(int pageSize, int pageNumber) {
        Sort sort = Sort.by(sortField);
        sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }

    public String getSortField() {
        return sortField;
    }

    public String getSortDir() {
        return sortDir;
    }

    public String getKeyword() {
        return keyword;
    }

}
