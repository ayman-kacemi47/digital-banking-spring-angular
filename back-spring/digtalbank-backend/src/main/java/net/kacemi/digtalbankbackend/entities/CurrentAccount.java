package net.kacemi.digtalbankbackend.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("CA")
@AllArgsConstructor
public class CurrentAccount extends BankAccount {
    private double overDraft;
}
