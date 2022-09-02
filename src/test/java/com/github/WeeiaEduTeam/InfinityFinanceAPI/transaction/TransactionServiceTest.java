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
import static org.mockito.ArgumentMatchers.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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
    TransactionDTO transactionDTOTest;
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
                .name("name")
                .build();

        transactionTest = Transaction.builder()
                .id(1L)
                .transactionType(TransactionType.INCOME)
                .title("title")
                .description("desc")
                .category(categoryTest)
                .appuser(appUserTest)
                .value(600)
                .quantity(2)
                .build();

        transactionDTOTest = TransactionDTO.builder()
                .id(1L)
                .transactionType(TransactionType.INCOME)
                .title("title")
                .description("desc")
                .userName("name")
                .value(600)
                .quantity(2)
                .categoryName("name")
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

    @Test
    @DisplayName("Should delete transaction and related category with no relations.")
    void shouldDeleteTransactionByIdAndCategory() {
        // given
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.ofNullable(transactionTest));

        // when
        transactionService.deleteOneTransaction(transactionTest.getId());

        // then
        verify(transactionRepository).delete(transactionTest);
        verify(categoryService).deleteCategoryIfNotRelated(transactionTest.getCategory().getId());
    }

    @Test
    @DisplayName("Should create transaction for given user with unknown category.")
    void shouldCreateTransactionForGivenUserWithUnknownCategory() {
        //given
        when(transactionUtil.createTransactionFromCreateTransactionDTOAndUserId(any(CreateTransactionDTO.class), eq(1L))).thenReturn(transactionTest);
        when(transactionUtil.mapTransactionToTransactionDTO(any(Transaction.class))).thenReturn(transactionDTOTest);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionTest);

        //when
        var savedTransaction = transactionService.createTransactionForGivenUser(appUserTest.getId(), createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(savedTransaction, hasProperty("value", equalTo(600)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(2)));
        assertThat(savedTransaction, hasProperty("categoryName", equalTo("name")));
    }

    @Test
    @DisplayName("Should save transaction for given user and found category.")
    void shouldCreateTransactionForGivenUser() {
        //given
        when(transactionUtil.createTransactionFromCreateTransactionDTOAndUserId(any(CreateTransactionDTO.class), eq(1L))).thenReturn(transactionTest);
        when(transactionUtil.mapTransactionToTransactionDTO(any(Transaction.class))).thenReturn(transactionDTOTest);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionTest);

        //when
        var savedTransaction = transactionService.createTransactionForGivenUser(appUserTest.getId(), createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(savedTransaction, hasProperty("value", equalTo(600)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(2)));
        assertThat(savedTransaction, hasProperty("categoryName", equalTo("name")));
    }

    @Test
    @DisplayName("Should replace transaction content.")
    void shouldReplaceTransactionForGivenUser() {
        //given
        when(transactionUtil.overwriteTransactionByCreateTransactionDTO(any(Transaction.class), any(CreateTransactionDTO.class))).thenReturn(transactionTest);
        when(transactionUtil.mapTransactionToTransactionDTO(any(Transaction.class))).thenReturn(transactionDTOTest);
        when(transactionRepository.findByIdAndAppuserId(1L, 1L)).thenReturn(transactionTest);

        //when
        var savedTransaction = transactionService.replaceTransaction(transactionTest.getAppuser().getId(), transactionTest.getId(), createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(savedTransaction, hasProperty("value", equalTo(600)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(2)));
        assertThat(savedTransaction, hasProperty("description", equalTo("desc")));
        assertThat(savedTransaction, hasProperty("title", equalTo("title")));
        assertThat(savedTransaction, hasProperty("categoryName", equalTo("name")));
    }

    @Test
    @DisplayName("Should get all transactions for given user and category.")
    void shouldGetAllTransactionsForGivenUserAndCategory() {
        //given
        when(transactionUtil.mapTransactionToTransactionDTO(any(Transaction.class))).thenReturn(transactionDTOTest);
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
        assertThat(firstTransaction, hasProperty("categoryName", equalTo("name")));
    }

    @Test
    @DisplayName("Should get all transactions for given user.")
    void shouldGetAllTransactionsForGivenUser() {
        //given
        when(transactionUtil.mapTransactionToTransactionDTO(any(Transaction.class))).thenReturn(transactionDTOTest);
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
        assertThat(firstTransaction, hasProperty("categoryName", equalTo("name")));
    }
}
