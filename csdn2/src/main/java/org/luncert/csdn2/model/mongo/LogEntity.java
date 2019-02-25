package org.luncert.csdn2.model.mongo;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document("logs")
@Data
@Builder
public class LogEntity implements Serializable
{

    public enum LogLevel
    {
        Error, Warn, Info;

        public String getName()
        {
            return this.toString().toLowerCase();
        }
    }

    private static final long serialVersionUID = 1L;
    
    @Id
    private ObjectId id;

    private String timestamp;
    private String level;
    private String desc;
    private String detail;

}