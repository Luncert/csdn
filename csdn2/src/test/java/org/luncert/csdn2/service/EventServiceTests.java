package org.luncert.csdn2.service;

import java.util.EventListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * why use {@code @SpringBootTest} like this
 * <li>https://spring.io/guides/gs/testing-web/
 * <li>https://www.jianshu.com/p/5dae923e64a8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EventServiceTests
{

    @Autowired
    private EventService eventService;

    public static class HelloListener implements EventListener
    {

        public void sayHello(String name)
        {
            System.out.println("Hello, " + name + "!");
        }

    }

    @Test
    public void test()
    {
        eventService.register("OnHello", new HelloListener());
        eventService.submit("OnHello", "Luncert");
    }

}