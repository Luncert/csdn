package org.luncert.csdn2.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.csdn2.repository.mongo.LogRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 只引入Web层的Spring上下文,不启动Tomcat,由MockMVC发送请求
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LogControllerTests
{

    @Autowired
    MockMvc mock;
    
    @Autowired
    LogRepos repos;

    @Test
    public void testGetLastNHours() throws Exception
    {
        mock.perform(get("/log/last3Hours"))
            .andExpect(status().is(200))
            .andDo(print())
            .andReturn();
    }

    @Test
    public void testGetAll() throws Exception
    {
        mock.perform(get("/log"))
            .andExpect(status().is(200))
            .andDo(print())
            .andReturn();
    }

}