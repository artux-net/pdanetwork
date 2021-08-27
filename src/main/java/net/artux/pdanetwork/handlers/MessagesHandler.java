package net.artux.pdanetwork.handlers;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.model.Conversation;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.communication.utilities.MongoMessages;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

import static net.artux.pdanetwork.utills.ServletContext.mongoUsers;

@Service
public class MessagesHandler extends SocketHandler {

  private final HashMap<Integer, List<WebSocketSession>> conversations = new HashMap<>();

  private final MongoMessages mongoMessages;

  public MessagesHandler(MemberService memberService, MongoMessages mongoMessages) {
    super(memberService);
    this.mongoMessages = mongoMessages;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession userSession) {
    super.afterConnectionEstablished(userSession);
    Map<String, String> params = ServletHelper.splitQuery(userSession.getUri().getRawQuery());
    Member member = getMember(userSession);
    int pdaId = member.getPdaId();

    //ServletContext.log("Opened session(" + userSession.getId() + ") for pda: " + pdaId + ", query: " + userSession.getQueryString());

    if (params.containsKey("to")) {
        // private message
        int id = Integer.parseInt(params.get("to"));
        int conversationId = mongoMessages.getDialogID(pdaId, id);
        if (conversationId != 0) {
          addToConversation(conversationId, userSession);
        } else {
          //conv does not exist
          userSession.getAttributes().put("first", true);
          userSession.getAttributes().put("pda", pdaId);
          userSession.getAttributes().put("to", id);
        }
      } else if (params.containsKey("c")) {
        // group message
        int conversationId = Integer.parseInt(params.get("c"));
        if (mongoMessages.hasConversation(conversationId)
                && mongoMessages.conversationHas(conversationId, pdaId)) {
          addToConversation(conversationId, userSession);
        } else {
          reject(userSession, "Неправильный запрос");
          //ServletContext.log("Can not connect pdaId " + pdaId + " to conversation " + conversationId);
          Conversation conversation = mongoMessages.getConversation(conversationId);
          if (conversation != null) {
            //ServletContext.log(Arrays.toString(conversation.allMembers().toArray()) + " does not have this id");
          }
            //else ServletContext.log("This conversation does not exist");


        }
      } else {
        reject(userSession, "Неправильный запрос");
        //ServletContext.log("Session does not have query");
      }
  }

  @Override
  public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {
    int conversation = (int) userSession.getAttributes().get("conversation");
    UserMessage userMessage = new UserMessage(conversation, (Member) userSession.getAttributes().get("user"), webSocketMessage.getPayload().toString());

    for (WebSocketSession session : conversations.get(conversation)) {
      sendObject(session,userMessage);
    }

    mongoMessages.addMessageToConversation(conversation, userMessage);
    updateDialog(userSession, userMessage);
  }

  private void addToConversation(int conversationId, WebSocketSession session){
    if (!conversations.containsKey(conversationId)) {
      List<WebSocketSession> sessions = new ArrayList<>();
      accept(session);
      conversations.put(conversationId, sessions);
    } else {
      conversations.get(conversationId)
              .add(session);
    }
    session.getAttributes().put("conversation", conversationId);

    List<UserMessage> messages = mongoMessages.getLastMessages(conversationId, 30, 0);
    sendObject(session, messages);
  }


  @Override
  public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
    super.afterConnectionClosed(webSocketSession, closeStatus);
    if (webSocketSession.getAttributes().get("conversation") != null) {
      int conversation = (int) webSocketSession.getAttributes().get("conversation");

      conversations.get(conversation).remove(webSocketSession);
      if (conversations.get(conversation).size() == 0)
        conversations.remove(conversation);
    }
    //ServletContext.log("Closed session: " + userSession.getId());
  }

  private void updateDialog(WebSocketSession userSession, UserMessage message){
    if (userSession.getAttributes().get("first") != null &&
            (Boolean) userSession.getAttributes().get("first")) {
      //add new conv
      int pdaId = (int) userSession.getAttributes().get("pda");
      int id = (int) userSession.getAttributes().get("to");

      Conversation conversation = ServletContext.mongoMessages.newConversation(pdaId, id);
      addToConversation(conversation.getCid(), userSession);
      for (int ids : conversation.allMembers()) {
        mongoUsers.updateDialog(pdaId, conversation.cid);
        //SocketConfigurator.dialogsSocket.sendUpdate(ids, gson.toJson(message));
      }
    } else {
      //up conv
      Conversation conversation = mongoMessages
              .getConversation((int) userSession.getAttributes().get("conversation"));
      for (int ids : conversation.allMembers()) {
        mongoUsers.updateDialog(ids, conversation.cid);
        //SocketConfigurator.dialogsSocket.sendUpdate(ids, gson.toJson(message));
      }
    }
  }

}
