package com.ecommerce.site.shop.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ecommerce.common.model.entity.State} entity
 */
public record StateDto(Integer id, String name) implements Serializable {
}