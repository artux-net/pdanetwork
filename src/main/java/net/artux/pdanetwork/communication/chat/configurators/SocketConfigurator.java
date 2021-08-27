package net.artux.pdanetwork.communication.chat.configurators;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.communication.FeedSocket;
import net.artux.pdanetwork.communication.arena.ArenaSocket;
import net.artux.pdanetwork.communication.chat.ChatSocket;
import net.artux.pdanetwork.communication.chat.DialogsSocket;
import net.artux.pdanetwork.communication.chat.GroupsSocket;
import net.artux.pdanetwork.communication.chat.MessagesSocket;
import net.artux.pdanetwork.handlers.ChatHandler;
import org.springframework.context.annotation.Configuration;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;


public class SocketConfigurator {


    /*
    public static final ChatHandler chatServer = new ChatHandler();
    public static final GroupsSocket groupServer = new GroupsSocket();
    public static final MessagesSocket messagesServer = new MessagesSocket();
    public static final ArenaSocket arenaSocket = new ArenaSocket();
    public static final DialogsSocket dialogsSocket = new DialogsSocket();
    public static final FeedSocket feedSocket = new FeedSocket();

    public <T> T getEndpointInstance(Class<T> endpointClass) {
        if (endpointClass.equals(ChatSocket.class))
            return (T) chatServer;
        else if (endpointClass.equals(GroupsSocket.class))
            return (T) groupServer;
        else if (endpointClass.equals(MessagesSocket.class))
            return (T) messagesServer;
        else if (endpointClass.equals(ArenaSocket.class))
            return (T) arenaSocket;
        else if (endpointClass.equals(DialogsSocket.class))
            return (T) dialogsSocket;
        else return (T) feedSocket;
    }*/
}
