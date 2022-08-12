package com.laiandlina.crm.persistance.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogOutRequest {
    private DeviceInfo deviceInfo;
    private String token;
}