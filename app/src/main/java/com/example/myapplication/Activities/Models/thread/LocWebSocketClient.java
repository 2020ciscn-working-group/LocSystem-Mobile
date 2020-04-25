package com.example.myapplication.Activities.Models.thread;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class LocWebSocketClient extends WebSocketClient {
    public LocWebSocketClient(URI serverUri){
        super(serverUri, new Draft_6455());
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
