package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class CategoryUtil {

    public CategoryDTO mapToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .name(category.getName())
                .build();
    }

    public boolean isNumberPositive(int value) {
        return value > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}
