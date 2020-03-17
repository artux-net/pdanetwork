package com.pdanetwork.chat.configurators;

import com.pdanetwork.chat.ChatSocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * ChatSocketConfigurator
 * @author Jiji_Sasidharan
 */
public class ChatSocketConfigurator extends Configurator {

    private static ChatSocket chatServer = new ChatSocket();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T)chatServer;
    }
}
