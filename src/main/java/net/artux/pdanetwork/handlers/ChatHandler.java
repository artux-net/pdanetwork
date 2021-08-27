package net.artux.pdanetwork.handlers;

import net.artux.pdanetwork.communication.chat.BadWordsFilter;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.swing.*;
import java.util.HashMap;

@Service
public class ChatHandler extends SocketHandler {

  private static final LimitedArrayList<UserMessage> lastMessages = new LimitedArrayList<>(150);
  private static final HashMap<Integer, String> banMap = new HashMap<>();

  private static boolean updateMessages = false;

  public ChatHandler(MemberService memberService) {
    super(memberService);
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession userSession){
    super.afterConnectionEstablished(userSession);

    if (getMember(userSession).getBlocked() < 1) {
      accept(userSession);
      sendObject(userSession, lastMessages);
    } else {
      reject(userSession, "Вы были заблокированы за нарушение правил.");
    }
  }

  @Override
  public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage){
    UserMessage userMessage = getMessage(userSession, webSocketMessage);
    if(BadWordsFilter.contains(userMessage)) {
      sendSystemMessage(userSession, "Мат в общих чатах запрещен.");
      return;
      //ServletContext.log("Bad word detected, pdaId: " + userMessage.pdaId + ", message: " + userMessage.message);
    } else if (banMap.containsKey(userMessage.pdaId)){
      sendSystemMessage(userSession, "Вы заблокированы на час.");
      return;
    }
    lastMessages.add(userMessage);
    for (WebSocketSession session : getSessions()) {
      if (updateMessages) {
        sendObject(session, lastMessages);
        updateMessages = false;
      } else sendObject(session, userMessage);
    }
  }


  public void addToBanList(Integer pdaId, String reason){
    banMap.put(pdaId, reason);
    //ServletContext.log("pdaId: " + pdaId + " banned for hour in general chat: " + reason);
    new Timer(60*60*1000, e -> banMap.remove(pdaId)).start();
  }

  public void removeMessage(Long time){
    lastMessages.removeIf(userMessage -> userMessage.time == time);
    updateMessages = true;
  }

}
