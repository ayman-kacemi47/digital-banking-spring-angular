package net.kacemi.digtalbankbackend.repositories;

import net.kacemi.digtalbankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
