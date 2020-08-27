package com.muradit.projectx.Model;

public class Report {
    private String user_id;
    private String store_id;
    private String reportText;
    private String Date;
    public Report(String user_id, String store_id, String reportText,String Date) {
        this.user_id = user_id;
        this.store_id = store_id;
        this.reportText = reportText;
        this.Date=Date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }
}
