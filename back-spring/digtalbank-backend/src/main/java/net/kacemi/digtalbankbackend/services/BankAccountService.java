package net.kacemi.digtalbankbackend.services;

import net.kacemi.digtalbankbackend.dtos.BankAccountDTO;
import net.kacemi.digtalbankbackend.dtos.CurrentAccountDTO;
import net.kacemi.digtalbankbackend.dtos.CustomerDTO;
import net.kacemi.digtalbankbackend.dtos.SavingAccountDTO;
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
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(String id) throws CustomerNotFoundException;

    CustomerDTO getCustomer(String customerId) throws CustomerNotFoundException;
    CurrentAccountDTO saveCurrentAccount(BigDecimal initialBalance, BigDecimal overDraft, String customerId) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingAccount(BigDecimal initialBalance, double interestRate, String customerId) throws  CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    List<BankAccountDTO> listBankAccounts();
    BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
    void debit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException, NotEnoughBalanceException;
    void credit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String fromBankAccountId, String toBankAccountId, double amount, String description) throws BankAccountNotFoundException, NotEnoughBalanceException;
}
