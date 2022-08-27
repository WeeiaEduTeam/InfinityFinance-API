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
    @Autowired
    private final CategoryUtils categoryUtils;

    public Optional<Category> getCategoryByName(String categoryName) {

        return Optional.ofNullable(categoryRepository.findByName(categoryName));
    }

    public CategoryDTO createCategory(Category category) {
        var savedCategory = categoryRepository.save(category);

        return categoryUtils.mapCategoryToCategoryDTO(savedCategory);
    }
}
