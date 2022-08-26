package com.laiandlina.erp.web.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.setSuccess(success);
        this.setMessage(message);
    }

}