package net.kacemi.digtalbankbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kacemi.digtalbankbackend.entities.*;
import net.kacemi.digtalbankbackend.enums.AccountStatus;
import net.kacemi.digtalbankbackend.enums.OperationType;
import net.kacemi.digtalbankbackend.excepetions.BankAccountNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.CustomerNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.NotEnoughBalanceException;
import net.kacemi.digtalbankbackend.repositories.BankAccountRepository;
import net.kacemi.digtalbankbackend.repositories.CustomerRepository;
import net.kacemi.digtalbankbackend.repositories.OperationRepository;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    BankAccountRepository bankAccountRepository;
    CustomerRepository customerRepository;
    OperationRepository operationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {

        log.info("Saving new customer");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentAccount(BigDecimal initialBalance, BigDecimal overDraft, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
            CurrentAccount currentAccount= new CurrentAccount();
            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setBalance(initialBalance);
            currentAccount.setCustomer(customer);
            currentAccount.setCurrency("MAD");
            currentAccount.setCreatedAt(new Date());
            currentAccount.setStatus(AccountStatus.ACTIVATED);
            currentAccount.setOverDraft(overDraft);

        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public SavingAccount saveSavingAccount(BigDecimal initialBalance, BigDecimal interestRate, String customerId) throws CustomerNotFoundException{
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount= new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setCurrency("MAD");
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.ACTIVATED);
        savingAccount.setInterestRate(interestRate);

        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<BankAccount> listBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(bankAccountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
    }

    @Override
    public void debit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException, NotEnoughBalanceException {
        BankAccount bankAccount = getBankAccount(bankAccountId);
        if(bankAccount.getBalance().subtract(BigDecimal.valueOf(amount)).compareTo(BigDecimal.ZERO) < 0){
            throw new NotEnoughBalanceException("Not enough balance");
        }else{
            Operation operation = new Operation();
            operation.setBankAccount(bankAccount);
            operation.setAmount(amount);
            operation.setDate(new Date());
            operation.setOperationType(OperationType.DEBIT);
            operation.setDescription(description);
            bankAccount.getOperations().add(operation);
            bankAccount.setBalance(bankAccount.getBalance().subtract(BigDecimal.valueOf(amount)));
            bankAccountRepository.save(bankAccount);
            operationRepository.save(operation);
        }
    }

    @Override
    public void credit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = getBankAccount(bankAccountId);

            Operation operation = new Operation();
            operation.setBankAccount(bankAccount);
            operation.setAmount(amount);
            operation.setDate(new Date());
            operation.setOperationType(OperationType.CREDIT);
            operation.setDescription(description);
            bankAccount.getOperations().add(operation);
            bankAccount.setBalance(bankAccount.getBalance().add(BigDecimal.valueOf(amount)));
            bankAccountRepository.save(bankAccount);
            operationRepository.save(operation);

    }

    @Override
    public void transfer(String fromBankAccountId, String toBankAccountId, double amount, String description) throws BankAccountNotFoundException, NotEnoughBalanceException {
        BankAccount bankAccountSender = getBankAccount(fromBankAccountId);
        BankAccount bankAccountReciever = getBankAccount(toBankAccountId);
        debit(fromBankAccountId,amount,"Transfer "+amount+" "+bankAccountSender.getCurrency()+" to "+ bankAccountReciever.getCustomer().getName());
        credit(toBankAccountId,amount,"Recieve "+amount+" "+bankAccountSender.getCurrency()+" from "+ bankAccountSender.getCustomer().getName());
    }
}
