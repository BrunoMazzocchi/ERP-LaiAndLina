package com.laiandlina.erp.persistance.repository;

import com.laiandlina.erp.persistance.data.*;
import com.laiandlina.erp.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(nativeQuery = true, value = "select * from vw_post")
    List<VwPost> findAllPost();
    Post save(Post post);

    Post findById(int id);
    void deleteById(int idPost);

    @Query(nativeQuery = true, value = "select * from vw_post where user = ?")
    List<VwPost> findByUserId (int idUser);
}
