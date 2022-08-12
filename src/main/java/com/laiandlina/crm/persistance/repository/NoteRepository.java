package com.laiandlina.crm.persistance.repository;

import com.laiandlina.crm.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    Note save(Note note);
    List<Note> findNoteByProductClient(int idProductClient);
}
