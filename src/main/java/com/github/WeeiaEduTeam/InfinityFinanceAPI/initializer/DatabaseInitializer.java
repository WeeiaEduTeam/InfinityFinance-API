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
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    private void createTransactionList() {
        transactionList.addAll(
                Arrays.asList(
                    Transaction.builder()
                            .value(100)
                            .quantity(1)
                            .transactionType(TransactionType.INCOME)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Transaction.builder()
                            .value(666)
                            .quantity(2)
                            .transactionType(TransactionType.INCOME)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .value(123)
                            .quantity(10)
                            .transactionType(TransactionType.INCOME)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(0))
                            .build(),
                    Transaction.builder()
                            .value(1020)
                            .quantity(1)
                            .transactionType(TransactionType.INCOME)
                            .category(categoryList.get(4))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .value(100)
                            .quantity(1)
                            .transactionType(TransactionType.OUTCOME)
                            .category(categoryList.get(4))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .value(1000)
                            .quantity(1)
                            .transactionType(TransactionType.OUTCOME)
                            .category(categoryList.get(3))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .value(1001)
                            .quantity(11)
                            .transactionType(TransactionType.OUTCOME)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Transaction.builder()
                            .value(10110)
                            .quantity(1)
                            .transactionType(TransactionType.OUTCOME)
                            .category(categoryList.get(0))
                            .appuser(appUserList.get(0))
                            .build(),
                    Transaction.builder()
                            .value(100)
                            .quantity(1)
                            .transactionType(TransactionType.OUTCOME)
                            .category(categoryList.get(1))
                            .appuser(appUserList.get(1))
                            .build(),
                    Transaction.builder()
                            .value(100)
                            .quantity(1)
                            .transactionType(TransactionType.INCOME)
                            .category(categoryList.get(2))
                            .appuser(appUserList.get(1))
                            .build()
                )
        );
    }
}
