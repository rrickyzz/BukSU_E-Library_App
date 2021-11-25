package com.dacoders.buksue_libraryapp.BookAccess;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.R;
import com.dacoders.buksue_libraryapp.TabFragments.DownloadFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookOptions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookOptions extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookOptions() {
        // Required empty public constructor
    }

    public BookOptions(Collection_DAO collection_dao) {
        this.book=collection_dao;
        this.book_title = collection_dao.getTitle();
        this.book_author =collection_dao.getAuthor();
        this.url = collection_dao.getFileUrl();
        this.permission = collection_dao.getPermission();

    }
    // TODO: Rename parameter arguments, choose names that match

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookOptions.
     */
    // TODO: Rename and change types and number of parameters
    public static BookOptions newInstance(String param1, String param2) {
        BookOptions fragment = new BookOptions();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    Collection_DAO book;
    String book_title;
    String book_author;
    String url;
    String permission;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_book_options, container, false);
        TextView bookTitle = view.findViewById(R.id.bookOptionsBookTitle);

        TextView bookAuthor = view.findViewById(R.id.bookOptionsBookAuthor);

        MaterialButton readBtn= view.findViewById(R.id.bookOptionsReadBtn);
        MaterialButton downloadBtn = view.findViewById(R.id.bookOptionsDownloadBtn);

        bookTitle.setText(book_title);
        bookAuthor.setText("Author: "+book_author);
        if (permission.equals("Download Permitted")){
            downloadBtn.setEnabled(true);
        }else{
            downloadBtn.setEnabled(false);
            downloadBtn.setTextColor(Color.parseColor("#C4C4C4"));

        }

        readBtn.setOnClickListener(click->{
            AppCompatActivity activity  = (AppCompatActivity) getContext();
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
                    sweetAlertDialog.dismiss();
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    List<Collection_DAO> dl_list = new ArrayList<>();
                    dl_list.add(book);
                    Fragment dl_fragment = new DownloadFragment(dl_list);

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dl_fragment).addToBackStack(null).commit();


                }
            });







        });


        return view;

    }





}

