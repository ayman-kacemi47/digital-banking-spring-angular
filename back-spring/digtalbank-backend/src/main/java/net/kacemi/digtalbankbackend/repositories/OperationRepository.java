package net.kacemi.digtalbankbackend.repositories;

import net.kacemi.digtalbankbackend.entities.BankAccount;
import net.kacemi.digtalbankbackend.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
