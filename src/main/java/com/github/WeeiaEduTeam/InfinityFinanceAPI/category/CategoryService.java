package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryUtil categoryUtil;

    public Category getCategoryByName(String categoryName) {

        return categoryRepository.findByName(categoryName);
    }

    public Category createCategory(String categoryName) {
        Category category = new Category(categoryName);

        return categoryRepository.save(category);
    }

    public void checkAndDeleteCategoryIfNotRelated(Long id) {
        var foundCategory = getCategoryById(id);

        deleteCategoryIfNotRelated(foundCategory);
    }

    private void deleteCategoryIfNotRelated(Category foundCategory) {
        if (!isNumberPositive(foundCategory.getTransactions().size()))
            deleteCategoryById(foundCategory.getId());
    }

    private Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found in database"));
    }

    private boolean isNumberPositive(int value) {
        return categoryUtil.isNumberPositive(value);
    }

    private void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
