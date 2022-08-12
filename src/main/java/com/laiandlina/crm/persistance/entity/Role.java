package com.laiandlina.crm.persistance.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String description;
    private int state;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RoleName roleName;
}