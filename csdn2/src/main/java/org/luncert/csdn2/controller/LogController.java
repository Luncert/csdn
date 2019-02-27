package org.luncert.csdn2.controller;

import java.util.Calendar;
import java.util.List;

import org.luncert.csdn2.model.mongo.Log;
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
 * 提供查询Log的接口
 */
@RestController
@RequestMapping("/log")
public class LogController
{

    @Autowired
    private LogRepos repos;

    @GetMapping
    public Page<Log> logs(@RequestParam(name="page") int page,
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
    public List<Log> lastWeek()
    {
        return logsLastNHours(168);
    }

    @GetMapping("/last3Day")
    public List<Log> last3Day()
    {
        return logsLastNHours(72);
    }

    @GetMapping("/lastDay")
    public List<Log> lastDay()
    {
        return logsLastNHours(24);
    }

    @GetMapping("/last{n}Hours")
    public List<Log> logsLastNHours(@PathVariable("n") int n)
    {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR_OF_DAY, -n);
        String startTimestamp = String.valueOf(time.getTimeInMillis());
        return repos.findByTimestampAfter(startTimestamp);
    }
    
}