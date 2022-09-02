package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.dto.CategoryDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.Transaction;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryUtils {
    public CategoryDTO mapCategoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .name(category.getName())
                .build();
    }
}
