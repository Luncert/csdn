package org.luncert.csdn2.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.luncert.csdn2.model.mongo.ArticleEntity;
import org.luncert.csdn2.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ArticleReposAspect {

    @Autowired
    private EventService eventService;

    public static final String ON_SAVE_ARTICLE_ENTITY = "OnSaveArticleEntity";

    private static final String PACKAGE_NAME = "org.luncert.csdn2.repository.mongo.ArticleRepos";

    @Before("execution(public * " + PACKAGE_NAME + ".save()) and args(articleEntity)")
    public void beforeSave(ArticleEntity articleEntity) {
        eventService.submit(ON_SAVE_ARTICLE_ENTITY, articleEntity);
    }

}