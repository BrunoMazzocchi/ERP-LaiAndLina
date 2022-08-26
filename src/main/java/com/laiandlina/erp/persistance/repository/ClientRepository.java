package com.laiandlina.erp.persistance.repository;


import com.laiandlina.erp.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query(nativeQuery = true, value = "select * from client where state <> 3")
    List<Client> findAll();
    Optional<Client>findById(int clientId);
    Client save(Client client);
    void deleteById(int idClient);
}
