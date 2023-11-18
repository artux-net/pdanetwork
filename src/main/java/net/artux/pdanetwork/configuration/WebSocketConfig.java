package net.artux.pdanetwork.configuration;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.configuration.handlers.ChatHandler;
import net.artux.pdanetwork.configuration.handlers.DialogsHandler;
import net.artux.pdanetwork.configuration.handlers.GroupsHandler;
import net.artux.pdanetwork.configuration.handlers.RPHandler;
import net.artux.pdanetwork.configuration.handlers.chat.DialogHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;
    private final DialogHandler dialogHandler;
    private final DialogsHandler dialogsHandler;
    private final GroupsHandler groupsHandler;
    private final RPHandler rpHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(chatHandler, "/chat")
                .addHandler(dialogHandler, "/dialog")
                .addHandler(dialogsHandler, "/dialogs")
                .addHandler(groupsHandler, "/groups")
                .addHandler(rpHandler, "/rp")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
    }

}
