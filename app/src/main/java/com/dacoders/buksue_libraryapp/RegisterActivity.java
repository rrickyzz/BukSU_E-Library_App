package com.dacoders.buksue_libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.internal.DynamicLinkData;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {
    private MyLoadingButton registerBtn;
    private TextView tvlogin;
    private TextInputLayout email, password;
    private CardView v1, v2, v3, v4;
    private boolean isv1ok = false;
    private boolean isv2ok = false;
    private boolean isv3ok = false;
    private boolean isv4ok = false;
    FirebaseAuth rAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvlogin = findViewById(R.id.R_tv_login);
        registerBtn = findViewById(R.id.RbtnRegister);
        email = findViewById(R.id.R_TLI_email);
        v1 = findViewById(R.id.verifyCardView1);
        v2 = findViewById(R.id.verifyCardView2);
        v3 = findViewById(R.id.verifyCardView3);
        v4 = findViewById(R.id.verifyCardView4);
        rAuth = FirebaseAuth.getInstance();
        password = findViewById(R.id.R_TLI_password);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#09427D"));
        ActionBar actionBar = getSupportActionBar();
     //   actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setDisplayShowHomeEnabled(true);
      //  actionBar.setTitle("");
     //   actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#09427D")));
        initComponents();


    }

    private void initComponents() {

        registerBtn.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                registerBtn.showNormalButton();
                if (email.getEditText().getText().toString().trim().isEmpty()) {
                    email.setError("This field cannot be empty!");
                    email.requestFocus();

                } else if (!email.getEditText().getText().toString().trim().endsWith("buksu.edu.ph")) {
                    email.setError("Invalid university address");
                    email.requestFocus();

                } else if (password.getEditText().getText().toString().trim().isEmpty()) {
                    password.setError("This field cannot be empty");
                    password.requestFocus();
                } else if (!isPasswordInputIsOk()) {
                    password.setError("Password is weak!");
                    password.requestFocus();
                } else if (isEmailInputIsOk() && isPasswordInputIsOk()) {
                    registerBtn.showLoadingButton();
                    createNewUser();

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
                if (!currentPasswordInput.isEmpty()) {
                    password.setError("");
                }
                if (currentPasswordInput.length() >= 8) {
                    v1.setCardBackgroundColor(Color.parseColor("#00E676"));
                    isv1ok = true;
                } else {
                    v1.setCardBackgroundColor(Color.parseColor("#C5C5C5"));
                    isv1ok = false;
                }

                if (currentPasswordInput.matches("(.*[A-Z].*)")) {
                    v2.setCardBackgroundColor(Color.parseColor("#00E676"));
                    isv2ok = true;
                } else {
                    v2.setCardBackgroundColor(Color.parseColor("#C5C5C5"));
                    isv2ok = false;
                }


                if (currentPasswordInput.matches("(.*[a-z].*)")) {
                    v3.setCardBackgroundColor(Color.parseColor("#00E676"));
                    isv3ok = true;
                } else {
                    v3.setCardBackgroundColor(Color.parseColor("#C5C5C5"));
                    isv3ok = false;
                }

                if (currentPasswordInput.matches("(.*[0-9].*)")) {

                    v4.setCardBackgroundColor(Color.parseColor("#00E676"));
                    isv4ok = true;
                } else {
                    v4.setCardBackgroundColor(Color.parseColor("#C5C5C5"));
                    isv4ok = false;
                }


            }
        });


        tvlogin.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });


    }

    private boolean isPasswordInputIsOk() {
        return (isv1ok && isv2ok && isv3ok && isv4ok & !password.getEditText().getText().toString().trim().isEmpty());
    }

    private boolean isEmailInputIsOk() {
        return (!email.getEditText().getText().toString().trim().isEmpty() && email.getEditText().getText().toString().trim().endsWith("buksu.edu.ph"));
    }


    private void createNewUser() {
        String userEmail = email.getEditText().getText().toString().trim();
        String userPassword = password.getEditText().getText().toString().trim();

        rAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    rAuth.getCurrentUser().sendEmailVerification(myActionCodeSettings()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            setUpErrorAlertDialog(e.getMessage());
                            registerBtn.showNormalButton();
                        }


                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            createSweetAlertSuccessDialog();
                            registerBtn.showNormalButton();
                            registerBtn.setEnabled(false);

                        }
                    });


                } else {

                    //set up error dialog box
                    String err = ((FirebaseAuthException) task.getException()).getErrorCode();
                    createSweetAlertErrorDialog(err);
                    registerBtn.showNormalButton();


                }

            }
        });
    }


    private void setUpErrorAlertDialog(String errorCode) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RegisterActivity.this, R.style.errorDialog);

        switch (errorCode) {
            case "ERROR_EMAIL_ALREADY_IN_USE":

                builder.setMessage("University email address is already in use by another account.")
                        .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;

            default:
                builder.setMessage(errorCode)
                        .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;


        }

    }

    private ActionCodeSettings myActionCodeSettings() {

        ActionCodeSettings actionCodeSettings;

        actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName("com.dacoders.buksue_libraryapp", false, null)
                .setHandleCodeInApp(true)
                .setUrl("https://e-library-92ba5.firebaseapp.com")
                .build();


        return actionCodeSettings;


    }




    /* private void setUpSuccessDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RegisterActivity.this,R.style.successDialog);
        builder.setMessage(R.string.dialogMessageRegister)
                .setPositiveButton("Open Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                            startActivity(intent);
                        }catch(ActivityNotFoundException e){
                            Toast.makeText(RegisterActivity.this, "No installed apps found to open email", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(RegisterActivity.this,UnverifiedUserLandingActivity.class));

                    }
                }).show();


    }

    */


    @SuppressLint("ResourceAsColor")
    private void createSweetAlertErrorDialog(String errorCode){

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE);
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
            case "ERROR_EMAIL_ALREADY_IN_USE":

                sweetAlertDialog.setContentText("Email is already in use by another account");
                break;




            default:

                sweetAlertDialog.setContentText(errorCode);
                break;




        }







    }





    @SuppressLint("ResourceAsColor")
    private void createSweetAlertSuccessDialog(){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE);
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
                    Toast.makeText(RegisterActivity.this, "No installed apps found to open email", Toast.LENGTH_SHORT).show();
                }



                sweetAlertDialog.dismiss();
            }
        });


        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setOnClickListener(click->{
            sweetAlertDialog.dismiss();
            startActivity(new Intent(RegisterActivity.this,UnverifiedUserLandingActivity.class));



        });

    }







    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}