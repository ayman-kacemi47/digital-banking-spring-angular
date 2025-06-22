package net.kacemi.digtalbankbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kacemi.digtalbankbackend.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data @NoArgsConstructor
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")

@AllArgsConstructor
public abstract class BankAccount {
    @Id
    private String id;
    private Date createdAt;
    @Column(precision = 12, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount",fetch = FetchType.LAZY)
    private List<Operation> operations;


}
