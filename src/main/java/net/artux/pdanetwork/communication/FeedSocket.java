package net.artux.pdanetwork.communication;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.chat.BadWordsFilter;
import net.artux.pdanetwork.communication.chat.configurators.SocketConfigurator;
import net.artux.pdanetwork.communication.model.LimitedArrayList;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.communication.utilities.model.Comment;
import net.artux.pdanetwork.communication.utilities.model.FeedMessage;
import net.artux.pdanetwork.utills.ServletContext;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/feed/{token}/{load}", configurator = SocketConfigurator.class)
public class FeedSocket {

    private final LimitedArrayList<FeedMessage> waitList = new LimitedArrayList<>(150);
    private final LimitedArrayList<FeedMessage> feed = new LimitedArrayList<>(150);
    private final Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config, @PathParam("token") String token, @PathParam("load") String load) throws IOException {
        if (token.equals("*"))
            token = (String) config.getUserProperties().get("t");
        Member member = ServletContext.mongoUsers.getByToken(token);
        if (member != null) {
            userSession.getUserProperties().put("m", member);
            if (!load.equals("*"))
                for (FeedMessage feedMessage : feed)
                    userSession.getAsyncRemote().sendText(gson.toJson(feedMessage));
        } else {
            userSession.close();
        }
    }

    private void sendMessage(Session userSession, String msg) {
        userSession.getAsyncRemote().sendText(
                UserMessage.getSystemMessage("ru", msg).toString());
    }

    public void applyPost(FeedMessage message) {
        waitList.remove(message);
        message.setId(feed.size() + 1);
        feed.add(message);
    }

    public LimitedArrayList<FeedMessage> getWaitList() {
        return waitList;
    }

    @OnClose
    public void onClose(Session userSession) {
        userSession.getAsyncRemote();
    }

    @OnMessage
    public void onMessage(String message, Session userSession) throws IllegalStateException, JsonSyntaxException {
        Action action = gson.fromJson(message, Action.class);
        switch (action.getAction()) {
            case "post":
                waitList.add(gson.fromJson(action.getData(), FeedMessage.class));
                break;
            case "comment":
                Comment comment = gson.fromJson(action.getData(), Comment.class);
                comment.setPdaId(((Member) userSession.getUserProperties().get("m")).getPdaId());
                for (FeedMessage feedMessage : feed) {
                    if (!BadWordsFilter.contains(message)) {
                        if (comment.getPostId() == feedMessage.getId()) {
                            feedMessage.addComment(comment);
                            for (Session session : userSession.getOpenSessions()) {
                                session.getAsyncRemote().sendText(message);
                            }
                        }
                    } else {
                        // send error // sendMessage(userSession,"Мат в общих чатах запрещен.");
                    }

                }
                break;
        }
    }

    @OnError
    public void onError(Throwable thr) {
        ServletContext.error("Error at feed socket", thr);
        thr.printStackTrace();
    }

}