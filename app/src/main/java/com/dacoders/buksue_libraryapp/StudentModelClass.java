package com.dacoders.buksue_libraryapp;

import com.google.android.gms.tasks.Task;

public class StudentModelClass {

    public StudentModelClass() {
    }

    public StudentModelClass(String uid, String university_id, String profile_pic_link, String firstName, String lastName, String displayName, String mobile, String gender, String college, String year) {
        this.uid = uid;
        this.university_id = university_id;
        this.profile_pic_link = profile_pic_link;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.mobile = mobile;
        this.gender = gender;
        this.college = college;
        this.year = year;
    }


    private String uid;
   private String university_id;
   private String profile_pic_link;
   private String firstName;
   private String lastName;
   private String displayName;
   private String mobile;
   private String gender;
   private String college;
   private String year;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(String university_id) {
        this.university_id = university_id;
    }

    public String getProfile_pic_link() {
        return profile_pic_link;
    }

    public void setProfile_pic_link(String profile_pic_link) {
        this.profile_pic_link = profile_pic_link;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
