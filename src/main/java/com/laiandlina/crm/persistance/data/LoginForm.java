package com.laiandlina.crm.persistance.data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    // Payload Validators
    private String email;
    private String password;


}
