package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ReplaceAppUserAllDetailsDTO  extends CreateAppUserAdminDTO {

    private String firstName;
    private String secondName;
}
