package com.laiandlina.crm.domain.event.listener;


import com.laiandlina.crm.domain.cache.*;
import com.laiandlina.crm.domain.event.*;
import com.laiandlina.crm.persistance.data.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;

@Component
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {

    private final LoggedOutJwtTokenCache tokenCache;
    private static final Logger logger = LoggerFactory.getLogger(OnUserLogoutSuccessEventListener.class);

    @Autowired
    public OnUserLogoutSuccessEventListener(LoggedOutJwtTokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    public void onApplicationEvent(OnUserLogoutSuccessEvent event) {
        if (null != event) {
            DeviceInfo deviceInfo = event.getLogOutRequest().getDeviceInfo();
            logger.info(String.format("Log out success event received for user [%s] for device [%s]", event.getUserEmail(), deviceInfo));
            tokenCache.markLogoutEventForToken(event);
        }
    }
}