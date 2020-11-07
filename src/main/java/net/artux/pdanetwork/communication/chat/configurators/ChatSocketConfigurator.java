package net.artux.pdanetwork.communication.chat.configurators;

import net.artux.pdanetwork.communication.chat.ChatSocket;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class ChatSocketConfigurator extends Configurator {

    private static ChatSocket chatServer = new ChatSocket();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) {
        return (T)chatServer;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        config.getUserProperties().putAll(request.getHeaders());
        for (String s : request.getHeaders().keySet()) {
            config.getUserProperties().put(s, request.getHeaders().get(s).get(0));
        }
    }
}
