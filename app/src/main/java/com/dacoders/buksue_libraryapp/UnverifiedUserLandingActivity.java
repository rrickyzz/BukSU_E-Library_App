package com.dacoders.buksue_libraryapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UnverifiedUserLandingActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView textViewName,userStatus;
    MyLoadingButton sendAnotherVerifBtn;
    MaterialButton signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unverified_user_landing);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#09427D"));
      //  ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
     //   actionBar.setDisplayShowHomeEnabled(true);
       // actionBar.setTitle("");
     //   actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#09427D")));
        mAuth = FirebaseAuth.getInstance();
          signOutBtn = findViewById(R.id.UbtnSignout);
        sendAnotherVerifBtn = findViewById(R.id.sendEmailVerification);
        textViewName = findViewById(R.id.unverifiedUserName);
        if(mAuth.getCurrentUser()!=null){
        textViewName.setText("Hi "+mAuth.getCurrentUser().getEmail());}
        sendAnotherVerifBtn.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                sendAnotherVerifBtn.showLoadingButton();
                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createSweetAlertSuccessDialog();
                        sendAnotherVerifBtn.showNormalButton();
                    }
                });
            }
        });


        signOutBtn.setOnClickListener(v->{
            mAuth.signOut();
            startActivity(new Intent(UnverifiedUserLandingActivity.this,MainActivity.class));
        });




    }



    @SuppressLint("ResourceAsColor")
    private void createSweetAlertSuccessDialog(){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(UnverifiedUserLandingActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setContentText("A verification message has been sent to the university email you have provided. Check it now in order to get verified.");
        sweetAlertDialog.setConfirmText("Open Email");
        sweetAlertDialog.setCancelText("Later");
        sweetAlertDialog.show();
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.textSecondary);
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setTextColor(R.color.textSecondary);


        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    Toast.makeText(UnverifiedUserLandingActivity.this, "No installed apps found to open email", Toast.LENGTH_SHORT).show();
                }



                sweetAlertDialog.dismiss();
            }
        });


        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setOnClickListener(click->{



            sweetAlertDialog.dismiss();

        });

    }














    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}