package ru.selm.manager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.selm.manager.entity.Authority;
import ru.selm.manager.repository.CUserRepository;

@Service
@RequiredArgsConstructor
public class CUserDetailService implements UserDetailsService {

    private final CUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user ->
                        User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .authorities(user.getAuthorities().stream()
                                        .map(Authority::getAuthority)
                                        .map(SimpleGrantedAuthority::new)
                                        .toList())
                                .build()
                ).orElseThrow(() -> new UsernameNotFoundException("User % not found".formatted(username)));
    }
}
