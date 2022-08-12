package com.laiandlina.crm.persistance.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
@Table(name = "note")
public class Note {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    private Integer id;
    private String description;


        @ManyToOne
        @JoinColumn(name = "idUser")
         private User user  ;

        @ManyToOne
        @JoinColumn(name = "idProductClient")
        private ProductClient productClient  ;

}
