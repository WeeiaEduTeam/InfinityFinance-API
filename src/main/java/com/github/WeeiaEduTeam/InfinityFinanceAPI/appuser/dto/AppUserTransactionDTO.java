package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppUserTransactionDTO {
    private String username;
    private String email;
    private String firstName;
}
