package org.luncert.csdn2.component;

import java.io.IOException;
import java.util.EventListener;

import javax.websocket.OnClose;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.luncert.csdn2.aspect.ArticleReposAspect;
import org.luncert.csdn2.aspect.LogServiceAspect;
import org.luncert.csdn2.model.mongo.ArticleEntity;
import org.luncert.csdn2.model.mongo.LogEntity;
import org.luncert.csdn2.service.EventService;
import org.luncert.csdn2.service.LogService;
import org.luncert.csdn2.util.NormalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// https://blog.csdn.net/zhangdehua678/article/details/78913839/
// https://blog.csdn.net/tornadojava/article/details/78781474
// 如何实现订阅模式：使用aop或者其他，在save entity的时候，trigger事件，通知到WebSocketServer，然后分发给订阅了的客户端，这意味着还要用session
@Component
public class WebSocketHandler extends TextWebSocketHandler
{

    @Autowired
    private EventService eventService;

    @Autowired
    private LogService logService;

    private WebSocketSession session;

    private EventListener logEntityListener, articleEntityListener;

    class LogEntityListener implements EventListener
    {

        public void onSave(LogEntity logEntity)
        {
            System.out.println(222);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "Article");
            jsonObject.put("content", JSON.toJSON(logEntity));
            sendMessage(jsonObject.toJSONString());
        }

    }

    class ArticleEntityListener implements EventListener
    {

        public void onSave(ArticleEntity articleEntity)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "Log");
            jsonObject.put("content", JSON.toJSON(articleEntity));
            sendMessage(jsonObject.toJSONString());
        }

    }

    private void sendMessage(String message)
    {
        try {
            this.session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            logService.error("exception on WebSocketServer.sendMessage",
                NormalUtil.throwableToString(e));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        super.afterConnectionEstablished(session);
        logEntityListener = new LogEntityListener();
        articleEntityListener = new ArticleEntityListener();
        this.session = session;
        eventService.register(LogServiceAspect.ON_SAVE_LOG_ENTITY, logEntityListener);
        eventService.register(ArticleReposAspect.ON_SAVE_ARTICLE_ENTITY, articleEntityListener);
    }

    @OnClose
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        super.afterConnectionClosed(session, status);
        eventService.unregister(LogServiceAspect.ON_SAVE_LOG_ENTITY, logEntityListener);
        eventService.unregister(ArticleReposAspect.ON_SAVE_ARTICLE_ENTITY, articleEntityListener);
    }

}