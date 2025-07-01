package net.kacemi.digtalbankbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kacemi.digtalbankbackend.dtos.BankAccountDTO;
import net.kacemi.digtalbankbackend.dtos.CurrentAccountDTO;
import net.kacemi.digtalbankbackend.dtos.CustomerDTO;
import net.kacemi.digtalbankbackend.dtos.SavingAccountDTO;
import net.kacemi.digtalbankbackend.entities.*;
import net.kacemi.digtalbankbackend.enums.AccountStatus;
import net.kacemi.digtalbankbackend.enums.OperationType;
import net.kacemi.digtalbankbackend.excepetions.BankAccountNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.CustomerNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.NotEnoughBalanceException;
import net.kacemi.digtalbankbackend.mappers.BankAccountMapper;
import net.kacemi.digtalbankbackend.mappers.CurrentAccountMapper;
import net.kacemi.digtalbankbackend.mappers.CustomerMapper;
import net.kacemi.digtalbankbackend.mappers.SavingAccountMapper;
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
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountMapper bankAccountMapper;
    BankAccountRepository bankAccountRepository;
    CustomerRepository customerRepository;
    OperationRepository operationRepository;
    CustomerMapper customerMapper;
    CurrentAccountMapper currentAccountMapper;
    SavingAccountMapper savingAccountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

        log.info("Saving new customer");
        Customer customer = customerMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccountDTO saveCurrentAccount(BigDecimal initialBalance, BigDecimal overDraft, String customerId) throws CustomerNotFoundException {
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

        return currentAccountMapper.fromCurrentAccount(bankAccountRepository.save(currentAccount));
    }

    @Override
    public SavingAccountDTO saveSavingAccount(BigDecimal initialBalance, double interestRate, String customerId) throws CustomerNotFoundException{
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

        return savingAccountMapper.fromSavingAccount(bankAccountRepository.save(savingAccount));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream().map((customer -> customerMapper.fromCustomer(customer))).toList();
    }

    @Override
    public List<BankAccountDTO> listBankAccounts() {
        return  bankAccountRepository.findAll().stream().map(bankAccount-> bankAccountMapper.fromBankAccount(bankAccount)).toList();
    }

    @Override
    public BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=   bankAccountRepository.findById(bankAccountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
        if( bankAccount instanceof CurrentAccount){
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return currentAccountMapper.fromCurrentAccount(currentAccount);
        }else{
            SavingAccount savingAccount= (SavingAccount) bankAccount;
            return savingAccountMapper.fromSavingAccount(savingAccount);
        }
    }

    @Override
    public void debit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException, NotEnoughBalanceException {
        BankAccount bankAccount =  bankAccountRepository.findById(bankAccountId).orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
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

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));

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
        BankAccount bankAccountSender = bankAccountRepository.findById(fromBankAccountId).orElseThrow(()->new BankAccountNotFoundException("Sender Bank account not found"));

        BankAccount bankAccountReciever =bankAccountRepository.findById(toBankAccountId).orElseThrow(()->new BankAccountNotFoundException("Reciever Bank account not found"));
        debit(fromBankAccountId,amount,"Transfer "+amount+" "+bankAccountSender.getCurrency()+" to "+ bankAccountReciever.getCustomer().getName());
        credit(toBankAccountId,amount,"Recieve "+amount+" "+bankAccountSender.getCurrency()+" from "+ bankAccountSender.getCustomer().getName());
    }
}
