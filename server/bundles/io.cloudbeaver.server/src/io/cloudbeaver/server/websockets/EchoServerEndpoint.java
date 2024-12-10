package io.cloudbeaver.server.websockets;

import jakarta.websocket.*;
import org.jkiss.dbeaver.Log;

public class EchoServerEndpoint extends Endpoint implements MessageHandler.Whole<String> {
    private static final Log log = Log.getLog(EchoServerEndpoint.class);
    private Session session;
    private RemoteEndpoint.Async remote;

    @Override
    public void onClose(Session session, CloseReason close) {
        super.onClose(session, close);
        this.session = null;
        this.remote = null;
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.remote = this.session.getAsyncRemote();
        log.info("WebSocket Open: %s".formatted(session));
        // attach echo message handler
        session.addMessageHandler(this);
        this.remote.sendText("You are now connected to " + this.getClass().getName());
    }

    @Override
    public void onError(Session session, Throwable cause) {
        super.onError(session, cause);
        log.warn("WebSocket Error", cause);
    }

    @Override
    public void onMessage(String message) {
        log.info("Echoing back text message [%s]".formatted(message));
        this.remote.sendText(message);
    }
}