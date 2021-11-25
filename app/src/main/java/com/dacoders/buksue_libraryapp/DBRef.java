package com.dacoders.buksue_libraryapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DBRef {

    private FirebaseDatabase db;
    private DatabaseReference root;

    public DBRef(){
        db = FirebaseDatabase.getInstance();
        root = db.getReference();


    }

    public Task<Void> addStudent(StudentModelClass obj){
        return root.child("Student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(obj);
    }



}
