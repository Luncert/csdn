package org.luncert.csdn2.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.csdn2.model.normal.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTests
{

    @Autowired
    ArticleService service;

    @Test
    public void testGetMoreArticles() throws Exception
    {
        String shownOffset = service.getMoreArticle(Category.Career, null);
        System.out.println(shownOffset);
    }

}