package com.dacoders.buksue_libraryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProfileActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private Spinner spinnerCourse,spinnerYear;
    private ImageView changeProfilePic,changeCourse,changeYear;
    private EditText displayName_et,fullName_et;
    private  static boolean isCamOpen = false;
    private  static boolean isGalleryOpen = false;
    private Uri image_uri;
    private  static final int IMAGE_PICK_CODE = 1000;
    private  static final int PERMISSION_CODE = 1001;
    private  static final int IMAGE_CAPTURE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        displayName_et = findViewById(R.id.editProfileDisplayNameEditText);
        fullName_et = findViewById(R.id.editProfileFullNameEditText);
        changeProfilePic = findViewById(R.id.editProfileChangeProfilePicImageView);
        changeCourse = findViewById(R.id.editProfileChangeCourseImageView);
        changeYear = findViewById(R.id.editProfileChangeYearImageView);
        spinnerCourse = findViewById(R.id.editProfileCourse_spinner);
        spinnerYear = findViewById(R.id.editProfileYear_spinner);
        linearLayout = findViewById(R.id.changeProfileParentLayout);


        changeCourse.setOnClickListener(v->{
             setupCourseSpinner(spinnerCourse);

        });


        changeYear.setOnClickListener(v->{
            setupYearSpinner(spinnerYear);
        });

        changeProfilePic.setOnClickListener(v -> {
            setUpUploadProfilePicDialog();
        });





    }

    public void setupYearSpinner(Spinner spinner){

        List<String> year = new ArrayList<>();
        year.add("First Year");
        year.add("Second Year");
        year.add("Third Year");
        year.add("Fourth Year");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,year);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(yearAdapter);

    }

    private void setupCourseSpinner(Spinner spinner){

        List<String> course = new ArrayList<>();
        course.add("Bachelor of Science in Information Technology");
        course.add("Bachelor of Science in Education Major in English");
        course.add("Bachelor of Science in Business Administration");

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,course);
        courseAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(courseAdapter);



    }


    private void setUpUploadProfilePicDialog(){

        final Dialog dialog = new Dialog(ChangeProfileActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_upload_profile_pic);
        final MaterialButton btn_uploadPicFromGallery = dialog.findViewById(R.id.uploadFromGallery);
        final MaterialButton btn_capturePhoto = dialog.findViewById(R.id.captureProfilePic);

        btn_capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCamOpen = true;
                isGalleryOpen = false;
                captureImageForProfile();
                dialog.dismiss();
            }
        });

        btn_uploadPicFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGalleryOpen = true;
                isCamOpen = false;
                uploadImageForProfile();
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    private void captureImageForProfile(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                //Permission is not granted yet, request new.
                String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_CODE);
            }else{
                openCamera();
            }

        }else{

            openCamera();
        }



    }

    private void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"capture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"profile pic for buksu e library");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //camera intent
        Intent cameraIntent  = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
       // profilePic.setImageURI(image_uri);

    }



    private void uploadImageForProfile(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_CODE);
            }else{
                pickImage();
            }

        }else{
            pickImage();
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            if(isGalleryOpen) {
               // profilePic.setImageURI(data.getData());
            }
        }
        if(resultCode==RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            if(isCamOpen){
               // profilePic.setImageURI(image_uri);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(isCamOpen){
                        openCamera();
                    }
                    if(isGalleryOpen){
                        pickImage();
                    }

                }
                else{
                    Snackbar.make(linearLayout,"Permission Access Denied", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }


    private  void pickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);

    }












}