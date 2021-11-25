package com.dacoders.buksue_libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Window window;
    MyLoadingButton mLoginBtn;
    TextView tvRegister,tvForgotPass;
    TextInputLayout email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        initComponents();

    }


    private void initComponents() {
        mAuth =FirebaseAuth.getInstance();
        window = getWindow();
        window.setStatusBarColor(Color.parseColor("#09427D"));
        tvRegister = findViewById(R.id.TvRegister);
        tvForgotPass = findViewById(R.id.TvForgotPass);
        mLoginBtn = findViewById(R.id.btnLogin);
        email = findViewById(R.id.m_email);
        password = findViewById(R.id.m_password);


        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ResetPasswordActivity1.class));
            }
        });


        mLoginBtn.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                mLoginBtn.showNormalButton();
                if (email.getEditText().getText().toString().trim().isEmpty())
                   {
                    email.setError("This field cannot be empty!");
                    email.requestFocus();
                   }
                else if(!email.getEditText().getText().toString().trim().endsWith("buksu.edu.ph"))
                {
                    email.setError("Invalid university address");
                   email.requestFocus();
                }
                else if(password.getEditText().getText().toString().trim().isEmpty())
                {
                    password.setError("This field cannot be empty");
                    password.requestFocus();
                }

                else
                    {

                    mLoginBtn.showLoadingButton();
                    loginUser();
                    mLoginBtn.setEnabled(false);
                    }
            }
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 String currentEmailInput = email.getEditText().getText().toString().trim();

                 if(!currentEmailInput.isEmpty()||currentEmailInput.endsWith("buksu.edu.ph")){
                     email.setError("");
                 }

            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               String currentPasswordInput = password.getEditText().getText().toString().trim();
               if(currentPasswordInput.isEmpty()){
                   password.setError("");
               }
            }
        });



        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String userEmail = email.getEditText().getText().toString().trim();
        String userPassword = password.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    } else{

                        startActivity(new Intent(MainActivity.this, UnverifiedUserLandingActivity.class));
                    }
                }
                else{

                     mLoginBtn.showNormalButton();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFailure(@NonNull Exception e) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setConfirmText("Okay");
                sweetAlertDialog.show();
                sweetAlertDialog.setContentText(e.getMessage());
                sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.textSecondary);
                sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sweetAlertDialog.dismiss();
                    }
                });

      }
        });
    }


    @SuppressLint("ResourceAsColor")
    private void createSweetAlertDialog(String errorCode){

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setConfirmText("Okay");
        sweetAlertDialog.show();
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.textSecondary);
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sweetAlertDialog.dismiss();
            }
        });






        switch (errorCode){
            case "ERROR_WRONG_PASSWORD":

                 sweetAlertDialog.setContentText("The password you entered is incorrect.");
                 break;


            case "ERROR_USER_NOT_FOUND":


                 sweetAlertDialog.setContentText("There is no account matching these credentials.");
                 break;



            default:

                sweetAlertDialog.setContentText("Couldn't sign in!");
                break;




        }







    }











    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();

    }
}


