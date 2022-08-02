package net.artux.pdanetwork.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.models.communication.LimitedLinkedList;
import net.artux.pdanetwork.models.communication.MessageDTO;
import net.artux.pdanetwork.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.swing.*;
import java.util.HashMap;

@Service
public class ChatHandler extends SocketHandler {

  private static final LimitedLinkedList<MessageDTO> lastMessages = new LimitedLinkedList<>(150);
  private static final HashMap<Long, String> banMap = new HashMap<>();

  private static boolean updateMessages = false;

  public ChatHandler(UserService userService, ObjectMapper objectMapper) {
    super(userService, objectMapper);
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession userSession){
    super.afterConnectionEstablished(userSession);

    accept(userSession);
    sendObject(userSession, lastMessages);
    //TODO block
    /*if (getMember(userSession).getBlocked() < 1) {

    } else {
      reject(userSession, "Вы были заблокированы за нарушение правил.");
    }*/
  }

  @Override
  public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage){
    MessageDTO message = getMessage(userSession, webSocketMessage);
    if(BadWordsFilter.contains(message.getContent())) {
      sendSystemMessage(userSession, "Мат в общих чатах запрещен.");
      return;
      //ServletContext.log("Bad word detected, pdaId: " + userMessage.pdaId + ", message: " + userMessage.message);
   /* } else if (banMap.containsKey(messageEntity.getAuthor().getId())){
      sendSystemMessage(userSession, "Вы заблокированы на час.");
      return;*/
    }
    lastMessages.add(message);
    for (WebSocketSession session : getSessions()) {
      if (updateMessages) {
        sendObject(session, lastMessages);
        updateMessages = false;
      } else sendObject(session, message);
    }
  }


  public void addToBanList(Long pdaId, String reason){
    banMap.put(pdaId, reason);
    //ServletContext.log("pdaId: " + pdaId + " banned for hour in general chat: " + reason);
    new Timer(60*60*1000, e -> banMap.remove(pdaId)).start();
  }

  public void removeMessage(Long time){
    lastMessages.removeIf(userMessage -> userMessage.getTimestamp() == time);
    updateMessages = true;
  }

}
