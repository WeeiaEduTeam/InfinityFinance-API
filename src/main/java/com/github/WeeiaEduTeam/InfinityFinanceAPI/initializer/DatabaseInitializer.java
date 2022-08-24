package com.github.WeeiaEduTeam.InfinityFinanceAPI.initializer;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserRepository;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryRepository;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.ledger.Ledger;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.ledger.LedgerRepository;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final LedgerRepository ledgerRepository;
    private final RoleRepository roleRepository;

    private final List<AppUser> appUserList;
    private final List<Ledger> ledgerList;
    private final List<Category> categoryList;
    private final List<Role> roleList;

    @Override
    public void run(String... args) {
        createRolesEntity();
        createAppUserEntity();
        createCategoryList();
        createLedgerList();


        appUserRepository.saveAll(appUserList);
        categoryRepository.saveAll(categoryList);
        ledgerRepository.saveAll(ledgerList);
        roleRepository.saveAll(roleList);
    }

    private void createRolesEntity() {
        roleList.addAll(
                Arrays.asList(
                        Role.builder()
                                .name("ROLE_ADMIN")
                                .build(),
                        Role.builder()
                                .name("ROLE_USER")
                                .build()
                )
        );
    }

    private void createAppUserEntity() {
        appUserList.addAll(
                Arrays.asList(
                    AppUser.builder()
                            .username("pasa")
                            .email("patryk@wp.pl")
                            .password("need to bcrypt this passwd")
                            .firstName("Patryk")
                            .secondName("Yes")
                            .roles(Arrays.asList(roleList.get(0)))
                            .build(),
                    AppUser.builder()
                            .username("geralt123")
                            .email("superaancko@o2.pl")
                            .password("need to bcrypt this passwd2")
                            .firstName("Geraldo")
                            .secondName("Ciro")
                            .roles(roleList)
                            .build()
                )
        );
    }

    private void createCategoryList() {
        categoryList.addAll(
                Arrays.asList(
                    Category.builder()
                            .name("shopping")
                            .build(),
                    Category.builder()
                            .name("fuel")
                            .build(),
                    Category.builder()
                            .name("toys")
                            .build(),
                    Category.builder()
                            .name("drugs")
                            .build(),
                    Category.builder()
                            .name("kitchen ideas")
                            .build()
                )
        );
    }

    private void createLedgerList() {
        ledgerList.addAll(
                Arrays.asList(
                    Ledger.builder()
                            .value(100)
                            .quantity(1)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Ledger.builder()
                            .value(666)
                            .quantity(2)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(1))
                            .build(),
                    Ledger.builder()
                            .value(123)
                            .quantity(10)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(0))
                            .build(),
                    Ledger.builder()
                            .value(1020)
                            .quantity(1)
                            .category(categoryList.get(5))
                            .appuser(appUserList.get(1))
                            .build()
                    /*Ledger.builder()
                            .value(100)
                            .quantity(1)
                            .category(categoryList.get(4))
                            .appuser(appUserList.get(1))
                            .build(),
                    Ledger.builder()
                            .value(1000)
                            .quantity(1)
                            .category(categoryList.get(3))
                            .appuser(appUserList.get(1))
                            .build(),
                    Ledger.builder()
                            .value(1001)
                            .quantity(11)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Ledger.builder()
                            .value(10110)
                            .quantity(1)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Ledger.builder()
                            .value(100)
                            .quantity(1)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(1))
                            .build(),
                    Ledger.builder()
                            .value(100)
                            .quantity(1)
                            .category(categoryList.get(2))
                            .appuser(appUserList.get(2))
                            .build()*/
                )
        );
    }
}
