package com.dacoders.buksue_libraryapp;

import java.time.Month;

public class DateUserRegisteredModel {


    String IdNumber;
    String userId;
    String college;
    int date;
    Month month;
    int year;

    public DateUserRegisteredModel(String idNumber, String userId,String college, int date, Month month, int year) {
        this.IdNumber = idNumber;
        this.userId = userId;
        this.college = college;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(String idNumber) {
        IdNumber = idNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
