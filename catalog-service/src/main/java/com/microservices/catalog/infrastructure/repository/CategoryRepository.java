package com.microservices.catalog.infrastructure.repository;

import com.microservices.catalog.domain.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CategoryRepository extends ReactiveCrudRepository<Category, UUID> {
}
