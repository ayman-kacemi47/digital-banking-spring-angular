package net.kacemi.digtalbankbackend.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kacemi.digtalbankbackend.enums.OperationType;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Date date;
private double amount;
@Enumerated(EnumType.STRING)
private OperationType operationType;

@ManyToOne
private BankAccount bankAccount;
}
