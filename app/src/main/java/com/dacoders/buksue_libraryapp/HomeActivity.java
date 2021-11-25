package com.dacoders.buksue_libraryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.CollectionAdapter.CollectionMainRecyclerAdapter;
import com.dacoders.buksue_libraryapp.CollectionModel.Category;
import com.dacoders.buksue_libraryapp.DrawerFragments.AboutUsFragment;
import com.dacoders.buksue_libraryapp.DrawerFragments.ProfileFragment;
import com.dacoders.buksue_libraryapp.NoNetworkConnectivityHelper.NetworkChangeReceiver;
import com.dacoders.buksue_libraryapp.NoNetworkConnectivityHelper.NoInternetFragment;
import com.dacoders.buksue_libraryapp.TabFragments.HomeFragment;
import com.dacoders.buksue_libraryapp.TabFragments.DownloadFragment;
import com.dacoders.buksue_libraryapp.TabFragments.SearchFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NetworkChangeReceiver.ReceiverListener {

    private boolean ConnectedToNetwork;
    private  boolean isDownloadFragment;
    private RecyclerView categoryTitleRecycler;
    CollectionMainRecyclerAdapter categoryRecyclerAdapter;
     Dialog dialog2;   //variable for uploading pic

    private  String profilePicLink;
    private  FirebaseAuth mauth;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout linearLayout;
    private  static boolean isCamOpen = false;
    private  static boolean isGalleryOpen = false;
    private  Uri image_uri;
    private  static final int IMAGE_PICK_CODE = 1000;
    private  static final int PERMISSION_CODE = 1001;
    private  static final int IMAGE_CAPTURE_CODE = 1001;
    private CircleImageView profilePic;
    private View headerParentLayout ;
    private TextView displayName;
    private ImageView changeDp;
    private TextView universityEmailTextView;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private com.jb.dev.progress_indicator.dotGrowProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mauth = FirebaseAuth.getInstance();
        initToolBarAndDrawer();
        checkConnection(); // check internet connectivity

        checkIfUserHasInfoInDatabase();

       }

    @Override
    protected void onStart() {
      super.onStart();

      checkConnection();

        if(ConnectedToNetwork){}else{
            // start no internet connectivity landing page
            gotoNoInternetLandingPage();

        }



    }

    @Override
    protected void onResume() {

        checkConnection();


        super.onResume();


        if(ConnectedToNetwork){

        }else{
            // start no internet connectivity landing page
                 gotoNoInternetLandingPage();

        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        checkConnection();
    }



    private void hasInternetConnection(boolean isConnected){

        ConnectedToNetwork = isConnected;


    }



    private void gotoNoInternetLandingPage(){


        Fragment fragment = new NoInternetFragment();
        HomeActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();


    }


    private void initCategoryRecyclerView(List<Category> categoryList){

           categoryTitleRecycler = findViewById(R.id.collectionMainRecyclerView);
           RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
           categoryTitleRecycler.setLayoutManager(layoutManager);
           categoryRecyclerAdapter = new CollectionMainRecyclerAdapter(this,categoryList);
           categoryTitleRecycler.setAdapter(categoryRecyclerAdapter);



       }




    private void initToolBarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        linearLayout = findViewById(R.id.home_linearLayout);
        drawerLayout = findViewById(R.id.home_activityDrawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        bottomNavigationView = findViewById(R.id.bottomNavView);
        navigationView = findViewById(R.id.navView);
        headerParentLayout = navigationView.getHeaderView(0);
        profilePic = headerParentLayout.findViewById(R.id.profilePic_imageView);
        displayName = headerParentLayout.findViewById(R.id.profileDisplayNameTextView);
        universityEmailTextView = headerParentLayout.findViewById(R.id.profileEmailTextView);
        changeDp = headerParentLayout.findViewById(R.id.changeProfilePicBtn);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        isDownloadFragment=false;
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);


        //init profile pic

        initProfilePic();
        initChangeProfilePicBtn();

        //set email text view

        initName();

        universityEmailTextView.setText(mauth.getCurrentUser().getEmail() );




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_profile:

                        // goto profile fragment



                        if(ConnectedToNetwork){

                            Fragment profileFragment = new ProfileFragment(profilePicLink);
                            HomeActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).addToBackStack(null).commit();

                        }else{
                            // start no internet connectivity landing page
                            gotoNoInternetLandingPage();

                        }



                         break;

                        case R.id.menu_feedback:

                            if(ConnectedToNetwork){

                                createRatingDialog();


                            }else {
                                // start no internet connectivity landing page
                                gotoNoInternetLandingPage();

                            }

                        break;


                        case R.id.menu_about_us:


                        // goto  about us fragment

                        Fragment fragment = new AboutUsFragment();

                        HomeActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();


                        break;
                    case R.id.menu_logout:



                         if(ConnectedToNetwork){


                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this,SweetAlertDialog.NORMAL_TYPE);
                        sweetAlertDialog.setContentText("Are you sure you want to sign out?");
                        sweetAlertDialog.setConfirmText("Sign out");
                        sweetAlertDialog.setCancelText("Cancel");
                        sweetAlertDialog.show();

                        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.textSecondary);
                        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setTextColor(R.color.textSecondary);
                        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
                                                                                                           @Override
                                                                                                           public void onClick(View view) {

                                                                                                               mauth.signOut();

                                                                                                               startActivity(new Intent(HomeActivity.this,MainActivity.class));

                                                                                                           }});   }
                         else{
                             // start no internet connectivity landing page
                             gotoNoInternetLandingPage();

                         }
                     break;
                }
                 drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:

                        fragment = new HomeFragment();
                        isDownloadFragment = false;


                        break;

                    case R.id.bottom_nav_search:
                        fragment = new SearchFragment();
                        isDownloadFragment = false;

                        break;

                    case R.id.bottom_nav_downloads:
                        fragment = new DownloadFragment();
                        isDownloadFragment = true;

                        break;
                }

                if(ConnectedToNetwork){

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();  }


                else if(isDownloadFragment){

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();  }



                else{

                    // start no internet connectivity landing page
                    gotoNoInternetLandingPage();

                }
                return true;

            }










        });





    }







    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createRatingDialog(){
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_feedback);
        final EditText messageBox = dialog.findViewById(R.id.feedBackMessageBox);
        final MaterialButton submitBtn = dialog.findViewById(R.id.submitFeedback);
        final MaterialButton cancelSubmitBtn = dialog.findViewById(R.id.cancelFeedback);

        messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {

                if(messageBox.getText().toString().trim().equals("")){
                    submitBtn.setEnabled(false);

                }else{
                    submitBtn.setEnabled(true);
                 }

            }
        });
        
        
        submitBtn.setOnClickListener(click->{
            String feedback = messageBox.getText().toString().trim();
             dialog.dismiss();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            LocalDateTime currentTime  = LocalDateTime.now();



            submitFeedback(feedback,String.valueOf(dtf.format(currentTime)));

        });

        cancelSubmitBtn.setOnClickListener(click->{
            dialog.dismiss();
        });
        

        dialog.show();






    }

    private void submitFeedback(String message,String time){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        String userId = mauth.getUid();
        FeedBackModel feedBackModel = new FeedBackModel(message,time);


        databaseReference.child("Feedbacks").child(userId).setValue(feedBackModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


                createSweetAlertDialog(1,"Feedback submitted successfully!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                createSweetAlertDialog(0,"Error on submitting feedback!");



            }
        });





    }


    @SuppressLint("ResourceAsColor")
    private void createSweetAlertDialog(int type, String contentMessage){



        if(type==1) {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setContentText(contentMessage);
             sweetAlertDialog.setConfirmText("Okay");
            sweetAlertDialog.show();
            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.textSecondary);


            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sweetAlertDialog.dismiss();
                }
            });

        }
        if(type==0){
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setContentText(contentMessage);
            sweetAlertDialog.setConfirmText("Okay");
            sweetAlertDialog.show();

            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sweetAlertDialog.dismiss();
                }
            });

        }

    }










    private void setupCollegeSpinner(Spinner spinner){

        List<String> college = new ArrayList<>();
        college.add("College of Technology");

        college.add("College of Art and Sciences");

        college.add("College of Administration");

        college.add("College of Nursing");

        college.add("College of Business");

        college.add("College of Education");

        college.add("College of Law");

        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,college);
        collegeAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(collegeAdapter);



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

    private void initProfilePic(){

        // we will get the default FirebaseDatabase instance
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        String userId = mauth.getUid();


        // Here "image" is the child node value we are getting
        // child node data in the getImage variable
        DatabaseReference getImage = databaseReference.child("Student").child(userId).child("profile_pic_link");

        // Adding listener for a single change
        // in the data at this location.
        // this listener will triggered once
        // with the value of the data at the location
        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // getting a DataSnapshot for the location at the specified
                // relative path and getting in the link variable
                profilePicLink = dataSnapshot.getValue(String.class);


                // loading that data into profilePic
                // variable which is ImageView
                Picasso.get().load(profilePicLink).into(profilePic);
            }

            // this will called when any problem
            // occurs in getting data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // we are showing that error message in toast
                Toast.makeText(HomeActivity.this, "Error Loading Profile Photo", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initName() {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        String userId = mauth.getUid();


        // Here "image" is the child node value we are getting
        // child node data in the getImage variable
        DatabaseReference getFirstName = databaseReference.child("Student").child(userId).child("firstName");
        DatabaseReference getLastName = databaseReference.child("Student").child(userId).child("lastName");



        // calling add value event listener method
        // for getting the value of "firstName" from database.
        getFirstName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String value = snapshot.getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.
                displayName.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                //  Toast.makeText(HomeActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


        // calling add value event listener method
        // for getting the value of "lastName" from database.
        getLastName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String value = snapshot.getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.
                displayName.setText(displayName.getText()+" "+value);
            }  @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                //  Toast.makeText(HomeActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

        private void setUpFormDialog(){
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_items);
        final Spinner collegeSpinner = dialog.findViewById(R.id.form_course_spinner);
        final Spinner yearSpinner = dialog.findViewById(R.id.form_course_year);
        final TextInputLayout input_ID = dialog.findViewById(R.id.form_id_num);
        final TextInputLayout input_firstName = dialog.findViewById(R.id.form_userFirstName);
        final TextInputLayout input_lastName = dialog.findViewById(R.id.form_userLastName);
        final TextInputLayout input_displayName = dialog.findViewById(R.id.form_userName);
        final TextInputLayout input_mobileNumber = dialog.findViewById(R.id.form_mobile_num);
        final RadioGroup gender = dialog.findViewById(R.id.gender);
        final MaterialButton submitBTn = dialog.findViewById(R.id.form_submit_btn);




        submitBTn.setOnClickListener(v -> {
         if(input_ID.getEditText().getText().toString().trim().isEmpty()){
                input_ID.setError("this field cannot be empty!");
                input_ID.requestFocus();
                return;
            }else if((!input_ID.getEditText().getText().toString().trim().isEmpty())&&input_ID.getEditText().getText().toString().length()!=10){
                input_ID.setError("invalid id");
                input_ID.requestFocus();
                return;
            }
            else if(input_firstName.getEditText().getText().toString().trim().isEmpty()){
                input_firstName.setError("this field cannot be empty!");
                input_firstName.requestFocus();
                return;
            }

         else if(input_lastName.getEditText().getText().toString().trim().isEmpty()){
             input_lastName.setError("this field cannot be empty!");
             input_lastName.requestFocus();
             return;
         }
            else if(input_displayName.getEditText().getText().toString().trim().isEmpty()){
                input_displayName.setError("this field cannot be empty!");
                input_displayName.requestFocus();
                return;
            }else if(input_mobileNumber.getEditText().getText().toString().trim().isEmpty()){
                input_mobileNumber.setError("this field cannot be empty");
                return;
            }

            else if(gender.getCheckedRadioButtonId()==-1){
                gender.requestFocus();
                Toast.makeText(this, "Choose your gender", Toast.LENGTH_LONG).show();
                return;
            }
            else{

                 //get gender value
             String genderValue="";
                switch (gender.getCheckedRadioButtonId()){
                    case R.id.male:
                        genderValue = "Male";
                        break;
                    case R.id.female:
                        genderValue = "Female";
                        break;

                }
                // submit details
                addUserInfoToRealtimeDatabase(
                                                mauth.getUid(),
                                               input_ID.getEditText().getText().toString().trim(),
                                               "",
                                               input_firstName.getEditText().getText().toString().trim(),
                                                input_lastName.getEditText().getText().toString().trim(),
                                                input_displayName.getEditText().getText().toString(),
                                                 input_mobileNumber.getEditText().getText().toString().trim(),
                                                 genderValue,
                                                 collegeSpinner.getSelectedItem().toString(),
                                                 yearSpinner.getSelectedItem().toString()
                        );
                dialog.dismiss();
                setUpUploadProfilePicDialog();
            }
            dialog.dismiss();
            setUpUploadProfilePicDialog();


        });
        input_ID.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!input_ID.getEditText().toString().trim().isEmpty()||input_ID.getEditText().toString().trim().length()==10){
                    input_ID.setError("");
                }
            }
        });

        input_firstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!input_firstName.getEditText().toString().trim().isEmpty())
                {
                    input_firstName.setError("");
                }
            }
        });


        input_lastName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!input_lastName.getEditText().toString().trim().isEmpty())
                {
                    input_lastName.setError("");
                }
            }
        });

        input_displayName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!input_displayName.getEditText().toString().isEmpty()){
                    input_displayName.setError("");
                }
            }
        });

        input_mobileNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!input_mobileNumber.getEditText().toString().isEmpty())
                {
                    input_mobileNumber.setError("");
                }
            }
        });



        setupCollegeSpinner(collegeSpinner);
        setupYearSpinner(yearSpinner);
        dialog.show();

    }


    private void setUpUploadProfilePicDialog(){

        dialog2 = new Dialog(HomeActivity.this);
        dialog2.setCancelable(false);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_upload_profile_pic);
        final MaterialButton btn_uploadPicFromGallery = dialog2.findViewById(R.id.uploadFromGallery);
        final MaterialButton btn_capturePhoto = dialog2.findViewById(R.id.captureProfilePic);
        ImageView closeBTn = dialog2.findViewById(R.id.closeDialogUpload);

        closeBTn.setOnClickListener(click->{
            dialog2.dismiss();
        });

        btn_capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCamOpen = true;
                isGalleryOpen = false;
                captureImageForProfile();
                dialog2.dismiss();
            }
        });

        btn_uploadPicFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGalleryOpen = true;
                isCamOpen = false;
                uploadImageForProfile();
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }


    private void captureImageForProfile(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED||
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
        profilePic.setImageURI(image_uri);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            if(isGalleryOpen) {
               // profilePic.setImageURI(data.getData());
              //  System.out.println("Adding image to db");
                addProfileImageToStorage(data.getData());
               // image_uri = data.getData();
            }
         }
        if(resultCode==RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            if(isCamOpen){
              //  profilePic.setImageURI(image_uri);

             //   System.out.println("Adding image to db");
               addProfileImageToStorage(image_uri);

            }
        }
    }




    private void addUserInfoToRealtimeDatabase(String uid, String university_id, String profile_pic_link, String firstName, String lastName, String displayName, String mobile, String gender, String college, String year)
    {
        DBRef dbRef = new DBRef();
        StudentModelClass student = new StudentModelClass(uid,university_id,profile_pic_link,firstName,lastName,displayName,mobile,gender,college,year);
        dbRef.addStudent(student).addOnSuccessListener(suc->{
            Toast.makeText(this, "New student  has been added successfully.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail->{
            Toast.makeText(this, "Error"+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    private void addProfileImageToStorage(Uri uri){
        //progress dialog for uploading image

        SweetAlertDialog loadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.setContentText("Uploading photo....");
        loadingDialog.getProgressHelper().setBarColor(R.color.buksuSecondary);
        loadingDialog.setCancelable(false);




        StorageReference fileRef = storageReference.child("ProfilePhotos").child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                loadingDialog.dismiss();



                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // add link of profile picture to the realtime database


                        String userId = mauth.getUid();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                        reference.child("Student").child(userId).child("profile_pic_link").setValue(uri.toString());

                        createSweetAlertDialog(1,"Photo has been uploaded successfully.");




                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                loadingDialog.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();


                createSweetAlertDialog(1,"Error occurred on uploading your photo.");


            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void checkIfUserHasInfoInDatabase()
    {

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = root.child("Student");
        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    // user filled information, don't show the dialog

                }else{

                    //user has not submitted information yet
                    setUpFormDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }

    private  void initChangeProfilePicBtn(){
        changeDp.setOnClickListener(click->{



            setUpUploadProfilePicDialog();
        });
    }

    @Override
    public void onNetworkChange(boolean isConnected) {

        // calls when there is changes in network connectivity

        Toast.makeText(this, "Network: "+isConnected, Toast.LENGTH_SHORT).show();

        hasInternetConnection(isConnected);



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

        ConnectedToNetwork = isConnected;

    }


}
