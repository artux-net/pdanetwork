package net.artux.pdanetwork.communication.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.communication.MessageEntity;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.service.member.MemberService;
import net.artux.pdanetwork.service.util.Utils;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


@RequiredArgsConstructor
public abstract class SocketHandler implements WebSocketHandler {

  private final MemberService memberService;
  private static final List<WebSocketSession> sessionList = new ArrayList<>();
  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  private static final String USER = "user";

  @Override
  public void afterConnectionEstablished(WebSocketSession userSession){
    userSession.getAttributes().put(USER, getMember(userSession));
  }

  protected void accept(WebSocketSession userSession){
    sessionList.add(userSession);
  }

  protected void reject(WebSocketSession userSession, String message){
    if(!Utils.isEmpty(message) && userSession.isOpen()){
      try {
        sendSystemMessage(userSession, message);
        userSession.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    sessionList.remove(userSession);
  }

  protected static List<WebSocketSession> getSessions() {
    return sessionList;
  }

  protected void sendSystemMessageByPdaId(Integer pdaId, String msg){
    Optional<WebSocketSession> webSocketSessionOptional = sessionList.stream().filter(new Predicate<WebSocketSession>() {
      @Override
      public boolean test(WebSocketSession webSocketSession) {
        return getMember(webSocketSession).getPdaId() == pdaId;
      }
    }).findFirst();
    if (webSocketSessionOptional.isPresent())
      sendSystemMessage(webSocketSessionOptional.get(), msg);

  }

  protected UserEntity getMember(WebSocketSession userSession){
    if (userSession.getAttributes().containsKey(USER))
      return (UserEntity) userSession.getAttributes().get(USER);
    String base64 = userSession.getHandshakeHeaders().getFirst("authorization");
    UserEntity userEntity = memberService.getMember(base64);
    if (userEntity == null) {
      reject(userSession, "Авторизация не пройдена");
      throw new RuntimeException("Авторизация не пройдена");
    }
    else {
      userSession.getAttributes().put(USER, userEntity);
      return userEntity;
    }
  }

  protected void sendObject(WebSocketSession userSession, Object object){
    if (userSession!=null && userSession.isOpen())
      try {
        userSession.sendMessage(new TextMessage(gson.toJson(object)));
      }catch (Exception e){

      }
    else reject(userSession, "inactivity");
  }

  protected void sendSystemMessage(WebSocketSession userSession, String msg){
    sendObject(userSession, MessageEntity.getSystemMessage("ru", msg));
  }

  protected <T> T get(Class<T> clazz, WebSocketMessage<?> message){
    return gson.fromJson(message.getPayload().toString(), clazz);
  }

  protected MessageEntity getMessage(WebSocketSession userSession, WebSocketMessage<?> message){
    return new MessageEntity(getMember(userSession), message.getPayload().toString());
  }

  @Override
  public abstract void handleMessage(WebSocketSession userSession, WebSocketMessage<?> webSocketMessage);

  @Override
  public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
    reject(webSocketSession, null);
  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }

  @Override
  public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

  }
}
