package com.openclassrooms.mdd.security.services;

import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        // 1. Searching for User in BDD by email or username
        User user = userRepository.findByEmail(emailOrUsername)
                .orElseGet(() -> userRepository.findByUsername(emailOrUsername)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email or username: " + emailOrUsername)));

        // 2. Convert it to CustomUserDetails (includes ID)
        return CustomUserDetails.build(user.getId(), user.getEmail(), user.getPassword());
    }
}
