package com.laiandlina.erp.persistance.repository;


import com.laiandlina.erp.persistance.data.*;
import com.laiandlina.erp.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    User getUserById(Integer id);
    User getByEmail(String email);

    @Query(nativeQuery = true, value = "select * from vw_user_department where email = ?")
    VwUserDepartment getUserDepartmentByEmail(String email);

    @Query(nativeQuery = true, value = "select * from vw_user_department where state <> 3")
    List<VwUserDepartment> findAllUser();

    @Query(nativeQuery = true, value = "select * from vw_user_department where state = 3")
    List<VwUserDepartment> getAllDisabledUser();

    @Query(nativeQuery = true, value = "select user.* from role\n" +
            "inner join user_roles\n" +
            "\ton user_roles.role_id = role.id\n" +
            "    inner join user \n" +
            "\t\ton user_roles.user_id = user.id where role.id =  ? and user.active = 1 and state <> 3;")
    List<User> findByRoles(int idRol);

    @Modifying
    @Transactional
    @Query(value = "update user u set u.url_photo = ?  where u.id = ?", nativeQuery = true)
    void saveUserPicture(String firstname, Integer userId);


}
