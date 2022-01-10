package net.artux.pdanetwork.handlers;

import net.artux.pdanetwork.models.Member;
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
    Member member = getMember(userSession);
    sessions.put(member.getPdaId(), userSession);
    sendObject(userSession, getDialogs(member));
  }

  private List<DialogResponse> getDialogs(Member member){
    List<DialogResponse> response = new ArrayList<>();
    int pda = member.getPdaId();
    for (int id : member.getDialogs()) {
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
    Member member = getMember(userSession);
    //update conversation with new title, members, owners
    ConversationRequest request = get(ConversationRequest.class, webSocketMessage);
    if (request.cid == 0){
      if (member.subs.containsAll(request.members))
        mongoMessages.newConversation(member.getPdaId(), request);
      else
        sendError(userSession, "Участники должны находится у вас в друзьях");
    }else{
      if (mongoMessages.getConversation(request.cid).owners.contains(member.getPdaId()))
        if (member.subs.containsAll(request.members))
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
