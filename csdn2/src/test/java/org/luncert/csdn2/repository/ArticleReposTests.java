package org.luncert.csdn2.repository;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.csdn2.model.ArticleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleReposTests
{

    @Autowired
    private ArticleRepos repos;

    @Test
    public void testSave()
    {
        ArticleEntity entity = ArticleEntity.builder()
            .title("Java GC")
            .createTime(new Date().toString())
            .author("Luncert")
            .articleId("90123809813")
            .authorId("i092312")
            .readCount(302)
            .tags(new String[]{"Java", "GC"})
            .content("blabla.....")
            .build();
        repos.save(entity);
    }
    
}