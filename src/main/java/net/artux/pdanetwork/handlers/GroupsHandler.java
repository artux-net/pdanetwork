package net.artux.pdanetwork.handlers;

import net.artux.pdanetwork.models.user.Group;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Service
public class GroupsHandler extends SocketHandler {

  private HashMap <Group, LimitedArrayList<UserMessage>> lastMessages = initLastMessages();
  private static final int limit = 150;

  private HashMap<Group, LimitedArrayList<UserMessage>> initLastMessages() {
    HashMap<Group, LimitedArrayList<UserMessage>> lastMessages = new HashMap<>();

    lastMessages.put(Group.LONERS, new LimitedArrayList<>(limit));
    lastMessages.put(Group.BANDITS, new LimitedArrayList<>(limit));
    lastMessages.put(Group.DUTY, new LimitedArrayList<>(limit));
    lastMessages.put(Group.CLEAR_SKY, new LimitedArrayList<>(limit));
    lastMessages.put(Group.LIBERTY, new LimitedArrayList<>(limit));
    lastMessages.put(Group.MILITARY, new LimitedArrayList<>(limit));

    return lastMessages;
  }

  public GroupsHandler(MemberService memberService) {
    super(memberService);
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession userSession){
    super.afterConnectionEstablished(userSession);

    UserEntity userEntity = getMember(userSession);
    Group group = userEntity.getGroup();
    if (group != Group.LONERS) {
        sendObject(userSession, lastMessages.get(group));
    } else {
      sendSystemMessage(userSession, "Ваш PDA не подключен ни к одной из групп");
    }
  }

  @Override
  public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
    Group group = getMember(userSession).getGroup();
    UserMessage userMessage = getMessage(userSession, webSocketMessage);
    lastMessages.get(group).add(userMessage);

    for (WebSocketSession session : getSessions())
      if (getMember(session).getGroup() == group)
        sendObject(session, userMessage);
  }

}
