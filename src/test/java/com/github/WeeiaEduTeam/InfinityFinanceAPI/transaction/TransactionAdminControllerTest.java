package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.BaseIntegrationTests;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserAdminDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.ReplaceAppUserAllDetailsDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
class TransactionAdminControllerTest extends BaseIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final MediaType JSON = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8
    );

    private final String apiVersionUrl = "/api/v1";
    private final String getAllTransactionsAdminUrl = apiVersionUrl + "/admin/users/4/transactions";
    private final String postCreateTransactionAdminUrl = apiVersionUrl + "/admin/users/4/transactions";
    private final String putReplaceTransactionAdminUrl = apiVersionUrl + "/admin/users/8/transactions/7";
    private final String deleteTransactionAdminUrl = apiVersionUrl + "/admin/users/transactions/4";

    private final String exampleCategory = "exampleCategory";
    private final String exampleDescription = "exampleDescription";
    private final String exampleTitle = "exampleTitle";
    private final int exampleValue = 10;
    private final int exampleQuantity = 10;

    @Test
    @DisplayName("Should get all transactions.")
    void shouldGetAllTransactions() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(getAllTransactionsAdminUrl))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    @DisplayName("Should add transaction for given user id.")
    void shouldAddTransaction() throws Exception {
        var transactionToAdd = CreateTransactionDTO.builder()
                .transactionType(TransactionType.INCOME)
                .title(exampleTitle)
                .categoryName(exampleCategory)
                .value(exampleValue)
                .quantity(exampleQuantity)
                .description(exampleDescription)
                .build();

        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(transactionToAdd);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(postCreateTransactionAdminUrl)
                                .contentType(JSON)
                                .content(json))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categoryName").value(exampleCategory))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(exampleTitle))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.value").value(exampleValue))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(exampleQuantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(exampleDescription))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Should delete transaction by id.")
    void shouldDeleteTransactionById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(deleteTransactionAdminUrl))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Should replace transaction.")
    void shouldReplaceTransaction() throws Exception {
        var transactionToAdd = CreateTransactionDTO.builder()
                .transactionType(TransactionType.OUTCOME)
                .title(exampleTitle)
                .categoryName(exampleCategory)
                .value(exampleValue)
                .quantity(exampleQuantity)
                .description(exampleDescription)
                .build();

        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(transactionToAdd);

        mockMvc.perform(
                        MockMvcRequestBuilders.put(putReplaceTransactionAdminUrl)
                                .contentType(JSON)
                                .content(json))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categoryName").value(exampleCategory))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(exampleTitle))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.value").value(exampleValue))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(exampleQuantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(exampleDescription))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}