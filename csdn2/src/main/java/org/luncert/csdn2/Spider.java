package org.luncert.csdn2;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.luncert.csdn2.model.Article;
import org.luncert.csdn2.model.ArticleEntity;
import org.luncert.csdn2.model.ArticleRef;
import org.luncert.csdn2.model.Category;
import org.luncert.csdn2.model.LogEntity;
import org.luncert.csdn2.repository.ArticleRepos;
import org.luncert.csdn2.repository.LogRepos;
import org.luncert.csdn2.service.ArticleService;
import org.luncert.csdn2.service.ArticleService.ArticleRefs;
import org.luncert.csdn2.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 根据Category爬取csdn所有文章
 * TODO: 0 downtime code updating
 */
@Component
public class Spider {

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LogRepos logRepos;

    @Autowired
    private ArticleRepos articleRepos;

    public void start() {
        // 为每一个category都创建一个线程运行爬虫
        for (Category category : Category.categories) {
            threadPool.execute(() -> {
                int errorTime = 0;
                String shownOffset = null;
                // 如果连续三次getMoreArticle抛出异常，则结束执行该线程的爬虫
                while (errorTime < 3)
                {
                    try {
                        ArticleRefs refs = articleService.getMoreArticle(category, shownOffset);
                        for (ArticleRef ref : refs.getRefs())
                        {
                            try {
                                // save article to Mongodb
                                Article article = articleService.getArticle(ref.getUrl());
                                ArticleEntity articleEntity = ArticleEntity.builder()
                                    .title(article.getTitle())
                                    .createTime(article.getCreateTime())
                                    .author(article.getAuthor())
                                    .articleId(ref.getId())
                                    .authorId(ref.getUserName())
                                    .readCount(article.getReadCount())
                                    .tags(article.getTags())
                                    .copyright(article.getCopyright())
                                    .content(article.getContent())
                                    .build();
                                articleRepos.save(articleEntity);
                            } catch (Exception e) {
                                // save log to Mongodb
                                logRepos.save(LogEntity.builder()
                                    .timestamp(DateHelper.now())
                                    .level(LogEntity.ERROR)
                                    .desc("Exception on getArticle(" + ref.getUrl() + ")")
                                    .detail(exceptionToString(e))
                                    .build());
                            }
                        }
                        shownOffset = refs.getShownOffset();
                        errorTime = 0;
                    } catch (Exception e) {
                        // save log to Mongodb
                        logRepos.save(LogEntity.builder()
                            .timestamp(DateHelper.now())
                            .level(LogEntity.ERROR)
                            .desc("Exception on getMoreArticle(" + category + ", " + shownOffset + ")")
                            .detail(exceptionToString(e))
                            .build());
                        errorTime++;
                    }
                }
            });
        }
    }

    private String exceptionToString(Exception e)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out));
        return out.toString();
    }

}