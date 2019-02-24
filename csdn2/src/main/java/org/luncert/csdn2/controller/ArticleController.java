package org.luncert.csdn2.controller;

import org.luncert.csdn2.model.Article;
import org.luncert.csdn2.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController
{

    @Autowired
    ArticleService articleService;

    @GetMapping("/test")
    public Article test() throws Exception
    {
        return articleService.getArticle("https://blog.csdn.net/u011499747/article/details/50725131");
    }

}