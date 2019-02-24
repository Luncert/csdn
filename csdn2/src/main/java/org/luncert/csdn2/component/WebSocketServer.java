package org.luncert.csdn2.component;

import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

// https://blog.csdn.net/zhangdehua678/article/details/78913839/ 
// 如何实现订阅模式：使用aop或者其他，在save entity的时候，trigger事件，通知到WebSocketServer，然后分发给订阅了的客户端，这意味着还要用session
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer
{

    @OnOpen
    public void onOpen()
    {

    }

}