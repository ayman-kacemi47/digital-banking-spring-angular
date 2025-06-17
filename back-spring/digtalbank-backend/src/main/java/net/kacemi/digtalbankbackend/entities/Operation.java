package net.kacemi.digtalbankbackend.entities;


import jakarta.persistence.*;
import net.kacemi.digtalbankbackend.enums.OperationType;

import java.sql.Date;

@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Date date;
private double amount;
private OperationType operationType;

@ManyToOne
private BankAccount bankAccount;
}
