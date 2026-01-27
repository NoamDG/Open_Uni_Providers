package com.example.open_uni_providers.models;

import androidx.annotation.NonNull;


public class Tender {
    String id, tenSubj, tenStat, tenWinner, pubDate, expDate, content, category;

    public Tender() {
    }

    public Tender(String id, String subject, String expireDate, String status, String winnerName, String pubDate, String Category, String content) {
        this.id = id;
        this.tenSubj = subject;
        this.expDate = expireDate;
        this.tenStat = status;
        this.tenWinner = winnerName;
        this.pubDate = pubDate;
        this.category = Category;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenSubj() {
        return tenSubj;
    }

    public void setTenSubj(String tenSubj) {
        this.tenSubj = tenSubj;
    }

    public String getTenStat() {
        return tenStat;
    }

    public void setTenStat(String tenStat) {
        this.tenStat = tenStat;
    }

    public String getTenWinner() {
        return tenWinner;
    }

    public void setTenWinner(String tenWinner) {
        this.tenWinner = tenWinner;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "Tender{" +
                "id='" + id + '\'' +
                ", tenSubj='" + tenSubj + '\'' +
                ", tenStat='" + tenStat + '\'' +
                ", tenWinner='" + tenWinner + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", expDate='" + expDate + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
