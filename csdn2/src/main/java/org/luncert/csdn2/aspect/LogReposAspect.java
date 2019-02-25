package org.luncert.csdn2.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.luncert.csdn2.model.mongo.LogEntity;
import org.luncert.csdn2.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogReposAspect
{

    @Autowired
    private EventService eventService;

    public static final String ON_SAVE_LOG_ENTITY = "OnSaveLogEntity";

    private static final String PACKAGE_NAME = "org.luncert.csdn2.repository.mongo.LogRepos";

    @Before("execution(public * " + PACKAGE_NAME + ".save()) and args(logEntity)")
    public void beforeSave(LogEntity logEntity) {
        eventService.submit(ON_SAVE_LOG_ENTITY, logEntity);
    }

}