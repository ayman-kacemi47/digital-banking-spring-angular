package net.kacemi.digtalbankbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data @NoArgsConstructor
@DiscriminatorValue("SA")
@AllArgsConstructor
public class SavingAccount extends BankAccount {
    @Column(precision = 25, scale = 2)
    private BigDecimal interestRate;
}
