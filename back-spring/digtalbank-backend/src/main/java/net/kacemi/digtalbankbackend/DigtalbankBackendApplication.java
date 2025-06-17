package net.kacemi.digtalbankbackend;

import net.kacemi.digtalbankbackend.entities.Customer;
import net.kacemi.digtalbankbackend.repositories.BankAccountRepository;
import net.kacemi.digtalbankbackend.repositories.CustomerRepository;
import net.kacemi.digtalbankbackend.repositories.OperationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigtalbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigtalbankBackendApplication.class, args);
    }

        @Bean
        CommandLineRunner start(CustomerRepository customerRepository,
                                BankAccountRepository bankAccountRepository,
                                OperationRepository operationRepository) {
        return args -> {
            Stream.of("Mohamed","Ayman","Hiba").forEach(name -> {
               Customer customer = new Customer();
               customer.setId(UUID.randomUUID().toString());//car ID est de type string donc je dois le faire mannuellement
               customer.setName(name);
               customer.setEmail(name + "@kacemi.net");
               customerRepository.save(customer);
            });
        };
        }

}
