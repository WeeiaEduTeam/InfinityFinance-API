package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CreateAppUserAdminDTO extends CreateAppUserUserDTO {
    private List<RoleDTO> roles;

    public boolean hasRole(String name) {
        RoleDTO role = new RoleDTO(name);

        return roles.stream()
                .filter(e -> e.equals(role))
                .toList()
                .size() > 0;
    }
}
