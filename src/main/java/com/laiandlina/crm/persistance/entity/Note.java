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
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;


        @ManyToOne
        @JoinColumn(name = "idUser")
         private User user  ;

        @ManyToOne
        @JoinColumn(name = "idProductClient")
        private ProductClient productClient  ;

}
