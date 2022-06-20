package net.artux.pdanetwork.communication.handlers;

import net.artux.pdanetwork.service.member.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


@Service
public class MessagesHandler extends SocketHandler {
  public MessagesHandler(UserService userService) {
    super(userService);
  }

  @Override
  public void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage) {

  }
/*
  private final HashMap<Integer, List<WebSocketSession>> conversations = new HashMap<>();

  private final MongoMessages mongoMessages;
  private final MongoUsers mongoUsers;
  private final DialogsHandler dialogsHandler;

  public MessagesHandler(MongoUsers mongoUsers, MemberService memberService, MongoMessages mongoMessages, DialogsHandler dialogsHandler) {
    super(memberService);
    this.mongoUsers = mongoUsers;
    this.mongoMessages = mongoMessages;
    this.dialogsHandler = dialogsHandler;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession userSession) {
    super.afterConnectionEstablished(userSession);
    Map<String, String> params = ServletHelper.splitQuery(userSession.getUri().getRawQuery());

    UserEntity userEntity = getMember(userSession);
    int pdaId = userEntity.getPdaId();
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
    updateDialog(userSession);
    int conversation = (int) userSession.getAttributes().get("conversation");
    UserMessage userMessage = new UserMessage(conversation, (UserEntity) userSession.getAttributes().get("user"), webSocketMessage.getPayload().toString());

    for (WebSocketSession session : conversations.get(conversation)) {
      sendObject(session,userMessage);
    }

    mongoMessages.addMessageToConversation(conversation, userMessage);
    dialogsHandler.sendUpdates(mongoMessages.getConversation(conversation), userMessage);
  }

  private void addToConversation(int conversationId, WebSocketSession session){
    if (!conversations.containsKey(conversationId)) {
      List<WebSocketSession> sessions = new ArrayList<>();
      sessions.add(session);
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

  private void updateDialog(WebSocketSession userSession){
    Conversation conversation;
    if (userSession.getAttributes().get("first") != null &&
            (Boolean) userSession.getAttributes().get("first")) {
      //add new conv
      int pdaId = (int) userSession.getAttributes().get("pda");
      int id = (int) userSession.getAttributes().get("to");

      conversation = mongoMessages.newConversation(pdaId, id);
      addToConversation(conversation.getCid(), userSession);
    } else {
      //up conv
      conversation = mongoMessages
              .getConversation((int) userSession.getAttributes().get("conversation"));

    }
    for (int ids : conversation.allMembers()) {
      mongoUsers.updateDialog(ids, conversation.cid);
    }
  }*/

}
