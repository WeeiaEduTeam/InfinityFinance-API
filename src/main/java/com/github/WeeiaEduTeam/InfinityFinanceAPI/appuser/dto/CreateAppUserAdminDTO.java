package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppUserAdminDTO extends CreateAppUserUserDTO {
    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }
}
