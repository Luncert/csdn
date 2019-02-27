package org.luncert.csdn2.repository.mongo;

import org.bson.types.ObjectId;
import org.luncert.csdn2.model.mongo.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepos extends MongoRepository<Article, ObjectId>
{

    Article findOneByTitle(String title);

    Article findOneByArticleId(String articleId);

}