package org.luncert.csdn2.model.normal;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class ArticleRef
{

    String avatar; // 作者头像
    String cacheTime; // 1550985580
    String category; // 总是null
    String categoryId; // 总是none
    String comments; // 评论
    String commits; // 0
    String createdTime; // 创建时间
    String desc; // 
    String downs; // 0
    String id; // 文章id 8554465
    String isExpert; // 0
    String nickName; // 作者昵称
    String qualityScore; // null
    String shownOffset; // 1550985574986643
    String shownTime; // 1550985574
    String sourceType; // null
    String strategy; // 其他
    String strategyId; // none
    String subTitle; // eg：u014381710的专栏
    String summary; // 比desc更短一点
    String[] tag; // 标签
    String title; // 标题
    String type; // 文章类型
    String url; // 文章url
    String userName; // 作者id
    String userUrl; // 作者主页url
    String views; // 浏览量

    public ArticleRef(JSONObject json)
    {
        avatar = json.getString("avatar");
        cacheTime = json.getString("cache_time");
        category = json.getString("category");
        categoryId = json.getString("category_id");
        comments = json.getString("comments");
        commits = json.getString("commits");
        createdTime = json.getString("created_at");
        desc = json.getString("desc");
        downs = json.getString("downs");
        id = json.getString("id");
        isExpert = json.getString("isexpert");
        nickName = json.getString("nickname");
        qualityScore = json.getString("quality_score");
        shownOffset = json.getString("shown_offset");
        shownTime = json.getString("shown_time");
        sourceType = json.getString("sourcetype");
        strategy = json.getString("strategy");
        strategyId = json.getString("strategy_id");
        subTitle = json.getString("sub_title");
        summary = json.getString("summary");
        System.out.println(json.get("tag"));
        tag = json.getString("tag").split(",");
        title = json.getString("title");
        type = json.getString("type");
        url = json.getString("url");
        userName = json.getString("user_name");
        userUrl = json.getString("user_url");
        views = json.getString("views");
    }

}