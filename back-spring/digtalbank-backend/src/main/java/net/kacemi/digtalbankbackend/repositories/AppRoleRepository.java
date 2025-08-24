package net.kacemi.digtalbankbackend.repositories;

import net.kacemi.digtalbankbackend.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppRoleRepository   extends JpaRepository<AppRole, Long> {
    Optional<AppRole> findByRoleName(String roleName);
}
