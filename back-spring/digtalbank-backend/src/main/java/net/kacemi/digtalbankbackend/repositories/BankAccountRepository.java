package net.kacemi.digtalbankbackend.repositories;

import net.kacemi.digtalbankbackend.entities.BankAccount;
import net.kacemi.digtalbankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
