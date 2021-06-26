package net.artux.pdanetwork.communication.arena;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.Profile;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static net.artux.pdanetwork.utills.ServletContext.log;

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

    public void move(Session session, Vector2 velocity) {
        state.players.get(session.getId()).velocity = velocity;
    }

    public void shoot(Session session, Vector2 velocity) {
        for(Map.Entry<String, Entity> entry : state.players.entrySet()) {
            Entity value = entry.getValue();
            if (state.players.get(session.getId()).position.isRayHit(velocity, value.getCollider().getLeft(), value.getCollider().getRight()))
                value.health -= 5;
        }
    }


    public void addSession(Session session) {
        sessions.add(session);
        Member member = (Member) session.getUserProperties().get("m");
        state.addPlayer(session, new Profile(member));

        log("New player has connected, pdaId: " +  member.getPdaId());
    }

    public void closeSession(Session session) {
        Member member =(Member) session.getUserProperties().get("m");
        log("Player has disconnected, pdaId: " +  member.getPdaId());

        state.removePlayer(session);
        sessions.remove(session);
    }

    @Override
    public void run() {
        while (running) {
            try {
                //SerializationUtils.serialize(this.state);
                state.act();
                for (Session session : sessions) {
                    if (session.isOpen())
                        session.getBasicRemote().sendText(gson.toJson(this.state));
                }
                state.dispose();

                Thread.sleep(30); // update 1000/50=20 times per second
            } catch (InterruptedException e) {
                try {
                    terminate();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
