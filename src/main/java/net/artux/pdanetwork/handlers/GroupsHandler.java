package net.artux.pdanetwork.handlers;

import net.artux.pdanetwork.models.UserEntity;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Service
public class GroupsHandler extends SocketHandler {

  private HashMap <Integer, LimitedArrayList<UserMessage>> lastMessages = initLastMessages();
  private static final int limit = 150;

  private HashMap<Integer, LimitedArrayList<UserMessage>> initLastMessages() {
    HashMap<Integer, LimitedArrayList<UserMessage>> lastMessages = new HashMap<>();

    lastMessages.put(1, new LimitedArrayList<>(limit));
    lastMessages.put(2, new LimitedArrayList<>(limit));
    lastMessages.put(3, new LimitedArrayList<>(limit));
    lastMessages.put(4, new LimitedArrayList<>(limit));
    lastMessages.put(5, new LimitedArrayList<>(limit));
    lastMessages.put(6, new LimitedArrayList<>(limit));

    return lastMessages;
  }

  public GroupsHandler(MemberService memberService) {
    super(memberService);
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession userSession){
    super.afterConnectionEstablished(userSession);

    UserEntity userEntity = getMember(userSession);
    int group = userEntity.getGroup();
    if (group != 0) {
        sendObject(userSession, lastMessages.get(group));
    } else {
      sendSystemMessage(userSession, "Ваш PDA не подключен ни к одной из групп");
    }
  }

  @Override
  public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
    int group = getMember(userSession).getGroup();
    UserMessage userMessage = getMessage(userSession, webSocketMessage);
    lastMessages.get(group).add(userMessage);

    for (WebSocketSession session : getSessions())
      if (getMember(session).getGroup() == group)
        sendObject(session, userMessage);
  }

}
