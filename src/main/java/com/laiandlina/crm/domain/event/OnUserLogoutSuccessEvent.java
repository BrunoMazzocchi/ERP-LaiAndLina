package com.laiandlina.crm.domain.event;

import com.laiandlina.crm.persistance.data.*;
import lombok.*;
import org.springframework.context.*;

import java.time.*;
import java.util.*;

@Getter
@Setter
public class OnUserLogoutSuccessEvent extends ApplicationEvent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String userEmail;
    private final String token;
    private final transient LogOutRequest logOutRequest;
    private final Date eventTime;

    public OnUserLogoutSuccessEvent(String userEmail, String token, LogOutRequest logOutRequest) {
        super(userEmail);
        this.userEmail = userEmail;
        this.token = token;
        this.logOutRequest = logOutRequest;
        this.eventTime = Date.from(Instant.now());
    }
}
