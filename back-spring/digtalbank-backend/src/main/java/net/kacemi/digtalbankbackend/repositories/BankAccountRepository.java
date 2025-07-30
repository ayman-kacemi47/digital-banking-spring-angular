package net.kacemi.digtalbankbackend.repositories;

import net.kacemi.digtalbankbackend.entities.BankAccount;
import net.kacemi.digtalbankbackend.entities.Customer;
import net.kacemi.digtalbankbackend.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    void deleteAllByCustomer(Customer customer);
    List<BankAccount> findBankAccountsByCustomer(Customer customer);
}
