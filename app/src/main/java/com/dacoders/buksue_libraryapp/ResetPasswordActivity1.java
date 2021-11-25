package com.dacoders.buksue_libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ResetPasswordActivity1 extends AppCompatActivity {
        ActionBar actionBar;
    private TextInputLayout email;
    private MyLoadingButton nextBtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password1);
       nextBtn = findViewById(R.id.passwordResetNextBtn);
        email = findViewById(R.id.resentPassEmail);
        mAuth = FirebaseAuth.getInstance();
        initComponents();



    }

    private void initComponents() {
        nextBtn.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                nextBtn.showNormalButton();
                if (email.getEditText().getText().toString().trim().isEmpty()) {
                    email.setError("This field cannot be empty!");
                    email.requestFocus();

                } else if (!email.getEditText().getText().toString().trim().endsWith("buksu.edu.ph")) {
                    email.setError("Invalid university address");
                    email.requestFocus();

                } else if (isEmailInputIsOk()) {
                    nextBtn.showLoadingButton();
                    confirmEmail(email.getEditText().getText().toString().trim());

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

                if (!currentEmailInput.isEmpty() || currentEmailInput.endsWith("buksu.edu.ph")) {
                    email.setError("");
                }

            }
        });

    }

    private void confirmEmail(String s) {
        mAuth.sendPasswordResetEmail(s).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createSweetAlertSuccessDialog();
                nextBtn.showNormalButton();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResetPasswordActivity1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                nextBtn.showNormalButton();
            }
        });
    }

    private boolean isEmailInputIsOk() {
        return (!email.getEditText().getText().toString().trim().isEmpty() && email.getEditText().getText().toString().trim().endsWith("buksu.edu.ph"));
    }


/*
    private void setUpSuccessDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ResetPasswordActivity1.this,R.style.successDialog);
        builder.setMessage(R.string.dialogPasswordResetLinkSent)
                .setPositiveButton("Open Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                            startActivity(intent);
                        }catch(ActivityNotFoundException e){
                            Toast.makeText(ResetPasswordActivity1.this, "No installed apps found to open email", Toast.LENGTH_SHORT).show();
                        }


                    }
                });





    }*/

    @SuppressLint("ResourceAsColor")
    private void createSweetAlertSuccessDialog(){

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ResetPasswordActivity1.this, SweetAlertDialog.SUCCESS_TYPE);
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
                    Toast.makeText(ResetPasswordActivity1.this, "No installed apps found to open email", Toast.LENGTH_SHORT).show();
                }



                sweetAlertDialog.dismiss();
            }
        });


        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setOnClickListener(click->{



            sweetAlertDialog.dismiss();

        });

    }










}



