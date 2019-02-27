package org.luncert.csdn2.service;

import org.luncert.csdn2.model.mongo.Log;
import org.luncert.csdn2.model.mongo.Log.LogLevel;
import org.luncert.csdn2.repository.mongo.LogRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService
{

    public static final String ON_SAVE_LOG_ENTITY = "OnSaveLogEntity";

    @Autowired
    private LogRepos logRepos;

    @Autowired
    private EventService eventService;

    public void info(String desc)
    {
        info(desc, null);
    }

    public void info(String desc, String detail)
    {
        log(LogLevel.Info, desc, detail);
    }

    public void warn(String desc)
    {
        warn(desc, null);
    }

    public void warn(String desc, String detail)
    {
        log(LogLevel.Warn, desc, detail);
    }

    public void error(String desc)
    {
        error(desc, null);
    }

    public void error(String desc, String detail)
    {
        log(LogLevel.Error, desc, detail);
    }

    private void log(LogLevel level, String desc, String detail)
    {
        save(Log.builder()
            .timestamp(String.valueOf(System.currentTimeMillis()))
            .level(level.getName())
            .desc(desc)
            .detail(detail)
            .build());
    }

    private void save(Log log)
    {
        // 发布事件，通知WebSocketHandler
        eventService.submit(ON_SAVE_LOG_ENTITY, log);
        logRepos.save(log);
    }

}