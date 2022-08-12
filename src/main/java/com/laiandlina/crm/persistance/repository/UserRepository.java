package com.laiandlina.crm.persistance.repository;


import com.laiandlina.crm.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    User getByEmail(String email);


    List<User> findAll();
}
