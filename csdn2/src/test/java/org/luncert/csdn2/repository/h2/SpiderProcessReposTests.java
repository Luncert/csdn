package org.luncert.csdn2.repository.h2;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.csdn2.model.h2.SpiderProcess;
import org.luncert.csdn2.model.normal.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpiderProcessReposTests
{

    @Autowired
    private SpiderProcessRepos repos;

    @Test
    public void testSave()
    {
        repos.save(new SpiderProcess(Category.Ai, "987193212"));
    }

    @Test
    public void testFindAll()
    {
        List<SpiderProcess> list = repos.findAll();
        System.out.println(list);
    }

}