package org.luncert.csdn2.config;

import org.luncert.csdn2.component.WebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer
{
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(websocketHandler(), "/websocket").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler websocketHandler()
    {
        return new WebSocketHandler();
    }

}