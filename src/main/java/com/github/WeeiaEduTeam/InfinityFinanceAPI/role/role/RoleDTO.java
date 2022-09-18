package com.github.WeeiaEduTeam.InfinityFinanceAPI.role.role;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof RoleDTO))
            return false;

        RoleDTO other = (RoleDTO)o;
        return (this.name == null && other.name == null)
                || (this.name != null && this.name.equals(other.name)
        );
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + name.hashCode();

        return result;
    }
}
