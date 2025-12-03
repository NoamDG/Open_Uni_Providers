package com.example.open_uni_providers.models;

import java.util.Date;

public class Tender {
    String tenSubj, tenStat, tenWinner, pubDate, expDate, content;




    public Tender(String subject, String expireDate, String status, String winnerName, String pubDate, String content) {
        this.tenSubj = subject;
        this.expDate = expireDate;
        this.tenStat = status;
        this.tenWinner = winnerName;
        this.pubDate = pubDate;
        this.content = content;
    }


    public String getSubject() { return tenSubj; }
    public String getExpireDate() { return expDate; }
    public String getStatus() { return tenStat; }
    public String getPublish() { return pubDate; }
    public String getWinnerName() { return tenWinner; }
    public void setWinnerName(String WinnerName) {
        this.tenWinner = WinnerName;
    }
    public String getContent() {return content;}
}
