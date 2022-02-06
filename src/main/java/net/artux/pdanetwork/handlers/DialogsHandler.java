package net.artux.pdanetwork.handlers;

import net.artux.pdanetwork.models.UserEntity;
import net.artux.pdanetwork.communication.model.*;
import net.artux.pdanetwork.communication.utilities.MongoMessages;
import net.artux.pdanetwork.models.Profile;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.service.profile.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DialogsHandler extends SocketHandler {

  private static HashMap<Integer, WebSocketSession> sessions = new HashMap<>();
  private final MongoMessages mongoMessages;
  private final ProfileService profileService;

  public DialogsHandler(MemberService memberService, MongoMessages mongoMessages, ProfileService profileService) {
    super(memberService);
    this.mongoMessages = mongoMessages;
    this.profileService = profileService;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession userSession){
    super.afterConnectionEstablished(userSession);
    UserEntity userEntity = getMember(userSession);
    sessions.put(userEntity.getPdaId(), userSession);
    sendObject(userSession, getDialogs(userEntity));
  }

  private List<DialogResponse> getDialogs(UserEntity userEntity){
    List<DialogResponse> response = new ArrayList<>();
    int pda = userEntity.getPdaId();
    for (int id : userEntity.getDialogs()) {
      Conversation conversation = mongoMessages.getConversation(id);

      if (conversation.allMembers().size() <= 2) {
        int anotherId = getAnotherId(conversation.allMembers(), pda);
        Profile profile = profileService.getProfile(anotherId);
        response.add(new DialogResponse(conversation, profile));
      } else {
        response.add(new DialogResponse(conversation));
      }
    }
    return response;
  }

  private int getAnotherId(List<Integer> ids, int pda){
    for (int i : ids){
      if(i!=pda){
        return i;
      }
    }
    return 0;
  }

  public void sendUpdates(Conversation conversation, UserMessage userMessage){
    for (int pdaId : conversation.allMembers()) {
      if (sessions.containsKey(pdaId)){
        sendObject(sessions.get(pdaId), userMessage);
      }
    }

  }


  @Override
  public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage){
    UserEntity userEntity = getMember(userSession);
    //update conversation with new title, members, owners
    ConversationRequest request = get(ConversationRequest.class, webSocketMessage);
    if (request.cid == 0){
      if (userEntity.subs.containsAll(request.members))
        mongoMessages.newConversation(userEntity.getPdaId(), request);
      else
        sendError(userSession, "Участники должны находится у вас в друзьях");
    }else{
      if (mongoMessages.getConversation(request.cid).owners.contains(userEntity.getPdaId()))
        if (userEntity.subs.containsAll(request.members))
          mongoMessages.updateConversation(request);
        else
          sendError(userSession, "Участники должны находится у вас в друзьях");
      else
        sendError(userSession, "Вы не владелец беседы");
    }
  }

  public void sendError(WebSocketSession session, String message){
    sendObject(session, new Status(false, message));
  }

}
