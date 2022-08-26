package com.laiandlina.erp.persistance.repository;

import com.laiandlina.erp.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    Note save(Note note);

    @Query(nativeQuery = true, value = "select * from vw_note where id_product_client = ?")
    List<VwNote> findNoteByProductClient(int idProductClient);
}
