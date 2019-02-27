package org.luncert.csdn2.controller;

import javax.websocket.server.PathParam;

import org.luncert.csdn2.model.mongo.Article;
import org.luncert.csdn2.repository.mongo.ArticleRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供查询ArticleEntity的接口
 */
@RestController
@RequestMapping("/article")
public class ArticleController
{

    @Autowired
    private ArticleRepos repos;

    @GetMapping("/{articleId}")
    public Article article(@PathParam("articleId") String articleId)
    {
        return repos.findOneByArticleId(articleId);
    }

    @GetMapping("/count")
    public long articleCount()
    {
        return repos.count();
    }

    @GetMapping
    public Page<Article> articles(@RequestParam(name="page") int page,
        @RequestParam(name="size") int size)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        return repos.findAll(pageable);
    }

}