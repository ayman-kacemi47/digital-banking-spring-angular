package net.kacemi.digtalbankbackend.services;

import net.kacemi.digtalbankbackend.entities.CurrentAccount;
import net.kacemi.digtalbankbackend.entities.Customer;
import net.kacemi.digtalbankbackend.entities.SavingAccount;
import net.kacemi.digtalbankbackend.entities.BankAccount;
import net.kacemi.digtalbankbackend.excepetions.BankAccountNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.CustomerNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.NotEnoughBalanceException;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountService{
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentAccount(BigDecimal initialBalance, BigDecimal overDraft, String customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingAccount(BigDecimal initialBalance, BigDecimal interestRate, String customerId) throws  CustomerNotFoundException;
    List<Customer> listCustomers();
    List<BankAccount> listBankAccounts();
    BankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
    void debit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException, NotEnoughBalanceException;
    void credit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String fromBankAccountId, String toBankAccountId, double amount, String description) throws BankAccountNotFoundException, NotEnoughBalanceException;
}
