package com.dacoders.buksue_libraryapp.BookStatisticsHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentReadsRecords {
    String key;
    String student_id;
    String student_firstName;
    String student_lastName;
    String month_January;
    String month_February;
    String month_March;
    String month_April;
    String month_May;
    String month_June;
    String month_July;
    String month_August;
    String month_September;
    String month_October;
    String month_November;
    String month_December;

    public StudentReadsRecords(String key, String student_id, String student_firstName, String student_lastName) {
        this.key = key;
        this.student_id = student_id;
        this.student_firstName = student_firstName;
        this.student_lastName = student_lastName;
       }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_firstName() {
        return student_firstName;
    }

    public void setStudent_firstName(String student_firstName) {
        this.student_firstName = student_firstName;
    }

    public String getStudent_lastName() {
        return student_lastName;
    }

    public void setStudent_lastName(String student_lastName) {
        this.student_lastName = student_lastName;
    }

}
