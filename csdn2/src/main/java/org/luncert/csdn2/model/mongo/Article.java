package org.luncert.csdn2.model.mongo;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document("Article")
public class Article implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    ObjectId id;
    
    String title;
    String createTime;
    String author;
    String readCount;
    String[] tags;
    String copyright;
    String content;

    String avatar; // 作者头像

    String cacheTime; // 1550985580
    String category; // 总是null
    String categoryId; // 总是none
    String comments; // 评论
    String commits; // 0
    String desc; // 
    String downs; // 0
    String articleId; // 文章id 8554465
    String isExpert; // 0
    String qualityScore; // null
    String shownOffset; // 1550985574986643
    String shownTime; // 1550985574
    String sourceType; // null
    String strategy; // 其他
    String strategyId; // none
    String subTitle; // eg：u014381710的专栏
    String summary; // 比desc更短一点
    String type; // 文章类型
    String url; // 文章url
    String userName; // 作者id
    String userUrl; // 作者主页url

}