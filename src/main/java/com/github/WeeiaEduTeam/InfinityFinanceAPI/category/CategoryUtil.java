package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.dto.CategoryDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryUtil {

    private final Util mainUtil;

    public CategoryDTO mapCategoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .name(category.getName())
                .build();
    }

    public boolean isNumberPositive(int value) {
        return mainUtil.isPositive(value);
    }
}
