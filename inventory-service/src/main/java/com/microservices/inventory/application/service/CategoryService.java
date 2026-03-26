package com.microservices.inventory.application.service;

import com.microservices.inventory.application.dto.CategorySyncRequest;
import com.microservices.inventory.domain.Category;
import com.microservices.inventory.infrastructure.repository.CategoryRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DatabaseClient databaseClient;

    public CategoryService(CategoryRepository categoryRepository, DatabaseClient databaseClient) {
        this.categoryRepository = categoryRepository;
        this.databaseClient = databaseClient;
    }

    public Flux<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Mono<Category> syncCategory(CategorySyncRequest request) {
        DatabaseClient.GenericExecuteSpec statement = databaseClient.sql("""
                        INSERT INTO categories (id, name, description)
                        VALUES (:id, :name, :description)
                        ON CONFLICT (id) DO UPDATE
                        SET name = EXCLUDED.name,
                            description = EXCLUDED.description
                        """)
                .bind("id", request.getId())
                .bind("name", request.getName());

        statement = request.getDescription() == null
                ? statement.bindNull("description", String.class)
                : statement.bind("description", request.getDescription());

        return statement.fetch()
                .rowsUpdated()
                .then(categoryRepository.findById(request.getId()));
    }
}
