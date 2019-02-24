package org.luncert.csdn2.model;

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

    public static final String ERROR = "ERROR";
    public static final String WARN = "WARN";
    public static final String INFO = "INFO";

    private static final long serialVersionUID = 1L;
    
    @Id
    private ObjectId id;

    private String timestamp;
    private String level;
    private String desc;
    private String detail;

}