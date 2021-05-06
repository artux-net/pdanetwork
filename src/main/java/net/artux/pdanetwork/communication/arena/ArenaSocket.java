package net.artux.pdanetwork.communication.arena;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.communication.Action;
import net.artux.pdanetwork.communication.chat.configurators.SocketConfigurator;
import net.artux.pdanetwork.utills.ServletContext;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint(value = "/arena/{token}", configurator = SocketConfigurator.class)
public class ArenaSocket {

    private final HashMap<Integer, ServerThread> states = new HashMap<>();
    private final Gson gson = new Gson();

    {
        states.put(0, new ServerThread());
        Thread thread = new Thread(states.get(0));
        thread.start();
    }

    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config, @PathParam("token") String token) throws IOException {
        if (token.equals("*"))
            token = (String) config.getUserProperties().get("t");
        Member member = ServletContext.mongoUsers.getByToken(token);
        if (member != null) {
            userSession.getUserProperties().put("m", member);
            userSession.getUserProperties().put("id", String.valueOf(member.getPdaId()));

            states.get(0).addSession(userSession);
        } else {
            userSession.close();
        }
    }

    @OnClose
    public void onClose(Session userSession) {
        states.get(0).closeSession(userSession);
    }


    @OnMessage
    public void onMessage(String message, Session userSession) throws IllegalStateException, JsonSyntaxException {
        Action action = gson.fromJson(message, Action.class);
        switch (action.getAction()) {
            case "move":
                Vector2 position = gson.fromJson(action.getData(), Vector2.class);
                userSession.getUserProperties().get("id");
                states.get(0).move(userSession, position);
                break;
        }

    }

    @OnError
    public void onError(Throwable thr) {
        ServletContext.error("Error at chat socket", thr);
        thr.printStackTrace();
    }

}