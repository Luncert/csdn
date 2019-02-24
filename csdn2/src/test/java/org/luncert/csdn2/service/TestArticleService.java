package org.luncert.csdn2.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.csdn2.model.Article;
import org.luncert.csdn2.model.Category;
import org.luncert.csdn2.service.ArticleService.ArticleRefs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestArticleService
{

    @Autowired
    ArticleService service;

    @Test
    public void testGetArticle() throws Exception
    {
        Article article = service.getArticle("https://blog.csdn.net/u011499747/article/details/50725131");
        System.out.println(article);
    }

    @Test
    public void testGetMoreArticles() throws Exception
    {
        ArticleRefs articleRefs = service.getMoreArticle(Category.Career, null);
        System.out.println(articleRefs);
    }

}