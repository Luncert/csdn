package org.luncert.csdn2.model.mongo;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "articles")
@Data
@Builder
public class ArticleEntity implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    private ObjectId id;
    
    private String title;
    private String createTime;
    private String author;
    private String articleId;
    private String authorId;
    private int readCount;
    private String[] tags;
    private String copyright;
    private String content;

}