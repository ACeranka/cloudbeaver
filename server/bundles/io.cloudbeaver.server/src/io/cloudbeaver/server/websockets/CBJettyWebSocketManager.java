/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2024 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudbeaver.server.websockets;

import io.cloudbeaver.server.AppWebSessionManager;
import io.cloudbeaver.server.CBPlatform;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CBJettyWebSocketManager extends Endpoint {
    private static final Log log = Log.getLog(CBJettyWebSocketManager.class);
    private final Map<String, List<CBEventsWebSocket>> socketBySessionId = new ConcurrentHashMap<>();
    private final AppWebSessionManager webSessionManager;

    public CBJettyWebSocketManager(@NotNull AppWebSessionManager webSessionManager) {
        this.webSessionManager = webSessionManager;

        new WebSocketPingPongJob(CBPlatform.getInstance(), this).scheduleMonitor();
    }

    @Override
    public void onClose(Session session, CloseReason close) {
        super.onClose(session, close);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        log.info("WebSocket Open: %s".formatted(session));
    }

    @Override
    public void onError(Session session, Throwable cause) {
        super.onError(session, cause);
        log.warn("WebSocket Error", cause);
    }
}
