package com.sapient.bms.service.auth;

import com.sapient.bms.dto.auth.AuthenticationRequest;
import com.sapient.bms.exception.BadRequestException;
import com.sapient.bms.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.sapient.bms.entity.User user = getUserEntity(email);
        List<SimpleGrantedAuthority> roles = null;
        if ("admin".equalsIgnoreCase(user.getRole())) {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new User(user.getEmail(), user.getPassword(),
                roles);
    }

    public com.sapient.bms.entity.User getUserEntity(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));
    }

    public void registerNewUser(AuthenticationRequest authenticationRequest) {

        if (userRepository.findByEmail(authenticationRequest.getUsername()).isPresent()) {
            throw new BadRequestException("User already exists");
        }
        com.sapient.bms.entity.User user = new com.sapient.bms.entity.User();
        user.setEmail(authenticationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authenticationRequest.getPassword()));
        user.setRole(authenticationRequest.getRole());
        userRepository.save(user);
    }
}
