package com.StreamLive.stream.websocket;

import com.StreamLive.stream.models.User;
import com.StreamLive.stream.services.UserService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private UserService userService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        // Cette méthode est appelée lorsque la connexion WebSocket est établie avec succès.
        // Tu peux ajouter ici des logiques spécifiques à ton application.
        User connectedUser = userService.getUserBySessionId(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        // Cette méthode est appelée lorsque le serveur reçoit un message texte de la session WebSocket.
        // Tu peux ajouter ici des logiques spécifiques à ton application pour traiter le message.
        String receivedMessage = message.getPayload();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        // Cette méthode est appelée lorsque la connexion WebSocket est fermée.
        // Tu peux ajouter ici des logiques spécifiques à ton application.
        User disconnectedUser = userService.getUserBySessionId(session.getId());
    }
}
