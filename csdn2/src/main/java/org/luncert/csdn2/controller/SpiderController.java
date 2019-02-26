package org.luncert.csdn2.controller;

import org.springframework.web.bind.annotation.RestController;
import org.luncert.csdn2.service.LogService;
import org.luncert.csdn2.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/spider")
public class SpiderController
{

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private LogService logService;

    @GetMapping("/start")
    public void start()
    {
        spiderService.start();
        logService.info("spider started");
    }

    @GetMapping("/stop")
    public void stop()
    {
        spiderService.stop();
        logService.info("spider stoped");
    }

    @GetMapping("/status")
    public String status()
    {
        return spiderService.status().name();
    }

}