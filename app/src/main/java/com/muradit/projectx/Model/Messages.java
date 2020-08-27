package com.muradit.projectx.Model;

public class Messages {
    private String sender;
    private String receiver;
    private String content;
    private String time;
    private String roomId;
    private String senderId;
    private String receiverId;
    private String status;

    public Messages(String sender, String receiver, String content, String time, String roomId, String senderId, String receiverId,String status) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoomId() {
        return roomId;
    }
}