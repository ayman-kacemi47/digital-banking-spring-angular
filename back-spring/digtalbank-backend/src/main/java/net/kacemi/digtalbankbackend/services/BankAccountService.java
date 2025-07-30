package net.kacemi.digtalbankbackend.services;

import net.kacemi.digtalbankbackend.dtos.*;
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

    List<OperationDTO> accountHistory(String bankAccountId) throws BankAccountNotFoundException;

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws  BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);

    List<BankAccountDTO> customerListBankAccounts(String customerId) throws CustomerNotFoundException;

}
