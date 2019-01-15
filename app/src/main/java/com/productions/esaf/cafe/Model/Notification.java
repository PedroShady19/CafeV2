package com.productions.esaf.cafe.Model;

public class Notification {
    public String body;
    public String tittle;

    public Notification(String body, String tittle) {
        this.body = body;
        this.tittle = tittle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
}
