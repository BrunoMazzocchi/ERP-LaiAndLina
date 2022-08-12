package com.laiandlina.crm.persistance.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    private Date date;
    private Integer state;
    private Integer user;



}
