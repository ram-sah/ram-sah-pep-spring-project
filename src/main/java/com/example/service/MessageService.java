package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.CustomException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

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


}
