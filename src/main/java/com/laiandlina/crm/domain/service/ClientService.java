package com.laiandlina.crm.domain.service;

import com.laiandlina.crm.persistance.entity.*;
import com.laiandlina.crm.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public Client save(Client client) {
        return  clientRepository.save(client);
    }

    public Optional<Client> findClientById(int idClient){
        return  clientRepository.findById(idClient);
    }


}
