package com.laiandlina.erp.domain.service;

import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;


    public List<Role> findAll(){
        return roleRepository.findAll();
    }
}
