package com.github.WeeiaEduTeam.InfinityFinanceAPI.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Configuration
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class CustomPageable {
    private final int PAGE_SIZE = 6;

    public <T> Pageable validateAndCreatePageable(int pageNumber, Sort.Direction sortDirection, String sortBy, Class<T> clazz) {
        pageNumber = validateAndReturnPageNumber(pageNumber);

        sortBy = validateAndReturnSortString(sortBy, clazz);

        return PageRequest.of(pageNumber, PAGE_SIZE,
                Sort.by(sortDirection, sortBy)
        );
    }

    private <T> String validateAndReturnSortString(String sortBy, Class<T> clazz) {

        Optional<String> output = checkIfStringExistsInClassValue(clazz, sortBy);

        if(notFoundValueInClass(output))
            return "id";

        return foundValue(output);
    }

    @NotNull
    private String foundValue(Optional<String> output) {
        return output.orElseGet(output::get);
    }

    @NotNull
    private <T> Optional<String> checkIfStringExistsInClassValue(Class<T> clazz, String sortBy) {
        String sortByToLower = sortBy.toLowerCase();

        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .map(String::toLowerCase)
                .filter(item -> item.equals(sortByToLower))
                .findAny();
    }

    private boolean notFoundValueInClass(Optional<String> output) {
        return output.isEmpty();
    }

    private int validateAndReturnPageNumber(int number) {
        return Math.max(number, 0);
    }
}
