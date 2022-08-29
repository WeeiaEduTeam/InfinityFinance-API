package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> getCategoryByName(String categoryName) {

        return Optional.ofNullable(categoryRepository.findByName(categoryName));
    }

    public Category createCategory(Category category) {
        var savedCategory = categoryRepository.save(category);

        return savedCategory;
    }
}
