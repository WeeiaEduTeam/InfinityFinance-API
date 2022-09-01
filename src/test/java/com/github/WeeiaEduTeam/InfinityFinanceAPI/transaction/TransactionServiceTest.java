package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryService categoryService;
    @Mock
    private TransactionUtil transactionUtil;

    Transaction transactionTest;
    AppUser appUserTest;
    Role roleTest;
    Category categoryTest;
    CreateTransactionDTO createTransactionDTOTest;

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
                .title("testTitle")
                .description("testDescription")
                .category(categoryTest)
                .appuser(appUserTest)
                .value(600)
                .quantity(2)
                .build();

        createTransactionDTOTest = CreateTransactionDTO.builder()
                .transactionType(transactionTest.getTransactionType())
                .title(transactionTest.getTitle())
                .description(transactionTest.getDescription())
                .value(transactionTest.getValue())
                .quantity(transactionTest.getQuantity())
                .categoryName(transactionTest.getCategory().getName())
                .build();
    }


    /*@Test
    @DisplayName("Should save transaction for given user and found category.")
    void createTransactionForGivenUser() {
        //given
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transactionTest);

        //when
        var savedTransaction = transactionService.createTransactionForGivenUser(appUserTest.getId(), createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(savedTransaction, hasProperty("value", equalTo(600)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(2)));
    }*/

    @Test
    @DisplayName("Should get all transactions for given user and category.")
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
    @DisplayName("Should get all transactions for given user.")
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
