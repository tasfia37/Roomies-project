package com.example.roomies.Chat;

public class ChatObject {

    private String  message;
    private boolean currentUser;

    public ChatObject(String  message, boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    public  String getMessage() { return  message;}
    public void setMessage(String message){this.message = message;}

    public  Boolean getCurrentUser() { return  currentUser;}
    public void setCurrentUser(Boolean currentUser){this.currentUser = currentUser;}

}
