package org.luncert.csdn2.util;

import com.alibaba.fastjson.JSON;

import lombok.Data;

@Data
public class Result
{
    public enum Status
    {
        OK
    }

    private int statusCode;
    private String desc;
    private String msg;

    public Result(Status status)
    {
        this(status, null);
    }

    public Result(Status status, String msg)
    {
        statusCode = status.ordinal();
        desc = status.name();
        this.msg = msg;
    }

    public String toString()
    {
        return JSON.toJSONString(this);
    }
    
}