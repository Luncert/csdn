package org.luncert.csdn2.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.luncert.csdn2.model.mongo.ArticleEntity;
import org.luncert.csdn2.model.normal.Article;
import org.luncert.csdn2.model.normal.ArticleRef;
import org.luncert.csdn2.model.normal.Category;
import org.luncert.csdn2.repository.mongo.ArticleRepos;
import org.luncert.csdn2.util.Request;
import org.luncert.csdn2.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.ToString;

@Service
public class ArticleService
{
    private static final String HEADER_USER_AGENT = "User-Agent";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36";

    private static final String API_MORE_ARTICLE = "https://www.csdn.net/api/articles?type=more&category=%s&shown_offset=%s";

    public Article getArticle(String url) throws IOException
    {
        Document doc = Jsoup.connect(url)
            .header(HEADER_USER_AGENT, USER_AGENT).get();
        
        String title = doc.selectFirst(".article-title-box")
            .selectFirst(".title-article").text();

        Element info = doc.selectFirst(".article-info-box")
            .selectFirst(".article-bar-top");

        String createTime = info.selectFirst(".time").text();
        String author = info.selectFirst(".follow-nickName").text();
        int readCount = Integer.valueOf(info.selectFirst(".read-count")
            .text().replace("阅读数：", ""));
        String[] tags = null;
        Element tagsBox = info.selectFirst(".tags-box");
        if (tagsBox != null)
            tags = tagsBox.text().split(" ");
        String copyright = null;
        Element copyrightElem = info.selectFirst(".article-copyright");
        if (copyrightElem != null)
            copyright = copyrightElem.text();
        
        String content = doc.selectFirst("#content_views").html();

        return Article.builder()
            .title(title)
            .createTime(createTime)
            .author(author)
            .readCount(Integer.valueOf(readCount))
            .tags(tags)
            .copyright(copyright)
            .content(content)
            .build();
    }

    public ArticleRefs getMoreArticle(Category category, String shownOffset) throws Exception
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

        List<ArticleRef> refs = new ArrayList<>();
        for (Object obj : jsonData.getJSONArray("articles"))
        {
            jsonData = (JSONObject) obj;
            refs.add(new ArticleRef(jsonData));
        }
        return new ArticleRefs(refs, jsonData.getString("shown_offset"));
    }

    @ToString
    @Getter
    public static class ArticleRefs
    {
        private List<ArticleRef> refs;
        private String shownOffset;
        private ArticleRefs(List<ArticleRef> refs, String shownOffset)
        {
            this.refs = refs;
            this.shownOffset = shownOffset;
        }
    }
    
    public static final String ON_SAVE_ARTICLE_ENTITY = "OnSaveArticleEntity";

    @Autowired
    private EventService eventService;

    @Autowired
    private ArticleRepos articleRepos;

    public void save(ArticleEntity articleEntity)
    {
        eventService.submit(ON_SAVE_ARTICLE_ENTITY, articleEntity);
        articleRepos.save(articleEntity);
    }

}