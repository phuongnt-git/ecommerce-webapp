package com.ecommerce.common.model.dto;

import com.ecommerce.common.model.entity.State;

import java.io.Serializable;

/**
 * A DTO for the {@link State} entity
 */
public record StateDto(Integer id, String name) implements Serializable {
}