package com.dacoders.buksue_libraryapp.DrawerFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.BookCollectionAdapter.Section.BySectionFragment;
import com.dacoders.buksue_libraryapp.R;
import com.dacoders.buksue_libraryapp.StudentModelClass;
import com.dacoders.buksue_libraryapp.TabFragments.NewsFeedFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    StudentModelClass student;
    Spinner collegeSpinner,yearSpinner;

    TextInputLayout univ_id, firstName,lastName,displayName,mobileNo;
    RadioGroup gender;
    MaterialButton save,cancel;
    List<String> collegeList = new ArrayList<>();
    List<String> year = new ArrayList<>();
    String profilePicLink;

    public ProfileFragment(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {



        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        univ_id = view.findViewById(R.id.form_edit_id_num);
        firstName = view.findViewById(R.id.form_edit_userFirstName);
        lastName = view.findViewById(R.id.form_edit_userLastName);
        displayName = view.findViewById(R.id.form_edit_userName);
        mobileNo = view.findViewById(R.id.form_edit_mobile_num);
        collegeSpinner = view.findViewById(R.id.form_edit_course_spinner);
        yearSpinner = view.findViewById(R.id.form_edit_course_year);
        cancel = view.findViewById(R.id.edit_cancel);
        gender = view.findViewById(R.id.edit_gender);
        save = view.findViewById(R.id.form_edit_save);

        //return to home when user cancel to make profile changes

        cancel.setOnClickListener(click->{
            returnToHomeFragment();
        });


        setupCollegeSpinner(collegeSpinner);
        setupYearSpinner(yearSpinner);
        initValuesForStudentObject();
        checkInputBeforeSubmit();





        return  view;

    }



    private  void returnToHomeFragment(){
        // goto home when user cancel to make profile changes

            Fragment homeFragment = new NewsFeedFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).addToBackStack(null).commit();




    }


    private void setupCollegeSpinner(Spinner spinner){

         collegeList.add("College of Technology");

        collegeList.add("College of Art and Sciences");

        collegeList.add("College of Administration");

        collegeList.add("College of Nursing");

        collegeList.add("College of Business");

        collegeList.add("College of Education");

        collegeList.add("College of Law");

        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<>(collegeSpinner.getContext(),R.layout.spinner_item,collegeList);
        collegeAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(collegeAdapter);



    }

    private void setupYearSpinner(Spinner spinner){

        year.add("First Year");
        year.add("Second Year");
        year.add("Third Year");
        year.add("Fourth Year");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(yearSpinner.getContext(),R.layout.spinner_item,year);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(yearAdapter);

    }




    private void initValuesForStudentObject(){


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();



        DatabaseReference studentReference= databaseReference.child("Student").child(FirebaseAuth.getInstance().getUid());

        studentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                student = snapshot.getValue(StudentModelClass.class);
                univ_id.getEditText().setText(student.getUniversity_id());
                firstName.getEditText().setText(student.getFirstName());
                displayName.getEditText().setText(student.getDisplayName());
                lastName.getEditText().setText(student.getLastName());
                mobileNo.getEditText().setText(student.getMobile());

                //set college spinner initial value using info from database
                int position = 0;

                for(String college: collegeList ){
                    if(college.equals(student.getCollege())){
                        break;
                    }
                    position++;
                }

                int position2 = 0;

                for(String s: year){
                    if(s.equals(student.getYear())){
                        break;
                    }
                    position2++;
                }


                collegeSpinner.setSelection(position);
                yearSpinner.setSelection(position2);

                //set gender radio button based on the info from db

                switch (student.getGender()){
                    case  "Male":
                        gender.check(R.id.edit_male);
                        break;

                    case "Female:":
                        gender.check(R.id.edit_female);
                        break;

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }


    private  void checkInputBeforeSubmit(){

        save.setOnClickListener(v -> {
            if(univ_id.getEditText().getText().toString().trim().isEmpty()){
                univ_id.setError("this field cannot be empty!");
                univ_id.requestFocus();
            }else if((!univ_id.getEditText().getText().toString().trim().isEmpty())&&univ_id.getEditText().getText().toString().length()!=10){
                univ_id.setError("invalid id");
                univ_id.requestFocus();
            }
            else if(firstName.getEditText().getText().toString().trim().isEmpty()){
                firstName.setError("this field cannot be empty!");
                firstName.requestFocus();
            }

            else if(lastName.getEditText().getText().toString().trim().isEmpty()){
                lastName.setError("this field cannot be empty!");
                lastName.requestFocus();
            }
            else if(displayName.getEditText().getText().toString().trim().isEmpty()){
                displayName.setError("this field cannot be empty!");
                displayName.requestFocus();
            }else if(mobileNo.getEditText().getText().toString().trim().isEmpty()){
                mobileNo.setError("this field cannot be empty");
            }
            else if(gender.getCheckedRadioButtonId()==-1){
                gender.requestFocus();
                Toast.makeText(getContext(), "Choose your gender", Toast.LENGTH_LONG).show();
            }
            else{
                //get gender value
                String genderValue="";
                switch (gender.getCheckedRadioButtonId()){
                    case R.id.edit_male:
                        genderValue = "Male";
                        break;
                    case R.id.edit_female:
                        genderValue = "Female";
                         break;

                }
                // submit details
                saveUserProfileChangesToRealtimeDatabase(
                        FirebaseAuth.getInstance().getUid(),
                        univ_id.getEditText().getText().toString().trim(), profilePicLink,
                        firstName.getEditText().getText().toString().trim(),
                        lastName.getEditText().getText().toString().trim(),
                        displayName.getEditText().getText().toString(),
                        mobileNo.getEditText().getText().toString().trim(),
                        genderValue,
                        collegeSpinner.getSelectedItem().toString(),
                        yearSpinner.getSelectedItem().toString()



                );
              }


        });
        univ_id.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!univ_id.getEditText().toString().trim().isEmpty()||univ_id.getEditText().toString().trim().length()==10){
                    univ_id.setError("");
                }
            }
        });

        firstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!firstName.getEditText().toString().trim().isEmpty())
                {
                    firstName.setError("");
                }
            }
        });


        lastName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!lastName.getEditText().toString().trim().isEmpty())
                {
                    lastName.setError("");
                }
            }
        });

        displayName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!displayName.getEditText().toString().isEmpty()){
                    displayName.setError("");
                }
            }
        });

        mobileNo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!mobileNo.getEditText().toString().isEmpty())
                {
                    mobileNo.setError("");
                }
            }
        });

          }


    @SuppressLint("ResourceAsColor")
    private void saveUserProfileChangesToRealtimeDatabase(String uid, String university_id, String profile_pic_link, String firstName, String lastName, String displayName, String mobile, String gender, String college, String year) {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();


        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setContentText("Are you sure you want to save profile changes?");
        sweetAlertDialog.setConfirmText("Proceed");
        sweetAlertDialog.setCancelText("Cancel");
        sweetAlertDialog.show();

        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.textSecondary);
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setTextColor(R.color.textSecondary);
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sweetAlertDialog.dismiss();

                DatabaseReference studentReference= databaseReference.child("Student").child(FirebaseAuth.getInstance().getUid());

                StudentModelClass student = new StudentModelClass(uid,university_id,profile_pic_link,firstName,lastName,displayName,mobile,gender,college,year);

                studentReference.setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        createSweetAlertDialog(1,"Profile Changes Saved!");

                        // return to home fragment after successful profile changes

                        returnToHomeFragment();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        createSweetAlertDialog(0,"Error on saving your profile!");
                    }
                });


            }
        });







    }




    @SuppressLint("ResourceAsColor")
    private void createSweetAlertDialog(int type, String contentMessage){



        if(type==1) {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
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
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
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




}