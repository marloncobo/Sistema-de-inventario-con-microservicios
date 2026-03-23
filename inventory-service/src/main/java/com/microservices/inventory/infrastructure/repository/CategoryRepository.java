package com.microservices.inventory.infrastructure.repository;

import com.microservices.inventory.domain.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CategoryRepository extends ReactiveCrudRepository<Category, UUID> {
}
