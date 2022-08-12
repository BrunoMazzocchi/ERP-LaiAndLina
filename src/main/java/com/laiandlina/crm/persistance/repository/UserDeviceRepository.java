package com.laiandlina.crm.persistance.repository;



import com.laiandlina.crm.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer> {


    Optional<UserDevice> findById(int id);

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    Optional<UserDevice> findByUserId(int userId);
}