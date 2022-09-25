package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;

import org.springframework.data.jpa.repository.JpaRepository;

interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String categoryName);
}
