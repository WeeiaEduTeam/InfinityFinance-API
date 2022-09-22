package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ReplaceAppUserByUserDTO {

    private String email;
    private String firstName;
    private String secondName;
}
