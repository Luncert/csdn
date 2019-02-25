package org.luncert.csdn2.controller;

import java.util.Calendar;
import java.util.List;

import javax.websocket.server.PathParam;

import org.luncert.csdn2.model.mongo.LogEntity;
import org.luncert.csdn2.repository.mongo.LogRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供查询LogEntity的接口
 */
@RestController("/log")
public class LogController
{

    @Autowired
    private LogRepos repos;

    @GetMapping
    public List<LogEntity> logs()
    {
        return repos.findAll();
    }

    @GetMapping("/lastWeek")
    public List<LogEntity> lastWeek()
    {
        return logsLastNHours(168);
    }

    @GetMapping("/last3Day")
    public List<LogEntity> last3Day()
    {
        return logsLastNHours(72);
    }

    @GetMapping("/lastDay")
    public List<LogEntity> lastDay()
    {
        return logsLastNHours(24);
    }

    @GetMapping("/last{n}Hours")
    public List<LogEntity> logsLastNHours(@PathParam("n") int n)
    {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR_OF_DAY, -n);
        String startTimestamp = String.valueOf(time.getTimeInMillis());
        return repos.findByTimestampAfter(startTimestamp);
    }
    
}