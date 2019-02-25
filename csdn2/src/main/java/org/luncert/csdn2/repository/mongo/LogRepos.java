package org.luncert.csdn2.repository.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.luncert.csdn2.model.mongo.LogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepos extends MongoRepository<LogEntity, ObjectId>
{

    /**
     * 查询出timestamp大于startTimestamp的数据
     */
    List<LogEntity> findByTimestampAfter(String startTimestamp);

}