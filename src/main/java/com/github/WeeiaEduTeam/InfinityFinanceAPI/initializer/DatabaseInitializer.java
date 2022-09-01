package com.github.WeeiaEduTeam.InfinityFinanceAPI.initializer;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserRepository;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryRepository;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.Transaction;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionRepository;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleRepository;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final RoleRepository roleRepository;

    private final List<AppUser> appUserList;
    private final List<Transaction> transactionList;
    private final List<Category> categoryList;
    private final List<Role> roleList;

    @Override
    public void run(String... args) {
        createRolesEntity();
        createAppUserEntity();
        createCategoryList();
        createTransactionList();


        appUserRepository.saveAll(appUserList);
        categoryRepository.saveAll(categoryList);
        transactionRepository.saveAll(transactionList);
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
                            .username("admin")
                            .email("patryk@wp.pl")
                            .password("{noop}admin")
                            .firstName("Patryk")
                            .secondName("Yes")
                            .roles(Collections.singletonList(roleList.get(0)))
                            .build(),
                    AppUser.builder()
                            .username("user")
                            .email("superaancko@o2.pl")
                            .password("{noop}user")
                            .firstName("Geraldo")
                            .secondName("Ciro")
                            .roles(Collections.singletonList(roleList.get(1)))
                            .build(),
                        AppUser.builder()
                                .username("test")
                                .email("superaancko@o2.pl")
                                .password("{bcrypt}" + new BCryptPasswordEncoder().encode("test"))
                                .firstName("Oki")
                                .secondName("Ok")
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

    private void createTransactionList() {
        transactionList.addAll(
                Arrays.asList(
                    Transaction.builder()
                            .transactionType(TransactionType.INCOME)
                            .title("Super income")
                            .description("myDescription")
                            .value(100)
                            .quantity(1)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.INCOME)
                            .title("waste of money")
                            .description("need more money")
                            .value(666)
                            .quantity(2)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.INCOME)
                            .title("idk")
                            .description("aha")
                            .value(123)
                            .quantity(10)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(0))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.INCOME)
                            .title("haha")
                            .description("aaaaa")
                            .value(1020)
                            .quantity(1)
                            .category(categoryList.get(4))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.OUTCOME)
                            .title("what")
                            .description("is this")
                            .value(100)
                            .quantity(1)
                            .category(categoryList.get(4))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.OUTCOME)
                            .title("ok")
                            .description("no")
                            .value(1000)
                            .quantity(1)
                            .category(categoryList.get(3))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.OUTCOME)
                            .title("oki")
                            .description("is this")
                            .value(1001)
                            .quantity(11)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.OUTCOME)
                            .title("ok")
                            .description("is ok")
                            .value(10110)
                            .quantity(1)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.OUTCOME)
                            .title("kosi")
                            .description("isok")
                            .value(100)
                            .quantity(1)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .transactionType(TransactionType.OUTCOME)
                            .title("ero")
                            .description("ero")
                            .value(100)
                            .quantity(1)
                            .category(categoryList.get(2))
                            .appuser(appUserList.get(1))
                            .build()
                )
        );
    }
}
