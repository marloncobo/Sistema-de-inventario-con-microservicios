package com.microservices.inventory.application.service;

import com.microservices.inventory.domain.Category;
import com.microservices.inventory.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Flux<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
