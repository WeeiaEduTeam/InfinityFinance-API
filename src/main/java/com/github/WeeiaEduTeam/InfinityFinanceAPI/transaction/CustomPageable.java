package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
class CustomPageable {

    private static final int PAGE_SIZE = 2;

    <T> Pageable validateAndCreatePageable(int pageNumber, Sort.Direction sortDirection, String sortBy, Class<T> clazz) {
        pageNumber = validateAndReturnPageNumber(pageNumber);

        sortBy = validateAndReturnSortString(sortBy, clazz);

        return PageRequest.of(pageNumber, PAGE_SIZE,
                Sort.by(sortDirection, sortBy)
        );
    }

    private <T> String validateAndReturnSortString(String sortBy, Class<T> clazz) {
        String sortByToLower = sortBy.toLowerCase();

        var output = Arrays.stream(clazz.getDeclaredFields())
                .map(e -> e.getName().toLowerCase())
                .filter(item -> item.equals(sortByToLower))
                .findAny();

        if(output.isEmpty())
            return "id";

        return output.get();
    }

    private int validateAndReturnPageNumber(int number) {
        return Math.max(number, 0);
    }
}
