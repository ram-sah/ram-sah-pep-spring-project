package com.example.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.CustomException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
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

    // Create messages
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message){
        try {
            Message newMessage = messageService.createMessage(message);
            return ResponseEntity.status(HttpStatus.OK).body(newMessage);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //Get all messages 
    @GetMapping("/messages")
    public ResponseEntity<List<Message>>getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }
   
    //Get message by ID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){
        Optional<Message> message = messageService.getMessageById(messageId);
        if(message.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(message.get());
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }
    //Delete messages by ID
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable Integer messageId) {
    int rowsDeleted = messageService.deleteMessageById(messageId);
    if (rowsDeleted == 1) {
        return ResponseEntity.ok(1); // If deleted, return 1
    }
    return ResponseEntity.ok().build(); // If not found, return empty body with 200 OK
    }

    // Update message text by ID
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable Integer messageId, @RequestBody Map<String, String> requestBody) {
        String messageText = requestBody.get("messageText");

        if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
     // Attempt to update the message
     int rowsUpdated = messageService.updateMessage(messageId, messageText);

     if (rowsUpdated > 0) {
         return ResponseEntity.status(HttpStatus.OK).body(rowsUpdated);  
     } else {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0); 
     }
    }

    //Get messages by a particular userID
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessageById(@PathVariable Integer accountId){
        List<Message> message = messageService.getMessagesById(accountId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
}
