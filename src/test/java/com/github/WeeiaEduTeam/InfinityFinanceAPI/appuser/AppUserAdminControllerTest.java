package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.BaseIntegrationTests;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserAdminDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.ReplaceAppUserAllDetailsDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
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
class AppUserAdminControllerTest extends BaseIntegrationTests {

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
    private final String getAllUsersAdminUrl = apiVersionUrl + "/admin/users";
    private final String deleteAllUsersAdminUrl = apiVersionUrl + "/admin/users/1";
    private final String putReplaceUserAdminUrl = apiVersionUrl + "/admin/users/1";
    private final String postCreateUserAdminUrl = apiVersionUrl + "/admin/users";

    private String exampleEmail = "example@wp.pl";
    private String exampleUsername = "Patryk1235!";
    private final String examplePassword = "{noop}example1231!";

    @Test
    @DisplayName("Should get all users.")
    void shouldGetAllUsers() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(getAllUsersAdminUrl))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    @DisplayName("Should add correct user with user and admin role.")
    void shouldAddUser() throws Exception {
        CreateAppUserAdminDTO createAppUserAdminDTO = new CreateAppUserAdminDTO();
        createAppUserAdminDTO.setEmail(exampleEmail);
        createAppUserAdminDTO.setPassword(examplePassword);
        createAppUserAdminDTO.setUsername(exampleUsername);
        createAppUserAdminDTO.setRoles(List.of(RoleDTO.builder().name("ROLE_ADMIN").build()));

        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAppUserAdminDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(postCreateUserAdminUrl)
                                .contentType(JSON)
                                .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(exampleEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(exampleUsername))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0].name").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[1].name").value("ROLE_ADMIN"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Should delete user by id.")
    void shouldDeleteUserById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(deleteAllUsersAdminUrl))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Should replace user.")
    void shouldReplaceUser() throws Exception {
        exampleUsername += "unique";
        exampleEmail += "unique";

        var userToReplace = new ReplaceAppUserAllDetailsDTO();
        userToReplace.setUsername(exampleUsername);
        userToReplace.setEmail(exampleEmail);
        userToReplace.setPassword(examplePassword);
        userToReplace.setRoles(List.of(RoleDTO.builder().name("shouldCreateOnlyUserRole").build()));

        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(userToReplace);

        mockMvc.perform(
                        MockMvcRequestBuilders.put(putReplaceUserAdminUrl)
                                .contentType(JSON)
                                .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(exampleEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(exampleUsername))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0].name").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}