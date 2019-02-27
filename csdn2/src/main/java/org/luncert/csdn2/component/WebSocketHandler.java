package org.luncert.csdn2.component;

import java.io.IOException;
import java.util.EventListener;

import javax.websocket.OnClose;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.luncert.csdn2.model.mongo.Article;
import org.luncert.csdn2.model.mongo.Log;
import org.luncert.csdn2.service.ArticleService;
import org.luncert.csdn2.service.EventService;
import org.luncert.csdn2.service.LogService;
import org.luncert.csdn2.service.SpiderService;
import org.luncert.csdn2.service.SpiderService.Status;
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

    private EventListener logListener, articleListener, spiderStatusListener;

    public class LogListener implements EventListener
    {

        public void onSave(Log log)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "Log");
            jsonObject.put("content", JSON.toJSON(log));
            sendMessage(jsonObject);
        }

    }

    public class ArticleListener implements EventListener
    {

        public void onSave(Article article)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "Article");
            jsonObject.put("content", JSON.toJSON(article));
            sendMessage(jsonObject);
        }

    }

    public class SpiderStatusListener implements EventListener
    {

        public void onChange(Status spiderStatus)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "Spider.Status");
            jsonObject.put("content", spiderStatus.name());
            sendMessage(jsonObject);
        }
    }

    private void sendMessage(JSONObject message)
    {
        try {
            this.session.sendMessage(new TextMessage(message.toJSONString()));
        } catch (IOException e) {
            logService.error("exception on WebSocketServer.sendMessage",
                NormalUtil.throwableToString(e));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        super.afterConnectionEstablished(session);
        this.session = session;

        logListener = new LogListener();
        eventService.register(LogService.ON_SAVE_LOG_ENTITY, logListener);

        articleListener = new ArticleListener();
        eventService.register(ArticleService.ON_SAVE_ARTICLE_ENTITY, articleListener);

        spiderStatusListener = new SpiderStatusListener();
        eventService.register(SpiderService.ON_CHANGE_STATUS, spiderStatusListener);
    }

    @OnClose
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        super.afterConnectionClosed(session, status);
        eventService.unregister(LogService.ON_SAVE_LOG_ENTITY, logListener);
        eventService.unregister(ArticleService.ON_SAVE_ARTICLE_ENTITY, articleListener);
        eventService.unregister(SpiderService.ON_CHANGE_STATUS, spiderStatusListener);
    }

}