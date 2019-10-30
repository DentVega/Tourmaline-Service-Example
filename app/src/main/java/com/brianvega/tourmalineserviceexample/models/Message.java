package com.brianvega.tourmalineserviceexample.models;

public class Message {

    public String message;

    public Integer color;

    public Message(String message, Integer color) {
        this.message = message;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
