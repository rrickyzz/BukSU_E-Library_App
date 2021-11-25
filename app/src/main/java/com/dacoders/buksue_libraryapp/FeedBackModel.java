package com.dacoders.buksue_libraryapp;

public class FeedBackModel

{


    String message;
    String time_delivered;

    public FeedBackModel(String message, String time_delivered) {
        this.message = message;
        this.time_delivered = time_delivered;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime_delivered() {
        return time_delivered;
    }

    public void setTime_delivered(String time_delivered) {
        this.time_delivered = time_delivered;
    }
}
