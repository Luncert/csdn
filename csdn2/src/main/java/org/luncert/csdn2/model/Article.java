package org.luncert.csdn2.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Article
{

    String title;
    String createTime;
    String author;
    int readCount;
    String[] tags;
    String copyright;
    String content;

}