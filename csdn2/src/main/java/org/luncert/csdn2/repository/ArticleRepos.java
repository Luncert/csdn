package org.luncert.csdn2.repository;

import org.bson.types.ObjectId;
import org.luncert.csdn2.model.ArticleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepos extends MongoRepository<ArticleEntity, ObjectId>
{

    ArticleEntity findOneByTitle(String title);

}