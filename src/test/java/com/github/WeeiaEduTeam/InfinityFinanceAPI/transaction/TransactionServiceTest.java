package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    Transaction transactionTest;
    AppUser appUserTest;
    Role roleTest;
    Category categoryTest;

    @BeforeEach
    void init() {
        roleTest = Role.builder()
                .id(1L)
                .name("TEST_ROLE")
                .build();

        appUserTest = AppUser.builder()
                .id(1L)
                .email("testemail@wp.pl")
                .firstName("John")
                .secondName("Smith")
                .username("smith123")
                .password("123")
                .roles(Collections.singletonList(roleTest))
                .build();

        categoryTest = Category.builder()
                .id(1L)
                .name("testCategory")
                .build();

        transactionTest = Transaction.builder()
                .id(1L)
                .transactionType(TransactionType.INCOME)
                .category(categoryTest)
                .appuser(appUserTest)
                .value(600)
                .quantity(2)
                .build();
    }

    @Test
    void getAllTransactionsForGivenUserAndCategory() {
        //given
        when(transactionRepository.findAllByAppuserIdAndCategoryId(appUserTest.getId(), categoryTest.getId())).thenReturn(Collections.singletonList(transactionTest));

        //when
        var transactions = transactionService.getAllTransactionsForGivenUserAndCategory(appUserTest.getId(), categoryTest.getId());

        //then
        assertEquals(transactions.size(), 1);

        var firstTransaction = transactions.get(0);
        assertThat(firstTransaction, instanceOf(TransactionDTO.class));
        assertThat(firstTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(firstTransaction, hasProperty("value", equalTo(600)));
        assertThat(firstTransaction, hasProperty("quantity", equalTo(2)));
        //category etc
    }

    @Test
    void getAllTransactionsForGivenUser() {
        //given
        when(transactionRepository.findAllByAppuserId(appUserTest.getId())).thenReturn(Collections.singletonList(transactionTest));

        //when
        var transactions = transactionService.getAllTransactionsForGivenUser(appUserTest.getId());

        //then
        assertEquals(transactions.size(), 1);

        var firstTransaction = transactions.get(0);
        assertThat(firstTransaction, instanceOf(TransactionDTO.class));
        assertThat(firstTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(firstTransaction, hasProperty("value", equalTo(600)));
        assertThat(firstTransaction, hasProperty("quantity", equalTo(2)));
        //category etc

    }
}
