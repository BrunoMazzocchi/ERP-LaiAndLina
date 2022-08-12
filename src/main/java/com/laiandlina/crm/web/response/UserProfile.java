package com.laiandlina.crm.web.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private Integer id;
    private String email;
    private String name;
    private boolean active;
}