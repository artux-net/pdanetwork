package net.artux.pdanetwork.communication.chat.configurators;

import net.artux.pdanetwork.communication.chat.ChatSocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

public class ChatSocketConfigurator extends Configurator {

    private static ChatSocket chatServer = new ChatSocket();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) {
        return (T)chatServer;
    }
}
