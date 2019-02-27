package org.luncert.csdn2.service;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.luncert.csdn2.model.mongo.Article;
import org.luncert.csdn2.model.normal.Category;
import org.luncert.csdn2.repository.mongo.ArticleRepos;
import org.luncert.csdn2.util.Request;
import org.luncert.csdn2.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService
{
    
    public static final String ON_SAVE_ARTICLE_ENTITY = "OnSaveArticleEntity";

    @Autowired
    private EventService eventService;

    @Autowired
    private ArticleRepos articleRepos;
    
    private static final String HEADER_USER_AGENT = "User-Agent";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36";

    private static final String API_MORE_ARTICLE = "https://www.csdn.net/api/articles?type=more&category=%s&shown_offset=%s";

    /**
     * 根据Article基本信息，获取Article的详细信息，如文章正文
     */
    private void getArticleContent(Article article) throws IOException
    {
        Document doc = Jsoup.connect(article.getUrl())
            .header(HEADER_USER_AGENT, USER_AGENT).get();

        Element info = doc.selectFirst(".article-info-box")
            .selectFirst(".article-bar-top");

        /*
        String title = doc.selectFirst(".article-title-box")
            .selectFirst(".title-article").text();
            String author = info.selectFirst(".follow-nickName").text();
            int readCount = Integer.valueOf(info.selectFirst(".read-count")
            .text().replace("阅读数：", ""));
            String[] tags = null;
            Element tagsBox = info.selectFirst(".tags-box");
            if (tagsBox != null)
            tags = tagsBox.text().split(" ");
        */

        String createTime = info.selectFirst(".time").text();
        String copyright = null;
        Element copyrightElem = info.selectFirst(".article-copyright");
        if (copyrightElem != null)
            copyright = copyrightElem.text();
        
        String content = doc.selectFirst("#content_views").html();

        article.setContent(content);
        article.setCopyright(copyright);
        article.setCreateTime(createTime);
    }

    public String getMoreArticle(Category category, String shownOffset) throws Exception
    {
        if (shownOffset == null)
        {
            String url = String.format("https://www.csdn.net/nav/%s", category.getName());
            Document doc = Jsoup.connect(url)
                .header(HEADER_USER_AGENT, USER_AGENT).get();
            shownOffset = doc.
                selectFirst("#feedlist_id").attr("shown-offset");
        }

        String url = String.format(API_MORE_ARTICLE, category.getName(), shownOffset);
        Response rep = Request.url(url)
            .header(HEADER_USER_AGENT, USER_AGENT)
            .execute();
        assert(rep.statusCode() == 200);
        JSONObject jsonData = (JSONObject) JSON.parse(rep.text());
        assert(jsonData.getString("status").equals("true"));

        for (Object obj : jsonData.getJSONArray("articles"))
        {
            jsonData = (JSONObject) obj;
            Article article =  Article.builder()
                .avatar(jsonData.getString("avatar"))
                .cacheTime(jsonData.getString("cache_time"))
                .category(jsonData.getString("category"))
                .categoryId(jsonData.getString("category_id"))
                .comments(jsonData.getString("comments"))
                .commits(jsonData.getString("commits"))
                .desc(jsonData.getString("desc"))
                .downs(jsonData.getString("downs"))
                .articleId(jsonData.getString("id"))
                .isExpert(jsonData.getString("isexpert"))
                .author(jsonData.getString("nickname"))
                .qualityScore(jsonData.getString("quality_score"))
                .shownOffset(jsonData.getString("shown_offset"))
                .shownTime(jsonData.getString("shown_time"))
                .sourceType(jsonData.getString("sourcetype"))
                .strategy(jsonData.getString("strategy"))
                .strategyId(jsonData.getString("strategy_id"))
                .subTitle(jsonData.getString("sub_title"))
                .summary(jsonData.getString("summary"))
                .tags(jsonData.getString("tag").split(","))
                .title(jsonData.getString("title"))
                .type(jsonData.getString("type"))
                .url(jsonData.getString("url"))
                .userName(jsonData.getString("user_name"))
                .userUrl(jsonData.getString("user_url"))
                .readCount(jsonData.getString("views"))
                .build();

            getArticleContent(article);
            save(article);
        }
        return jsonData.getString("shown_offset");
    }

    private void save(Article article)
    {
        eventService.submit(ON_SAVE_ARTICLE_ENTITY, article);
        articleRepos.save(article);
    }

}