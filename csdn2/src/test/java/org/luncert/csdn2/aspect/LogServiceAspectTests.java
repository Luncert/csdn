package org.luncert.csdn2.aspect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.csdn2.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LogServiceAspectTests
{

    @Autowired
    private LogService logService;

    @Test
    public void test() throws InterruptedException
    {
        for (int i = 0; i < 3; i++) {
            logService.info("test-" + i);
            Thread.sleep(1000);
        }
    }
    
}