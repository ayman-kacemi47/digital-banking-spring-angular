package net.kacemi.digtalbankbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kacemi.digtalbankbackend.dtos.*;
import net.kacemi.digtalbankbackend.entities.*;
import net.kacemi.digtalbankbackend.enums.AccountStatus;
import net.kacemi.digtalbankbackend.enums.OperationType;
import net.kacemi.digtalbankbackend.excepetions.BankAccountNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.CustomerNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.NotEnoughBalanceException;
import net.kacemi.digtalbankbackend.mappers.*;
import net.kacemi.digtalbankbackend.repositories.BankAccountRepository;
import net.kacemi.digtalbankbackend.repositories.CustomerRepository;
import net.kacemi.digtalbankbackend.repositories.OperationRepository;
import org.hibernate.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class BankAccountServiceImpl implements BankAccountService  {
    private final BankAccountMapper bankAccountMapper;
    BankAccountRepository bankAccountRepository;
    CustomerRepository customerRepository;
    OperationRepository operationRepository;
    CustomerMapper customerMapper;
    CurrentAccountMapper currentAccountMapper;
    SavingAccountMapper savingAccountMapper;
    OperationMapper operationMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

        log.info("Saving new customer");
        Customer customer = customerMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating new customer");
        Customer customer = customerMapper.fromCustomerDTO(customerDTO);
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.fromCustomer(updatedCustomer);
    }

    @Override
    public void deleteCustomer(String id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("no customer found with id: "+ id));
        for(BankAccount bankAccount : customer.getBankAccountList()) {
            operationRepository.deleteAllByBankAccount(bankAccount);
        }
        bankAccountRepository.deleteAll(customer.getBankAccountList());
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO getCustomer(String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("No customer found with id: "+customerId));
        return customerMapper.fromCustomer(customer);
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

    //withdrawl
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
           // bankAccountRepository.save(bankAccount);
            operationRepository.save(operation);
        }
    }

    //deposit
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
            //bankAccountRepository.save(bankAccount);
            operationRepository.save(operation);

    }

    @Override
    public void transfer(String fromBankAccountId, String toBankAccountId, double amount, String description) throws BankAccountNotFoundException, NotEnoughBalanceException {
        BankAccount bankAccountSender = bankAccountRepository.findById(fromBankAccountId).orElseThrow(()->new BankAccountNotFoundException("Sender Bank account not found"));

        BankAccount bankAccountReciever =bankAccountRepository.findById(toBankAccountId).orElseThrow(()->new BankAccountNotFoundException("Reciever Bank account not found"));
        debit(fromBankAccountId,amount,"Transfer "+amount+" "+bankAccountSender.getCurrency()+" to "+ bankAccountReciever.getCustomer().getName());
        credit(toBankAccountId,amount,"Recieve "+amount+" "+bankAccountSender.getCurrency()+" from "+ bankAccountSender.getCustomer().getName());
    }

    @Override
    public List<OperationDTO> accountHistory(String bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));

        return   operationRepository.findByBankAccountId(bankAccountId).stream().map(operation -> operationMapper.fromOperation(operation)).toList();
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
        Page<Operation> accountOperations=  operationRepository.findByBankAccountIdOrderByDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<OperationDTO> operationDTOList = accountOperations.getContent().stream().map(op -> operationMapper.fromOperation(op)).toList();
        accountHistoryDTO.setOperationDTOS(operationDTOList);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;

    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers= customerRepository.searchCustomer(keyword);

        return customers.stream().map(customer -> customerMapper.fromCustomer(customer)).toList();
    }

    @Override
    public List<BankAccountDTO> customerListBankAccounts(String customerId) {
        Customer customer = customerRepository.findById(customerId).get();
        return  bankAccountRepository.findBankAccountsByCustomer(customer).stream().map(bankAccount -> bankAccountMapper.fromBankAccount(bankAccount)).toList();

    }


}
