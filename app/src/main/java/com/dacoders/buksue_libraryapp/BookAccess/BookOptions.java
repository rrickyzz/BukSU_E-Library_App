package com.dacoders.buksue_libraryapp.BookAccess;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.Book.BookModelClass;
import com.dacoders.buksue_libraryapp.BookStatisticsHelper.IndividualBookStatsHelper;
import com.dacoders.buksue_libraryapp.BookStatisticsHelper.StudentReadsRecords;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.FileDownloadHelper.DownloadService;
import com.dacoders.buksue_libraryapp.MainActivity;
import com.dacoders.buksue_libraryapp.R;
import com.dacoders.buksue_libraryapp.TabFragments.DownloadFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookOptions extends Fragment {
    String firstName;
    String lastName;
    String userKey;
    private static final int PERMISSION_CODE = 1 ;
    private String bookKey="";
    private String bookIsbn="";
    boolean bookRecordExist=false;
    private List<String> bookData;
    String currMonthData;
    private IndividualBookStatsHelper bookStats;
    BookModelClass book;
    TextView bookTitle,bookAuthor,pageNumber,noOfDownloads,noOfReads;
    MaterialButton readBtn,downloadBtn;


    public BookOptions() {
        // Required empty public constructor
    }

    public BookOptions(BookModelClass book) {
     this.book = book;

    }

    public static BookOptions newInstance() {
        BookOptions fragment = new BookOptions();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_book_options, container, false);
         bookTitle = view.findViewById(R.id.bookOptionsBookTitleTextView);

         noOfDownloads = view.findViewById(R.id.bookOptionsNumbersOfDownloads);
         noOfReads = view.findViewById(R.id.bookOptionsNumbersOfReads);
         bookAuthor = view.findViewById(R.id.bookOptionsBookAuthorTitleTextView);
        pageNumber = view.findViewById(R.id.bookOptionsNumbersOfPages);
        readBtn= view.findViewById(R.id.bookOptionsReadBtn);
        downloadBtn = view.findViewById(R.id.bookOptionsDownloadBtn);

        bookTitle.setText(book.getTitle());
        pageNumber.setText(book.getPageNumber());
        bookAuthor.setText(book.getAuthor());



        if (book.getPermission().equals("Download Permitted")){
          //  access_type.setText("Free access");
            downloadBtn.setEnabled(true);
        }else{
            downloadBtn.setEnabled(false);
          //  access_type.setText("Read only");
            downloadBtn.setTextColor(Color.parseColor("#C4C4C4"));

        }

       // initPageNumber();


        readBtn.setOnClickListener(click->{
            AppCompatActivity activity  = (AppCompatActivity) getContext();

            //add count to book read stats

            if(bookRecordExist) {

                int readCount = Integer.parseInt(bookStats.getReads())+1;
                IndividualBookStatsHelper dataObj = new IndividualBookStatsHelper(bookStats.getId(), Integer.toString(readCount), bookStats.getDownloads());
                FirebaseDatabase.getInstance().getReference("IndividualBookStats").child(bookKey).setValue(dataObj);
                rewarding();
            }else{

                IndividualBookStatsHelper dataObj = new IndividualBookStatsHelper(bookKey,"1","0");
                FirebaseDatabase.getInstance().getReference("IndividualBookStats").child(bookKey).setValue(dataObj);
                rewarding();
            }
            Fragment fragment = new ReadBookFromUrlFragment(book);
             activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

        });

        downloadBtn.setOnClickListener(click->{



           SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.NORMAL_TYPE);
            sweetAlertDialog.setContentText("Are you sure you want to download this file?");
             sweetAlertDialog.setConfirmText("Download");
            sweetAlertDialog.setCancelText("Cancel");
            sweetAlertDialog.show();

            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.textSecondary);
            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setTextColor(R.color.textSecondary);
            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //add/update download count in database
                    if(bookRecordExist) {

                        int downloadCount = Integer.parseInt(bookStats.getDownloads())+1;
                        IndividualBookStatsHelper dataObj = new IndividualBookStatsHelper(bookStats.getId(),bookStats.getReads(),Integer.toString(downloadCount));
                        FirebaseDatabase.getInstance().getReference("IndividualBookStats").child(bookKey).setValue(dataObj);
                    }else{

                        IndividualBookStatsHelper dataObj = new IndividualBookStatsHelper(bookKey,"0","1");
                        FirebaseDatabase.getInstance().getReference("IndividualBookStats").child(bookKey).setValue(dataObj);
                    }

                    //start download service
                    Intent intent = new Intent(getActivity(),DownloadService.class);
                    intent.putExtra("url",book.getFileUrl());
                    intent.putExtra("filename",book.getTitle());
                    File  file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/BukSU-ELibrary/");
                    intent.putExtra("location",file.getAbsolutePath());
                    getActivity().startForegroundService(intent);



                    //checks if has permission to read storage

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED || checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED){
                            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, PERMISSION_CODE);
                        }

                    }





                    sweetAlertDialog.dismiss();

                }
            });







        });


        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        switch (requestCode){
            case  PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // start download if permission is granted.

                    AppCompatActivity activity = (AppCompatActivity) getContext();

                    List<Collection_DAO> dl_list = new ArrayList<>();
                //    dl_list.add(book);
                    Fragment dl_fragment = new DownloadFragment(dl_list);

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dl_fragment).addToBackStack(null).commit();



                }else {
                    Toast.makeText(getContext(), "Permission to read/write storage must be granted first! ", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private void rewarding(){
        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // Here "image" is the child node value we are getting
        // child node data in the getImage variable
        DatabaseReference getFirstName = databaseReference.child("Student").child(userId).child("firstName");
        DatabaseReference getLastName = databaseReference.child("Student").child(userId).child("lastName");
        DatabaseReference getUserId =  databaseReference.child("Student").child(userId).child("university_id");


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
              BookOptions.this.firstName = snapshot.getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.

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
                BookOptions.this.lastName = snapshot.getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.
            }  @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                //  Toast.makeText(HomeActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        getUserId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                BookOptions.this.userKey = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        StudentReadsRecords obj = new StudentReadsRecords(userId,userKey,firstName,lastName);

        LocalDateTime dateTime = LocalDateTime.now();

       // int dateRegistered = dateTime.getDayOfMonth();
        Month month = dateTime.getMonth();
        String curr_month = month.name();
        int year=  dateTime.getYear();


        // add count for reads per month or update if existing
     //   DatabaseReference r = FirebaseDatabase.getInstance().getReference();
      DatabaseReference r =   FirebaseDatabase.getInstance().getReference("ReaderLeaderboards").child(userId)
                .child(Integer.toString(year));
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(curr_month).exists()){
                  //  Toaster("curr month exist");
                    FirebaseDatabase.getInstance().getReference("ReaderLeaderboards").child(userId)
                            .child(Integer.toString(year)). child(curr_month).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            currMonthData = dataSnapshot.getValue(String.class);

                          /*  FirebaseDatabase.getInstance().getReference("ReaderLeaderboards").child(userId)
                                    .child(Integer.toString(year)).child(curr_month).setValue(Integer.toString(Integer.parseInt(currMonthData)+1));
                          */  HashMap<String,Object> map = new HashMap<>();
                            map.put(curr_month,Integer.toString(Integer.parseInt(currMonthData)+1));
                            FirebaseDatabase.getInstance().getReference("ReaderLeaderboards").child(userId)
                                    .child(Integer.toString(year)).updateChildren(map);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    StudentReadsRecords obj = new StudentReadsRecords(userId,userKey,firstName,lastName);

                    FirebaseDatabase.getInstance().getReference("ReaderLeaderboards").child(userId)
                            .child(Integer.toString(year)).setValue(obj);

                    HashMap<String,Object> map = new HashMap<>();
                    map.put(curr_month,"1");
                    FirebaseDatabase.getInstance().getReference("ReaderLeaderboards").child(userId)
                            .child(Integer.toString(year)).updateChildren(map);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    private void checkIfBookHasRecordsInStats()
    {

        DatabaseReference root = FirebaseDatabase.getInstance().getReference("IndividualBookStats");
        root.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child(bookKey).exists()){

                    FirebaseDatabase.getInstance().getReference("IndividualBookStats")
                            .child(bookKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*         for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                             bookStats.add(snapshot.getValue());
                         } */
                            bookStats = snapshot.getValue(IndividualBookStatsHelper.class);

                            //init no.of downloads textview and no.of read textview

                            noOfDownloads.setText(bookStats.getDownloads());
                            pageNumber.setVisibility(View.VISIBLE);
                            noOfReads.setText(bookStats.getReads());
                            bookRecordExist = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
               else{
                   noOfDownloads.setText("0");
                   noOfReads.setText("0");
                    pageNumber.setVisibility(View.VISIBLE);
                   bookRecordExist = false;


                    //book has no statistic records yet

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }


    private  void initBookData(){
        bookData = new ArrayList<>();


        Query query = FirebaseDatabase.getInstance().getReference("collection").orderByChild("fileUrl").equalTo(book.getFileUrl());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                BookModelClass obj = snapshot.getValue(BookModelClass.class);

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    bookData.add(String.valueOf(dataSnapshot.getValue()));
                    bookKey = dataSnapshot.getKey();



                }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Toaster(String msg){

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {

        initBookData();
        checkIfBookHasRecordsInStats();



        super.onStart();
    }

}

