package com.laiandlina.erp.domain.service;

import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.entity.Role;
import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class InitialDataLoader {
    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public ApplicationRunner initializer() {
        List<RoleName> roles = Arrays.asList(RoleName.ROLE_ADMIN, RoleName.ROLE_USER);
        return args -> roles.forEach(i -> createRoleIfNotFound(i));
    }

    private Optional<Role> createRoleIfNotFound(RoleName roleName){
        Optional<Role> role = roleRepository.findByRoleName(roleName);
        if (!role.isPresent()) {
            Role newRole = new Role();
            newRole.setRoleName(roleName);
            newRole = roleRepository.save(newRole);
        }
        return role;
    }
}
