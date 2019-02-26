package org.luncert.csdn2.component;

import org.luncert.csdn2.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogJob {

    @Autowired
    private LogService logService;
    
    @Scheduled(fixedDelay = 3000)
    public void fixedDelayJob(){
        logService.info("this is a test log");
    }
    
}