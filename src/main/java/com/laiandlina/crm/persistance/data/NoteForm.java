package com.laiandlina.crm.persistance.data;

import lombok.*;

import javax.persistence.*;

@Data
public class NoteForm {
    @Id
    private Integer id;
    private Integer idUser;
    private Integer idProductClient;
    private String title;
    private String description;
}
