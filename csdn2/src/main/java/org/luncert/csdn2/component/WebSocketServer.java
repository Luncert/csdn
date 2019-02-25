package org.luncert.csdn2.component;

import java.io.IOException;
import java.util.EventListener;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;

import org.luncert.csdn2.aspect.ArticleReposAspect;
import org.luncert.csdn2.aspect.LogReposAspect;
import org.luncert.csdn2.model.mongo.ArticleEntity;
import org.luncert.csdn2.model.mongo.LogEntity;
import org.luncert.csdn2.service.EventService;
import org.luncert.csdn2.service.LogService;
import org.luncert.csdn2.util.NormalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// https://blog.csdn.net/zhangdehua678/article/details/78913839/ 
// 如何实现订阅模式：使用aop或者其他，在save entity的时候，trigger事件，通知到WebSocketServer，然后分发给订阅了的客户端，这意味着还要用session
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer
{

    @Autowired
    private EventService eventService;

    @Autowired
    private LogService logService;

    private Session session;

    private EventListener logEntityListener, articleEntityListener;

    class LogEntityListener implements EventListener
    {

        public void onSave(LogEntity logEntity)
        {
            sendMessage(JSON.toJSONString(logEntity));
        }

    }

    class ArticleEntityListener implements EventListener
    {

        public void onSave(ArticleEntity articleEntity)
        {
            sendMessage(JSON.toJSONString(articleEntity));
        }

    }

    private void sendMessage(String message)
    {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logService.error("exception on WebSocketServer.sendMessage",
                NormalUtil.throwableToString(e));
        }
    }

    @OnOpen
    public void onOpen(Session session)
    {
        logEntityListener = new LogEntityListener();
        articleEntityListener = new ArticleEntityListener();
        this.session = session;
        eventService.register(LogReposAspect.ON_SAVE_LOG_ENTITY, logEntityListener);
        eventService.register(ArticleReposAspect.ON_SAVE_ARTICLE_ENTITY, articleEntityListener);
    }

    @OnClose
    public void onClose()
    {
        this.session = null;
        eventService.unregister(LogReposAspect.ON_SAVE_LOG_ENTITY, logEntityListener);
        eventService.unregister(ArticleReposAspect.ON_SAVE_ARTICLE_ENTITY, articleEntityListener);
    }

}