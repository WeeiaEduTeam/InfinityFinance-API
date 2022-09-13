package com.github.WeeiaEduTeam.InfinityFinanceAPI.role.role;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.util.BaseUtil;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends BaseUtil {
    private String name;
}
