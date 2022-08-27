package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.Transaction;
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


    @ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles =  new ArrayList<>();

    @OneToMany(mappedBy = "appuser")
    private List<Transaction> transactions = new ArrayList<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
}
