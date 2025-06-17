package net.kacemi.digtalbankbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kacemi.digtalbankbackend.enums.AccountStatus;

import java.sql.Date;
import java.util.List;

@Entity
@Data @NoArgsConstructor
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
public abstract class BankAccount {
    @Id
    private String id;
    private Date createdAt;
    private double balance;
    private AccountStatus status;
    private String currency;
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount")
    private List<Operation> operations;


}
