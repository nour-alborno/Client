package com.example.client.Model;

public class ContactUs {
    String message;
    String date;
    String clientId;
    String title;

    public ContactUs() {
    }

    public ContactUs(String message, String date, String clientId, String title) {
        this.message = message;
        this.date = date;
        this.clientId = clientId;
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
