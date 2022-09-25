package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/catgories")
    public ResponseEntity<List<Category>> getAllCategories(){

        var categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }
}
