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

    private final CategoryUtil categoryUtil;

    public Optional<Category> getCategoryByName(String categoryName) {

        return Optional.ofNullable(categoryRepository.findByName(categoryName));
    }

    public Category createCategory(Category category) {

        return categoryRepository.save(category);
    }

    public void deleteCategoryIfNotRelated(Long id) {
        var foundCategory = categoryRepository.findById(id);

        foundCategory.ifPresent((category) -> {
            if (!isNumberPositive(category.getTransactions().size()))
                deleteCategoryById(category.getId());
        });
    }

    private boolean isNumberPositive(int value) {
        return categoryUtil.isNumberPositive(value);
    }

    private void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
