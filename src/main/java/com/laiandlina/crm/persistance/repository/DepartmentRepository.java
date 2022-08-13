package com.laiandlina.crm.persistance.repository;

import com.laiandlina.crm.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
