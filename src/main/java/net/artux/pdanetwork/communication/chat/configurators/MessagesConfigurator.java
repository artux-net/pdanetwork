package net.artux.pdanetwork.communication.chat.configurators;

import net.artux.pdanetwork.communication.chat.MessagesSocket;

import javax.websocket.server.ServerEndpointConfig;

public class MessagesConfigurator extends ServerEndpointConfig.Configurator {

    private static MessagesSocket chatServer = new MessagesSocket();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T)chatServer;
    }
}
