package com.sadique.quicknotes;


public class QuickNotes {


    private String lastDate;
    private String quickNotes;
    public QuickNotes(){
        lastDate ="";
        quickNotes ="";}
    public String getQuickNotes() {
        return quickNotes;
    }

    public void setQuickNotes(String quickNotes) {
        this.quickNotes = quickNotes;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setquickNotes(String lastDate) {
        this.lastDate = lastDate;
    }

    public String toString() {
        return lastDate + ": " + quickNotes;
    }

}