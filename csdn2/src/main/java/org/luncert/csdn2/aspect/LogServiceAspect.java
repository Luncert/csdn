package org.luncert.csdn2.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.luncert.csdn2.model.mongo.LogEntity;
import org.luncert.csdn2.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogServiceAspect
{

    @Autowired
    private EventService eventService;

    public static final String ON_SAVE_LOG_ENTITY = "OnSaveLogEntity";

    @Before(value="execution(* org.luncert.csdn2.service.LogService.save(..)) && args(logEntity)")
    public void beforeSave(LogEntity logEntity) {
        System.out.println(111);
        eventService.submit(ON_SAVE_LOG_ENTITY, logEntity);
    }

}