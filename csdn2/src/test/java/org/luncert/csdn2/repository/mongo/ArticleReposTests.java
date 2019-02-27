package org.luncert.csdn2.repository.mongo;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.csdn2.model.mongo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArticleReposTests
{

    @Autowired
    private ArticleRepos repos;

    @Test
    public void testSave()
    {
        Article entity = Article.builder()
            .title("Java GC")
            .createTime(new Date().toString())
            .author("Luncert")
            .articleId("90123809813")
            .readCount("302")
            .tags(new String[]{"Java", "GC"})
            .content("blabla.....")
            .build();
        repos.save(entity);
    }
    
}