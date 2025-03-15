package com.example.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //New User registrations
    public Account registerUser(Account account){
        if(account.getUsername() == null || account.getUsername().isEmpty()){
            throw new CustomException("User can't be blank", HttpStatus.BAD_REQUEST);
        }
        if(account.getPassword() == null || account.getPassword().length() < 4){
            throw new CustomException("Password must be 4 or more characters", HttpStatus.BAD_REQUEST);
        }
        if(accountRepository.findByUsername(account.getUsername()).isPresent()){
            throw new CustomException("User already taken", HttpStatus.CONFLICT);
        }
        Account newAccount = new Account();
        newAccount.setUsername(account.getUsername());
        newAccount.setPassword(account.getPassword());
        
        return accountRepository.save(newAccount);
    }

    //Login User
    public Account loginUser(String username, String password){
        Optional<Account> user = accountRepository.findByUsername(username);
        if(user.isPresent() && user.get().getPassword().equals(password)){
            return user.get();
        }else{
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    //This method findById created for MessageService class to get 'getMessageById' method
    public Optional<Account> findById (Integer accountId){
        return accountRepository.findById(accountId);
    }

}