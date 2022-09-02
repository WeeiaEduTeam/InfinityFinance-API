package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.util.Util.isPositive;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> getCategoryByName(String categoryName) {

        return Optional.ofNullable(categoryRepository.findByName(categoryName));
    }

    public Category createCategory(Category category) {

        return categoryRepository.save(category);
    }

    public void deleteCategoryIfNotRelated(Long id) {
        var foundCategory = categoryRepository.findById(id);

        foundCategory.ifPresent((category) -> {
            if (!isPositive(category.getTransactions().size()))
                deleteCategoryById(category.getId());
        });
    }

    private void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
