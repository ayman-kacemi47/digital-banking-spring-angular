package net.kacemi.digtalbankbackend.services;


import net.kacemi.digtalbankbackend.repositories.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository userRepository;

    public CustomUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        var authorities = appUser.getRoles()
                .stream()
                .map(role -> role.getRoleName())
                .toArray(String[]::new);

        return User.withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities(authorities)
                .build();
    }
}