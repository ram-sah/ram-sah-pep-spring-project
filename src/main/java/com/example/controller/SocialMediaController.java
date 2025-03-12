package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {

    private final AccountService accountService;
    @Autowired
    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }

    //User Registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account){
        try {
            Account newAccount = accountService.registerUser(account);
            return ResponseEntity.status(HttpStatus.OK).body(newAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    //User Login
    @PostMapping("/login")
    public ResponseEntity<?>loginUser(@RequestBody Account account){
        try {
            Account authenticatedUser = accountService.loginUser(account.getUsername(),
            account.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(authenticatedUser);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

   
}
