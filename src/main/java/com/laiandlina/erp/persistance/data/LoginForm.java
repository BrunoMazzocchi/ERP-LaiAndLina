package com.laiandlina.erp.persistance.data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    // Payload Validators
    private String email;
    private String password;


}
