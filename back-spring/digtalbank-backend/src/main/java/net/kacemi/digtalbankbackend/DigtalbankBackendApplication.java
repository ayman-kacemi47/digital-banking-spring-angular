package net.kacemi.digtalbankbackend;

import net.kacemi.digtalbankbackend.dtos.BankAccountDTO;
import net.kacemi.digtalbankbackend.dtos.CustomerDTO;
import net.kacemi.digtalbankbackend.entities.*;
import net.kacemi.digtalbankbackend.enums.AccountStatus;
import net.kacemi.digtalbankbackend.enums.OperationType;
import net.kacemi.digtalbankbackend.excepetions.BankAccountNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.CustomerNotFoundException;
import net.kacemi.digtalbankbackend.excepetions.NotEnoughBalanceException;
import net.kacemi.digtalbankbackend.repositories.BankAccountRepository;
import net.kacemi.digtalbankbackend.repositories.CustomerRepository;
import net.kacemi.digtalbankbackend.repositories.OperationRepository;
import net.kacemi.digtalbankbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigtalbankBackendApplication {

//        double toFixed2(double amount) {
//        BigDecimal bd = new BigDecimal(amount);
//        String converted = bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
//        double res = Double.parseDouble(converted);
//            System.out.println("************************************");
//            System.out.println("amount: " + amount);
//            System.out.println("res: " + res);
//            System.out.println("************************************");
//        return res;
//        }
//
    public static void main(String[] args) {
        SpringApplication.run(DigtalbankBackendApplication.class, args);
    }

    @Bean
            CommandLineRunner testBankAccountService(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Mohamed","Ayman","Hiba","Malak","Youssef","Aicha").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setId(UUID.randomUUID().toString());//car ID est de type string donc je dois le faire mannuellement
                customer.setName(name);
                customer.setEmail(name + "@kacemi.net");
                bankAccountService.saveCustomer(customer);
            });


            bankAccountService.listCustomers().forEach(customer -> {
                BigDecimal randomNumber = BigDecimal.valueOf(Math.random()*10);
                if(randomNumber.compareTo(BigDecimal.valueOf(5)) <=0) {

                    BigDecimal initBalance = randomNumber.multiply(BigDecimal.valueOf(3112*3500));
                    BigDecimal overDraft = randomNumber.multiply(BigDecimal.valueOf(3112));
                    try {
                        bankAccountService.saveCurrentAccount(initBalance, overDraft, customer.getId());
                    } catch (CustomerNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    BigDecimal initBalance = randomNumber.multiply(BigDecimal.valueOf(3112*3500));

                    try {
                        bankAccountService.saveSavingAccount(initBalance, Math.random()*10, customer.getId());
                    } catch (CustomerNotFoundException e) {
                       // throw new RuntimeException(e);
                        e.printStackTrace();

                    }

                }

            });
            List<BankAccountDTO> bankAccountDTOs = bankAccountService.listBankAccounts();
            for(BankAccountDTO bankAccountDTO : bankAccountDTOs) {
                for(int i = 0 ; i < 10 ; i++){
                    try {
                        bankAccountService.credit(bankAccountDTO.getId(), 1000+Math.random()*12444, "Crédit");
                        bankAccountService.debit(bankAccountDTO.getId(), 1000+Math.random()*12444, "débit");
                    } catch (BankAccountNotFoundException  | NotEnoughBalanceException e) {
                        // throw new RuntimeException(e);
                        e.printStackTrace();
                    }
                }
            }


        };
    }

    //@Bean
        CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository, OperationRepository operationRepository) {
        return args -> {
            BankAccount bankAccount = bankAccountRepository.findById("70377e05-7a7a-4ff9-b6c6-a6297506374d").orElse(null);
            if (bankAccount == null) {
                System.out.println("Bank account not found");
            }else{
                System.out.println("ID "+bankAccount.getId());
                System.out.println("BALANCE "+bankAccount.getBalance() + " " + bankAccount.getCurrency());
                System.out.println("STATUS"+bankAccount.getStatus());
                System.out.println("created At"+bankAccount.getCreatedAt());
                System.out.println("CUSTOMER NAME "+ bankAccount.getCustomer().getName());
                System.out.println("instance  of "+ bankAccount.getClass().getName());
                if(bankAccount instanceof SavingAccount){
                    SavingAccount savingAccount = (SavingAccount) bankAccount;
                    System.out.println("INTEREST RATE "+savingAccount.getInterestRate());
                }else{
                    CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                    System.out.println("OVERDRAFT "+currentAccount.getOverDraft());

                }
                System.out.println("------------ Operations --------------------");
                bankAccount.getOperations().forEach(operation -> {
                    System.out.println(operation.getId()+"\t"+operation.getOperationType()+"\t"+operation.getAmount()+"\t"+operation.getDate());
                });

            }
        };
        }
        //@Bean
        CommandLineRunner start(CustomerRepository customerRepository,
                                BankAccountRepository bankAccountRepository,
                                OperationRepository operationRepository) {
        return args -> {
            Stream.of("Mohamed","Ayman","Hiba","Malak","Youssef").forEach(name -> {
               Customer customer = new Customer();
               customer.setId(UUID.randomUUID().toString());//car ID est de type string donc je dois le faire mannuellement
               customer.setName(name);
               customer.setEmail(name + "@kacemi.net");
               customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {
                BigDecimal randomNumber = BigDecimal.valueOf(Math.random()*10);
                if(randomNumber.compareTo(BigDecimal.valueOf(5)) <=0) {
                    CurrentAccount currenAccount = new CurrentAccount();
                    currenAccount.setId(UUID.randomUUID().toString());
                    currenAccount.setBalance(randomNumber.multiply(BigDecimal.valueOf(3112))
                            .add(BigDecimal.valueOf(200))
                            .multiply(BigDecimal.valueOf(111)));
                    currenAccount.setCreatedAt(new Date());
                    currenAccount.setCurrency("MAD");
                    currenAccount.setCustomer(customer);
                    currenAccount.setStatus(AccountStatus.CREATED);
                    currenAccount.setOverDraft(randomNumber.multiply(BigDecimal.valueOf(3112)) );

                    bankAccountRepository.save(currenAccount);

                }else{
                    SavingAccount savingAccount = new SavingAccount();
                    savingAccount.setId(UUID.randomUUID().toString());
                    savingAccount.setBalance(randomNumber.multiply(BigDecimal.valueOf(3112))
                            .add(BigDecimal.valueOf(200))
                            .multiply(BigDecimal.valueOf(111)));
                    savingAccount.setCreatedAt(new Date());
                    savingAccount.setCurrency("MAD");
                    savingAccount.setCustomer(customer);
                    savingAccount.setStatus(AccountStatus.CREATED);
                    savingAccount.setInterestRate(Math.random() );

                    bankAccountRepository.save(savingAccount);

                }
            });


            bankAccountRepository.findAll().forEach(bankAccount -> {
                for(int i = 0; i < 10; i++){
                    Operation operation = new Operation();
                    operation.setAmount(Math.random()*1000);
                    operation.setDate(new Date());
                    operation.setOperationType(Math.random()>0.5 ? OperationType.CREDIT : OperationType.DEBIT);
                    operation.setBankAccount(bankAccount);
                    operationRepository.save(operation);
                }
            });

        };


        }

}
