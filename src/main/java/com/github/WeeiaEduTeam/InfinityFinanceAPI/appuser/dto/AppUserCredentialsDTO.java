package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AppUserCredentialsDTO {
    private String username;
    private String password;
}
