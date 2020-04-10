package net.artux.pdanetwork.communication.chat.configurators;

import net.artux.pdanetwork.communication.chat.GroupsSocket;

import javax.websocket.server.ServerEndpointConfig;

public class GroupsSocketConfigurator extends ServerEndpointConfig.Configurator {

    private static GroupsSocket chatServer = new GroupsSocket();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) chatServer;
    }
}