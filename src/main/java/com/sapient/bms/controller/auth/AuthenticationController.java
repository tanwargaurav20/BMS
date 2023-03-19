package com.sapient.bms.controller.auth;

import com.sapient.bms.dto.auth.AuthenticationRequest;
import com.sapient.bms.dto.auth.AuthenticationResponse;
import com.sapient.bms.service.auth.UserService;
import com.sapient.bms.service.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token = jwtUtil.generateToken(userdetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<?> registerUser(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        try {
            userDetailsService.registerNewUser(authenticationRequest);
        } catch (Exception e) {
            throw new Exception("USER_DISABLED", e);
        }

        return ResponseEntity.ok("User Registered successfully. Username " + authenticationRequest.getUsername());
    }

}
