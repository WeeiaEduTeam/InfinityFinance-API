package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionTestHelper extends TransactionNameHolder{
    protected Transaction transactionTest;
    protected TransactionDTO transactionDTOTest;
    protected AppUser appUserTest;
    protected Role roleTest;
    protected Category categoryTest;
    protected CreateTransactionDTO createTransactionDTOTest;
    protected Transaction transactionNullCategoryAndUserTest;

    @BeforeEach
    void init() {
        roleTest = Role.builder()
                .id(TEST_ID)
                .name(TEST_ROLENAME)
                .build();

        appUserTest = AppUser.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .firstName(TEST_FIRSTNAME)
                .secondName(TEST_SECONDNAME)
                .username(TEST_USERNAME)
                .password(TEST_PLAINTEXT_PASSWORD)
                .roles(Collections.singletonList(roleTest))
                .build();

        categoryTest = Category.builder()
                .id(TEST_ID)
                .name(TEST_CATEGORY_NAME)
                .build();

        transactionTest = Transaction.builder()
                .id(TEST_ID)
                .transactionType(TEST_TRANSACTION_TYPE)
                .title(TEST_TITLE)
                .description(TEST_DESCRIPTION)
                .category(categoryTest)
                .appuser(appUserTest)
                .value(TEST_VALUE)
                .quantity(TEST_QUANTITY)
                .build();

        transactionDTOTest = TransactionDTO.builder()
                .id(TEST_ID)
                .transactionType(TEST_TRANSACTION_TYPE)
                .title(TEST_TITLE)
                .description(TEST_DESCRIPTION)
                .value(TEST_VALUE)
                .quantity(TEST_QUANTITY)
                .categoryName(TEST_CATEGORY_NAME)
                .build();

        createTransactionDTOTest = CreateTransactionDTO.builder()
                .transactionType(TEST_TRANSACTION_TYPE)
                .title(TEST_TITLE)
                .description(TEST_DESCRIPTION)
                .value(TEST_VALUE)
                .quantity(TEST_QUANTITY)
                .categoryName(TEST_CATEGORY_NAME)
                .build();

        transactionNullCategoryAndUserTest = Transaction.builder()
                .appuser(null)
                .category(null)
                .build();
    }
}
