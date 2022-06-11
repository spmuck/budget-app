package com.spmuck.budgetapp.controllers;


import com.spmuck.budgetapp.entity.AppUser;
import com.spmuck.budgetapp.models.LoginCredentials;
import com.spmuck.budgetapp.repository.UserRepository;
import com.spmuck.budgetapp.security.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private JWTUtility jwtUtility;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody AppUser user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user = userRepository.save(user);
        String token = jwtUtility.generateToken(user.getEmail());
        return Collections.singletonMap("jwt", token);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body){
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtility.generateToken(body.getEmail());

            return Collections.singletonMap("jwt", token);
        }catch (AuthenticationException authExc){
            throw new RuntimeException("Invalid Login Credentials");
        }
    }


}
