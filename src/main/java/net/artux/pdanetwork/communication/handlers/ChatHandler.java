package net.artux.pdanetwork.communication.handlers;

import net.artux.pdanetwork.communication.chat.BadWordsFilter;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.models.communication.MessageEntity;
import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.swing.*;
import java.util.HashMap;

@Service
public class ChatHandler extends SocketHandler {

  private static final LimitedArrayList<MessageEntity> lastMessages = new LimitedArrayList<>(150);
  private static final HashMap<Long, String> banMap = new HashMap<>();

  private static boolean updateMessages = false;

  public ChatHandler(UserService userService) {
    super(userService);
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
    MessageEntity messageEntity = getMessage(userSession, webSocketMessage);
    if(BadWordsFilter.contains(messageEntity)) {
      sendSystemMessage(userSession, "Мат в общих чатах запрещен.");
      return;
      //ServletContext.log("Bad word detected, pdaId: " + userMessage.pdaId + ", message: " + userMessage.message);
    } else if (banMap.containsKey(messageEntity.getAuthor().getId())){
      sendSystemMessage(userSession, "Вы заблокированы на час.");
      return;
    }
    lastMessages.add(messageEntity);
    for (WebSocketSession session : getSessions()) {
      if (updateMessages) {
        sendObject(session, lastMessages);
        updateMessages = false;
      } else sendObject(session, messageEntity);
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
