package org.luncert.csdn2.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.luncert.csdn2.model.mongo.ArticleEntity;
import org.luncert.csdn2.repository.mongo.ArticleRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供查询ArticleEntity的接口
 */
@RestController("/article")
public class ArticleController
{

    @Autowired
    private ArticleRepos repos;

    @GetMapping("/{articleId}")
    public ArticleEntity article(@PathParam("articleId") String articleId)
    {
        return repos.findOneByArticleId(articleId);
    }

    @GetMapping("")
    public List<ArticleEntity> articles()
    {
        return repos.findAll();
    }

}