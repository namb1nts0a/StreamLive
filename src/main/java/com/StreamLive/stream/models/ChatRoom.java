package com.StreamLive.stream.models;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameChatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ChatRoom(){

    }

    public ChatRoom(String nameChatRoom, User user){
        this.nameChatRoom = nameChatRoom;
        this.user = user;
    }

    // Getters et setters pour id, name, user

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameChatRoom() {
        return nameChatRoom;
    }

    public void setNameChatRoom(String nameChatRoom) {
        this.nameChatRoom = nameChatRoom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
