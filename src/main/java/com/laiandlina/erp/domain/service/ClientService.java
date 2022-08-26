package com.laiandlina.erp.domain.service;

import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
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

    public void delete(int idClient){
        clientRepository.deleteById(idClient);
    }

}
