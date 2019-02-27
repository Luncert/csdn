package org.luncert.csdn2.controller;

import java.util.Calendar;
import java.util.List;

import org.luncert.csdn2.model.mongo.LogEntity;
import org.luncert.csdn2.repository.mongo.LogRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供查询LogEntity的接口
 */
@RestController
@RequestMapping("/log")
public class LogController
{

    @Autowired
    private LogRepos repos;

    @GetMapping
    public Page<LogEntity> logs(@RequestParam(name="page") int page,
        @RequestParam(name="size") int size)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "timestamp");
        return repos.findAll(pageable);
    }

    @GetMapping("/count")
    public long logCount()
    {
        return repos.count();
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
    public List<LogEntity> logsLastNHours(@PathVariable("n") int n)
    {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR_OF_DAY, -n);
        String startTimestamp = String.valueOf(time.getTimeInMillis());
        return repos.findByTimestampAfter(startTimestamp);
    }
    
}