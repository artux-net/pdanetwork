package com.pdanetwork.chat.configurators;

import com.pdanetwork.chat.GroupsSocket;

import javax.websocket.server.ServerEndpointConfig;

public class GroupsSocketConfigurator extends ServerEndpointConfig.Configurator {

    private static GroupsSocket chatServer = new GroupsSocket();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) chatServer;
    }
}