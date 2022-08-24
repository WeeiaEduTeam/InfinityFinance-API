package com.github.WeeiaEduTeam.InfinityFinanceAPI.category;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.ledger.Ledger;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    //@Column(unique = true)
    private String name;

    @OneToMany
    @JoinColumn(name = "ledger_id")
    private List<Ledger> ledgers;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
}
