package net.artux.pdanetwork.configuration;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.handlers.ChatHandler;
import net.artux.pdanetwork.handlers.DialogsHandler;
import net.artux.pdanetwork.handlers.GroupsHandler;
import net.artux.pdanetwork.handlers.MessagesHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

  private final ChatHandler chatHandler;
  private final MessagesHandler messagesHandler;
  private final DialogsHandler dialogsHandler;
  private final GroupsHandler groupsHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
    webSocketHandlerRegistry
            .addHandler(chatHandler, "/chat")
            .addHandler(messagesHandler, "/dialog")
            .addHandler(dialogsHandler, "/dialogs")
            .addHandler(groupsHandler, "/groups")
            .setAllowedOriginPatterns("*");
  }

}
