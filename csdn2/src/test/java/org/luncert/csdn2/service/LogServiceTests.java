package org.luncert.csdn2.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LogServiceTests
{

    @Autowired
    private LogService logService;

    @Test
    public void test()
    {
        logService.info("info test log", "no more details");
        logService.warn("warn test log", "no more details");
        logService.error("error test log", "no more details");
    }

}