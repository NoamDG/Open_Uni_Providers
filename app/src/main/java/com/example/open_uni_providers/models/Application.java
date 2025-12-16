package com.example.open_uni_providers.models;

import androidx.annotation.NonNull;

public class Application {
    String id, fName, lName, content;

    public Application() {
    }

    public Application(String id, String fName, String lName, String content) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.content = content;
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
    public String getlName() {
        return lName;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
