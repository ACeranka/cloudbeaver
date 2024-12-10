package io.cloudbeaver.server.websockets;

import io.cloudbeaver.model.session.WebHttpRequestInfo;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.eclipse.jetty.ee10.websocket.jakarta.server.internal.JakartaWebSocketCreator;
import org.jkiss.utils.CommonUtils;

public class CBWebSocketServerConfigurator extends ServerEndpointConfig.Configurator {
    public static final String PROP_WEB_SESSION = "CB-Web-Session";

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        final String sessionId = request.getHttpSession() instanceof HttpSession httpSession ? httpSession.getId() : null;
        final String userAgentHeader = request.getHeaders().get("User-Agent").stream().findFirst().orElse(null);
        WebHttpRequestInfo requestInfo = new WebHttpRequestInfo(
            sessionId,
            sec.getUserProperties().get(JakartaWebSocketCreator.PROP_LOCALES),
            CommonUtils.toString(sec.getUserProperties().get(JakartaWebSocketCreator.PROP_REMOTE_ADDRESS)),
            userAgentHeader
        );
        sec.getUserProperties().put(PROP_WEB_SESSION, requestInfo);
    }
}
