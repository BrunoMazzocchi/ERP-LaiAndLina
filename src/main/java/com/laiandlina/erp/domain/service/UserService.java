package com.laiandlina.erp.domain.service;

import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UserService
{
    @Autowired
    UserRepository userRepository;

     Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    };

     public User getByEmail(String email){
         return userRepository.getByEmail(email);
     }

     public void updateUserProfilePicture(String img, int idUser){
          userRepository.saveUserPicture(img, idUser);
     }
}
