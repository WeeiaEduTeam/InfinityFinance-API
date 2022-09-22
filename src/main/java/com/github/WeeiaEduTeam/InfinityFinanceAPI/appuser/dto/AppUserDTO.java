package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class AppUserDTO {
    private long id;

    private String username;
    private String email;
    private String firstName;
    private String secondName;

    private List<RoleDTO> roles;
}
