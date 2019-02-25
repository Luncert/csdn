package org.luncert.csdn2.controller;

import org.springframework.web.bind.annotation.RestController;
import org.luncert.csdn2.service.LogService;
import org.luncert.csdn2.service.SpiderService;
import org.luncert.csdn2.util.Result;
import org.luncert.csdn2.util.Result.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController("/spider")
public class SpiderController
{

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private LogService logService;

    @GetMapping("/start")
    public @ResponseBody Result start()
    {
        spiderService.start();
        logService.info("spider started");
        return new Result(Status.OK);
    }

    @GetMapping("/stop")
    public @ResponseBody Result stop()
    {
        spiderService.stop();
        logService.info("spider stoped");
        return new Result(Status.OK);
    }

}