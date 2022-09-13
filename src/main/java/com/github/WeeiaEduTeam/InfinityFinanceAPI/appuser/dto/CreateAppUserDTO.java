package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class CreateAppUserDTO {
    private String username;
    private String password;
    private String email;
}
