package com.spmuck.budgetapp.controllers;

import com.spmuck.budgetapp.entity.AppUser;
import com.spmuck.budgetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserRepository userRepository;

    @GetMapping("/info")
    public AppUser getUserDetails(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(email).get();
    }


}
