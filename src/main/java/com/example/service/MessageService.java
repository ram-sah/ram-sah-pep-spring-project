package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.entity.Message;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }
    // create message
    public Message createMessage(Message message){
      if(message.getMessageText() == null || message.getMessageText().isEmpty()){
        throw new CustomException("Message can't be blank", HttpStatus.BAD_REQUEST);
      }
      if(message.getMessageText().length() > 255){
        throw new CustomException("Message can't exceed 255 characters", HttpStatus.BAD_REQUEST);
      }
      accountRepository.findById(message.getPostedBy()).orElseThrow(() -> new CustomException("User doesn't exist", HttpStatus.BAD_REQUEST));
      return messageRepository.save(message);
    }

    //Get all messages 
    public List<Message> getAllMessages(){
      return messageRepository.findAll();
    }

    //Get message by ID
    public Optional<Message> getMessageById(Integer messageId){
      return messageRepository.findById(messageId);
    }

    //Delete message byID
    public int deleteMessageById(Integer messageId) {
      if (!messageRepository.existsById(messageId)) {
          return 0;
      }
      messageRepository.deleteById(messageId);
      return 1;
      }
    
    //Update message
    public int updateMessage(Integer messageId, String newMessageText) {
      Optional<Message> existingMessage = messageRepository.findById(messageId);
      if (existingMessage.isPresent()) {
          Message message = existingMessage.get();
          message.setMessageText(newMessageText); // Update the message text
          messageRepository.save(message); // Save the updated message
          return 1;
      } else {
          return 0;
      }
    }

    //get all message by ID
    public List<Message> getMessagesById(Integer accountId){
      Account account = accountRepository.findById(accountId).orElse(null);
      if(account != null){
        return messageRepository.findByPostedBy(account.getAccountId());
      }
      return List.of();
    }

}
