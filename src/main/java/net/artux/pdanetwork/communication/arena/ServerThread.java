package net.artux.pdanetwork.communication.arena;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import org.apache.commons.lang3.SerializationUtils;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;

public class ServerThread implements Runnable {

    private volatile boolean running = true;
    private final State state = new State();
    private final ArrayList<Session> sessions = new ArrayList<>();
    private final Gson gson = new Gson();

    public void terminate() throws IOException {
        running = false;
        CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Terminated");
        for (Session session : sessions) {
            session.close(closeReason);
        }
    }

    public void move(Session session, Vector2 position) {
        state.entities.get(session.getUserProperties().get("id")).move(position);
    }

    public void addSession(Session session) {
        sessions.add(session);
        state.entities.put((String) session.getUserProperties().get("id"), new Entity(new Vector2(0, 0), (Member) session.getUserProperties().get("m")));
    }

    public void closeSession(Session session) {
        state.entities.remove(session.getUserProperties().get("id"));
        sessions.remove(session);
    }

    @Override
    public void run() {
        while (running) {
            try {
                SerializationUtils.serialize(this.state);
                for (Session session : sessions) {
                    //session.getBasicRemote().sendBinary();
                    session.getBasicRemote().sendObject(this.state);
                }


                Thread.sleep(50);
            } catch (InterruptedException e) {
                try {
                    terminate();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        }

    }
}
