package com.laiandlina.erp.domain.service;


import com.laiandlina.erp.persistance.entity.User;
import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("No se encontro el usuario -> username or email : " + username)
                );

        return  UserPrincipal.build(user);
    }
}
