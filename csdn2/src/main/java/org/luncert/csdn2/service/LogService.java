package org.luncert.csdn2.service;

import org.luncert.csdn2.model.mongo.LogEntity;
import org.luncert.csdn2.model.mongo.LogEntity.LogLevel;
import org.luncert.csdn2.repository.mongo.LogRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService
{

    @Autowired
    private LogRepos logRepos;

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
        logRepos.save(LogEntity.builder()
            .timestamp(String.valueOf(System.currentTimeMillis()))
            .level(level.getName())
            .desc(desc)
            .detail(detail)
            .build());
    }

}