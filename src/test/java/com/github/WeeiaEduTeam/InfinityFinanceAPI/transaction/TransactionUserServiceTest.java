package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionUserServiceTest {

    @InjectMocks
    private TransactionUserService transactionUserService;

    @Mock
    private TransactionAdminService transactionAdminService;

    @Mock
    private AppUserService appUserService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryService categoryService;

    Transaction transactionTest;
    TransactionDTO transactionDTOTest;
    AppUser appUserTest;
    Role roleTest;
    Category categoryTest;
    CreateTransactionDTO createTransactionDTOTest;
    Transaction transactionNullCategoryAndUserTest;

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
                .username("name")
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

        transactionNullCategoryAndUserTest = Transaction.builder()
                .appuser(null)
                .category(null)
                .build();
    }

    @Test
    @DisplayName("Should get all transactions for current logged user.")
    void shouldGetAllTransactionsForLoggedUser() {
        //given
        given(appUserService.getLoggedInUserId()).willReturn(1L);
        given(transactionAdminService.getAllTransactionsForGivenUser(anyLong(),anyInt(), any(Sort.Direction.class), anyString())).willReturn(Collections.singletonList(transactionDTOTest));
        //when
        var transactions = transactionUserService.getAllTransactionsForLoggedUser(1, Sort.Direction.valueOf("ASC"), "id");

        //then
        var firstTransaction = transactions.get(0);
        assertThat(firstTransaction, instanceOf(TransactionDTO.class));
        assertThat(firstTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(firstTransaction, hasProperty("value", equalTo(600)));
        assertThat(firstTransaction, hasProperty("quantity", equalTo(2)));
        assertThat(firstTransaction, hasProperty("categoryName", equalTo("name")));
    }

    @Test
    @DisplayName("Should get all transactions with given category for current logged user.")
    void shouldGetAllTransactionsWithCategoryForLoggedUser() {
        //given
        given(appUserService.getLoggedInUserId()).willReturn(1L);
        given(transactionAdminService.getAllTransactionsForGivenUserAndCategory(anyLong(), anyLong(), anyInt(), any(Sort.Direction.class), anyString())).willReturn(Collections.singletonList(transactionDTOTest));
        //when
        var transactions = transactionUserService.getAllTransactionsForLoggedUserAndGivenCategory(1, 1, Sort.Direction.valueOf("ASC"), "id");

        //then
        var firstTransaction = transactions.get(0);
        assertThat(firstTransaction, instanceOf(TransactionDTO.class));
        assertThat(firstTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(firstTransaction, hasProperty("value", equalTo(600)));
        assertThat(firstTransaction, hasProperty("quantity", equalTo(2)));
        assertThat(firstTransaction, hasProperty("categoryName", equalTo("name")));
    }

    @Test
    @DisplayName("Should delete transaction and related category with no relations for logged user.")
    void shouldDeleteTransactionByIdAndCategoryForLoggedUser() {
        // given
        given(transactionAdminService.getTransactionByIdAndByAppuserId(anyLong(), anyLong())).willReturn(transactionTest);
        given(appUserService.getLoggedInUserId()).willReturn(1L);

        //when
        transactionUserService.deleteSingleTransactionForLoggedUser(transactionTest.getId());

        // then
        verify(transactionAdminService).deleteTransactionWithCategory(transactionTest);
    }

    @Test
    @DisplayName("Should create transaction for logged user with known category.")
    void shouldCreateTransactionForLoggedUserWithKnownCategory() {
        //given
        given(appUserService.getLoggedInUserId()).willReturn(1L);
        given(transactionAdminService.createTransactionForGivenUser(anyLong(), any(CreateTransactionDTO.class))).willReturn(transactionDTOTest);

        //when
        var savedTransaction = transactionUserService.createTransactionForLoggedUser(createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(savedTransaction, hasProperty("value", equalTo(600)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(2)));
        assertThat(savedTransaction, hasProperty("categoryName", equalTo("name")));
    }

    @Test
    @DisplayName("Should replace transaction content for logged user.")
    void shouldReplaceTransactionForLoggedUser() {
        //given
        given(appUserService.getLoggedInUserId()).willReturn(1L);
        given(transactionAdminService.replaceTransaction(anyLong(), anyLong(), any(CreateTransactionDTO.class))).willReturn(transactionDTOTest);

        //when
        var savedTransaction = transactionUserService.replaceTransactionForLoggedUser(transactionTest.getAppuser().getId(), createTransactionDTOTest);

        //then
        assertThat(savedTransaction, instanceOf(TransactionDTO.class));
        assertThat(savedTransaction, hasProperty("transactionType", equalTo(TransactionType.INCOME)));
        assertThat(savedTransaction, hasProperty("value", equalTo(600)));
        assertThat(savedTransaction, hasProperty("quantity", equalTo(2)));
        assertThat(savedTransaction, hasProperty("description", equalTo("desc")));
        assertThat(savedTransaction, hasProperty("title", equalTo("title")));
        assertThat(savedTransaction, hasProperty("categoryName", equalTo("name")));
    }
}