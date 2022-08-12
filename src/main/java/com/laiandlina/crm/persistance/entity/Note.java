package com.laiandlina.crm.persistance.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
@Table(name = "notes")
public class Note {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    private Integer id;
    private String description;
    private Integer idUser;
    private Integer idProductClient;


        @ManyToOne
        @JoinColumn(name = "id_user")
         private User user  ;

    @ManyToOne
    @JoinColumn(name = "id_productClient")
    private ProductClient productClient  ;

}
