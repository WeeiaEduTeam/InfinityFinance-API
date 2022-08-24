package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.ledger.Ledger;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    //@Column(unique = true)
    private String username;

    //@Column(unique = true)
    private String email;

    private String password;

    private String firstName;
    private String secondName;

    @ManyToMany(mappedBy = "users")
    private List<Role> roles =  new ArrayList<>();

    @OneToMany(mappedBy = "appuser")
    private List<Ledger> ledgers = new ArrayList<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
}
