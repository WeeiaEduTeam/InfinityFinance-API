package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserAdminService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.util.CustomPageable;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

class TransactionAdminServiceTest extends TransactionTestHelper{

    @InjectMocks
    private TransactionAdminService transactionAdminService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AppUserAdminService appUserAdminService;

    @Mock
    private TransactionUtil transactionUtil;

    @Mock
    private CustomPageable customPageable;



    @Test
    @DisplayName("Should delete transaction and related category with no relations.")
    void shouldDeleteTransactionByIdAndCategory() {
        // given
        given(transactionRepository.findById(anyLong())).willReturn(Optional.ofNullable(transactionTest));

        // when
        transactionAdminService.deleteOneTransaction(transactionTest.getId());

        // then
        verify(transactionRepository).delete(transactionTest);
        verify(categoryService).checkAndDeleteCategoryIfNotRelated(transactionTest.getCategory().getId());
    }

    @Test
    @DisplayName("Should create transaction for given user with known category.")
    void shouldCreateTransactionForGivenUserWithKnownCategory() {
        //given
        given(transactionUtil.mapToTransaction(any(CreateTransactionDTO.class))).willReturn(transactionTest);
        given(transactionUtil.mapToTransactionDTO(any(Transaction.class))).willReturn(transactionDTOTest);
        given(appUserAdminService.getUserById(anyLong())).willReturn(appUserTest);
        given(transactionRepository.save(any(Transaction.class))).willReturn(transactionTest);

        //when
        var savedTransaction = transactionAdminService.createTransactionForGivenUser(appUserTest.getId(), createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TEST_TRANSACTION_TYPE)));
        assertThat(savedTransaction, hasProperty("value", equalTo(TEST_VALUE)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(TEST_QUANTITY)));
        assertThat(savedTransaction, hasProperty("categoryName", equalTo(TEST_CATEGORY_NAME)));
    }

    @Test
    @DisplayName("Should save transaction for given user and found category.")
    void shouldCreateTransactionForGivenUser() {
        //given
        given(transactionUtil.mapToTransaction(any(CreateTransactionDTO.class))).willReturn(transactionTest);
        given(transactionUtil.mapToTransactionDTO(any(Transaction.class))).willReturn(transactionDTOTest);
        given(appUserAdminService.getUserById(anyLong())).willReturn(appUserTest);
        given(transactionRepository.save(any(Transaction.class))).willReturn(transactionTest);

        //when
        var savedTransaction = transactionAdminService.createTransactionForGivenUser(appUserTest.getId(), createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TEST_TRANSACTION_TYPE)));
        assertThat(savedTransaction, hasProperty("value", equalTo(TEST_VALUE)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(TEST_QUANTITY)));
        assertThat(savedTransaction, hasProperty("categoryName", equalTo(TEST_CATEGORY_NAME)));
    }

    @Test
    @DisplayName("Should replace transaction content.")
    void shouldReplaceTransactionForGivenUser() {
        //given
        given(transactionUtil.overwriteTransactionByCreateTransactionDTO(any(Transaction.class), any(CreateTransactionDTO.class))).willReturn(transactionTest);
        given(transactionUtil.mapToTransactionDTO(any(Transaction.class))).willReturn(transactionDTOTest);
        given(transactionRepository.findByIdAndAppuserId(anyLong(), anyLong())).willReturn(transactionTest);

        //when
        var savedTransaction = transactionAdminService.replaceTransaction(transactionTest.getAppuser().getId(), transactionTest.getId(), createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TEST_TRANSACTION_TYPE)));
        assertThat(savedTransaction, hasProperty("value", equalTo(TEST_VALUE)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(TEST_QUANTITY)));
        assertThat(savedTransaction, hasProperty("description", equalTo(TEST_DESCRIPTION)));
        assertThat(savedTransaction, hasProperty("title", equalTo(TEST_TITLE)));
        assertThat(savedTransaction, hasProperty("categoryName", equalTo(TEST_CATEGORY_NAME)));
    }


    @Test
    @DisplayName("Should get all transactions for given user and category.")
    void shouldGetAllTransactionsForGivenUserAndCategory() {
        //given
        given(customPageable.validateAndCreatePageable(anyInt(), any(Sort.Direction.class), anyString(), ArgumentMatchers.<Class<A>>any())).willReturn(PageRequest.of(1,1));
        given(transactionUtil.mapToTransactionDTO(any(Transaction.class))).willReturn(transactionDTOTest);
        given(transactionRepository.findAllByAppuserIdAndCategoryId(anyLong(), anyLong(), any(Pageable.class))).willReturn(Collections.singletonList(transactionTest));

        //when
        var transactions = transactionAdminService.getAllTransactionsForGivenUserAndCategory(appUserTest.getId(), categoryTest.getId(), 0, Sort.Direction.valueOf("ASC"),"id");

        //then
        assertEquals(1, transactions.size());

        var firstTransaction = transactions.get(0);
        assertThat(firstTransaction, instanceOf(TransactionDTO.class));
        assertThat(firstTransaction, hasProperty("transactionType", equalTo(TEST_TRANSACTION_TYPE)));
        assertThat(firstTransaction, hasProperty("value", equalTo(TEST_VALUE)));
        assertThat(firstTransaction, hasProperty("quantity", equalTo(TEST_QUANTITY)));
        assertThat(firstTransaction, hasProperty("categoryName", equalTo(TEST_CATEGORY_NAME)));
    }

    @Test
    @DisplayName("Should get all transactions for given user.")
    void shouldGetAllTransactionsForGivenUser() {
        //given
        given(transactionUtil.mapToTransactionDTO(any(Transaction.class))).willReturn(transactionDTOTest);
        given(customPageable.validateAndCreatePageable(anyInt(), any(Sort.Direction.class), anyString(), ArgumentMatchers.<Class<A>>any())).willReturn(PageRequest.of(1,1));
        given(transactionRepository.findAllByAppuserId(anyLong(), any(Pageable.class))).willReturn(Collections.singletonList(transactionTest));

        //when
        var transactions = transactionAdminService.getAllTransactionsForGivenUser(appUserTest.getId(), 1, Sort.Direction.valueOf("ASC"), "id");

        //then
        assertEquals(1, transactions.size());

        var firstTransaction = transactions.get(0);
        assertThat(firstTransaction, instanceOf(TransactionDTO.class));
        assertThat(firstTransaction, hasProperty("transactionType", equalTo(TEST_TRANSACTION_TYPE)));
        assertThat(firstTransaction, hasProperty("value", equalTo(TEST_VALUE)));
        assertThat(firstTransaction, hasProperty("quantity", equalTo(TEST_QUANTITY)));
        assertThat(firstTransaction, hasProperty("categoryName", equalTo(TEST_CATEGORY_NAME)));
    }

    @Test
    @DisplayName("Should create category during create transaction.")
    void shouldThrowExceptionWhenUnknownCategory() {
        //given
        transactionNullCategoryAndUserTest.setAppuser(appUserTest);

        given(transactionUtil.mapToTransactionDTO(any(Transaction.class))).willReturn(transactionDTOTest);
        given(categoryService.createCategory(anyString())).willReturn(categoryTest);
        given(transactionRepository.save(any(Transaction.class))).willReturn(transactionTest);
        given(appUserAdminService.getUserById(anyLong())).willReturn(appUserTest);
        given(transactionUtil.mapToTransaction(any(CreateTransactionDTO.class)))
                .willReturn(transactionNullCategoryAndUserTest);

        //when
        var transaction = transactionAdminService.createTransactionForGivenUser(TEST_ID, createTransactionDTOTest);

        assertThat(transaction, instanceOf(TransactionDTO.class));
        assertThat(transaction, hasProperty("categoryName", equalTo(TEST_CATEGORY_NAME)));
    }
}

