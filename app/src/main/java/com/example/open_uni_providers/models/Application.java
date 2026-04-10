package com.example.open_uni_providers.models;

public class Application {
    String id, fName, lName, content, subject, status;

    public Application() {
    }

    public Application(String id, String fName, String lName, String content, String subject, String status) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.content = content;
        this.subject = subject;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getlName() {
        return lName;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
