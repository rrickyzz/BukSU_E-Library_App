package com.dacoders.buksue_libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.NoNetworkConnectivityHelper.NetworkChangeReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashScreen extends AppCompatActivity implements NetworkChangeReceiver.ReceiverListener {
    com.jb.dev.progress_indicator.dotGrowProgressBar progressBar;
    boolean isConnectedToNet;
    ActionBar actionBar;
    FirebaseAuth mAuth;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.dotGrowProgressbar);
        checkConnection();

        mAuth = FirebaseAuth.getInstance();
        if(hasNoUser()) {


        }
        if(mAuth.getCurrentUser()!=null){

        }

        Thread splashScreen = new Thread() {


            public void run() {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        sleep(2 * 1000);
                             if(isConnectedToNet){
                                 if(hasNoUser())
                                 {
                                     startActivity(new Intent(SplashScreen.this,MainActivity.class));
                                     finish();

                                 }

                                 if(hasUserButUnverified())
                                 {
                                     handleEmailVerification();
                                 }
                                 if(hasVerifiedUser()){
                                     startActivity(new Intent(SplashScreen.this,HomeActivity.class));
                                     finish();
                                 }



                             }





                    } catch (Exception e) {

                        Toast.makeText(SplashScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }




            }


            };

        if(isConnectedToNet){

        }else{
             SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SplashScreen.this, SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setContentText("No internet Connection");
            sweetAlertDialog.setConfirmText("Okay");
            sweetAlertDialog.setCustomImage(R.drawable.ic_baseline_wifi_off_24);
            sweetAlertDialog.show();
            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.white);
            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sweetAlertDialog.dismiss();
                    System.exit(0);

                }
            });
        }


        splashScreen.start();

         }


    private boolean hasNoUser()
    {
        return mAuth.getCurrentUser()==null;
    }
    private boolean hasUserButUnverified(){
        return mAuth.getCurrentUser()!=null&&!mAuth.getCurrentUser().isEmailVerified();
    }
    private boolean hasVerifiedUser(){
        return mAuth.getCurrentUser()!=null&&mAuth.getCurrentUser().isEmailVerified();
    }


    private void receiveDynamicLink(){
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {


            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                Uri deeplink = null;
                if(pendingDynamicLinkData!=null){
                    System.out.println("===============================================================================");
                System.out.println("====> oobCode: "+pendingDynamicLinkData.getLink().getQueryParameter("oobCode"));

                System.out.println("======> url: "+pendingDynamicLinkData.getLink().getQueryParameter("continueUrl"));
                  System.out.println("========= url2: "+pendingDynamicLinkData.getLink());
                  String code = pendingDynamicLinkData.getLink().getQueryParameter("oobCode");
                  mAuth.applyActionCode(code);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    private  void handleEmailVerification(){
        {

            FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                @Override
                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                    if(pendingDynamicLinkData!=null){
                        receiveDynamicLink();
                        mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mAuth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                    @Override
                                    public void onSuccess(GetTokenResult getTokenResult) {

                                        progressBar.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                                    }
                                });

                            }
                        });


                    } else if(pendingDynamicLinkData==null&&hasUserButUnverified()){
                        startActivity(new Intent(SplashScreen.this,UnverifiedUserLandingActivity.class));
                    }
                    else if(pendingDynamicLinkData==null&&hasVerifiedUser()){
                        startActivity(new Intent(SplashScreen.this,HomeActivity.class));
                    }
                }
            });




        }

    }



    private void checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        registerReceiver(new NetworkChangeReceiver(), intentFilter);

        // Initialize listener
        NetworkChangeReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        // assign it to globar variable

        isConnectedToNet= isConnected;

    }


    @Override
    protected void onStart() {
        if(mAuth.getCurrentUser()!=null){mAuth.getCurrentUser().reload();
        mAuth.getCurrentUser().getIdToken(true);}
        super.onStart();
        checkConnection();


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkConnection();
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        isConnected = isConnected;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}



