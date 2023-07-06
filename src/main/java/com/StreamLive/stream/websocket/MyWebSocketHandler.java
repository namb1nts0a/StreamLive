package com.StreamLive.stream.websocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class MyWebSocketHandler {

    private final SocketIOServer server;
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> rooms = new HashMap<>();

    private String active_room = "";

    private Map<String, Boolean> room_list = new HashMap<>();

    public MyWebSocketHandler(SocketIOServer server) {
        this.server = server;
        server.addListeners(this);
        server.start();
    }

    @OnConnect
    public void onConnect(SocketIOClient client){
        System.out.println("Client connected: "+client.getSessionId());
        String clientId = client.getSessionId().toString();
        users.put(clientId, null);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client){
        String clientId = client.getSessionId().toString();
        String room = users.get(clientId);

        if(!Objects.isNull(room)){
            System.out.printf("Client disconnected: %s from : %s%n", clientId, room);
            users.remove(clientId);
            client.getNamespace().getRoomOperations(room).sendEvent("userDisconnected", clientId);
        }
        printLog("onDisconnect", client, room);
    }

    @OnEvent("joinRoom")
    public void onJoinRoom(SocketIOClient client) {
        System.out.println("here");
        boolean state = true;
        String current_room = "";

        //Rehefa mahita libre dia mset false ny state
        for(var tmp : room_list.entrySet()){
            System.out.println("ROOM AVAILABLE: "+ tmp.getKey()+" "+tmp.getValue());
            if (tmp.getValue()) {
                state = false;
                current_room = tmp.getKey();
                break;
            }
        }

        if(state){

            String room_name = UUID.randomUUID().toString();
            active_room = room_name;
            System.out.println("Room created when joining: "+room_name);
            room_list.put(room_name, true);

            for(var r: room_list.entrySet()){

                if(r.getValue()){
                    int connectedClients = server.getRoomOperations(r.getKey()).getClients().size();

                    if (connectedClients == 0) {
                        client.joinRoom(r.getKey());
                        /*room_list.put(r.getKey(),true);*/

                        client.sendEvent("created", r.getKey());
                        users.put(client.getSessionId().toString(), r.getKey());
                        rooms.put(r.getKey(), client.getSessionId().toString());
                    } else if (connectedClients == 1) {
                        client.joinRoom(r.getKey());
                        client.sendEvent("joined", r.getKey());
                        users.put(client.getSessionId().toString(), r.getKey());
                        client.sendEvent("setCaller", rooms.get(r.getKey()));
                        room_list.put(r.getKey(),false);
                    } else {
                        client.sendEvent("full", r.getKey());
                    }
                }

            }
        }else{
            System.out.println("Current room else: "+current_room);

            int connectedClients = server.getRoomOperations(current_room).getClients().size();

            if (connectedClients == 1) {
                client.joinRoom(current_room);
                client.sendEvent("joined", current_room);
                users.put(client.getSessionId().toString(), current_room);
                client.sendEvent("setCaller", rooms.get(current_room));
                room_list.put(current_room,false);
            } else {
                client.sendEvent("full", current_room);
            }
        }
    }

    @OnEvent("ready")
    public void onReady(SocketIOClient client, String room, AckRequest ackRequest) {
        client.getNamespace().getBroadcastOperations().sendEvent("ready", active_room);
        printLog("onReady", client, active_room);
    }

    @OnEvent("candidate")
    public void onCandidate(SocketIOClient client, Map<String, Object> payload) {
        /*String room = (String) payload.get("room");*/

        client.getNamespace().getRoomOperations(active_room).sendEvent("candidate", payload);
        printLog("onCandidate", client, active_room);
    }

    @OnEvent("offer")
    public void onOffer(SocketIOClient client, Map<String, Object> payload) {
        System.out.println("Client: "+ client.getNamespace().toString());
        System.out.println("Payload: "+ payload.get("room"));
        /*String room = (String) payload.get("room");*/
        Object sdp = payload.get("sdp");
        client.getNamespace().getRoomOperations(active_room).sendEvent("offer", sdp);
        printLog("onOffer", client, active_room);
    }

    @OnEvent("answer")
    public void onAnswer(SocketIOClient client, Map<String, Object> payload) {
        /*String room = (String) payload.get("room");*/
        Object sdp = payload.get("sdp");
        client.getNamespace().getRoomOperations(active_room).sendEvent("answer", sdp);
        printLog("onAnswer", client, active_room);
    }

    @OnEvent("leaveRoom")
    public void onLeaveRoom(SocketIOClient client, String room) {
        client.leaveRoom(active_room);
        printLog("onLeaveRoom", client, active_room);
    }


    private static void printLog(String header, SocketIOClient client, String room){
        if (room == null) return;
        int size = 0;
        try{
            size = client.getNamespace().getRoomOperations(room).getClients().size();
        }catch(Exception e){
            log.error("error : ", e);
        }
        log.info("#Connectedclient - {} => room: {}, count: {}", header, room, size);
    }

}
