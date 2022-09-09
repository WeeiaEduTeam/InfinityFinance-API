package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppUserServiceTest {

    @InjectMocks
    private AppUserService appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @Test
    @DisplayName("Should throw UsernameNotFoundException when logged in user does not exist")
    void shouldThrowExceptionWhenLoggedInUserDoesNotExist() {
        //given
        given(appUserRepository.getIdByUsername(anyString())).willReturn(null);

        // when
        assertThatThrownBy(() -> appUserService.getLoggedInUserId())
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found in the database");
    }
}