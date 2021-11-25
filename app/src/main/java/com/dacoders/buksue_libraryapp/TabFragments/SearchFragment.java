package com.dacoders.buksue_libraryapp.TabFragments;

import static com.dacoders.buksue_libraryapp.R.color.lightGrey;
import static com.dacoders.buksue_libraryapp.R.color.yellow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.BookAccess.BookOptions;
import com.dacoders.buksue_libraryapp.BookSearchHelper.SearchResultFragment;
import com.dacoders.buksue_libraryapp.BookSearchHelper.SearchResultRecyclerAdapter;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.CollectionModel.Category;
import com.dacoders.buksue_libraryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;


    private List<String> lastSearches;
    private MaterialSearchBar searchBar;
    private FloatingActionButton searchBtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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


        View view  = inflater.inflate(R.layout.fragment_search, container, false);
        searchBar = view.findViewById(R.id.searchBar);
        searchBtn = view.findViewById(R.id.floatingActionBtnSearch);





        searchBar.setHint("Search here.");
        searchBar.setPlaceHolder("Search here.");
        searchBar.setSpeechMode(true);


  //      searchBar.setSearchIcon(R.drawable.ic_baseline_search_24);


        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


        searchBtn.setOnClickListener(click->{

            if(!searchBar.getText().isEmpty()) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment searchResultFragment = new SearchResultFragment(searchBar.getText().trim());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchResultFragment).addToBackStack(null).commit();
            }

        });


        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {


            }

            @Override
            public void onSearchConfirmed(CharSequence text) {




              //  searchBook(text,searchFilterList);

                if(!searchBar.getText().isEmpty()) {

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment searchResultFragment = new SearchResultFragment(String.valueOf(text).trim());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchResultFragment).addToBackStack(null).commit();

                }



            }








            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode){
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                       // drawer.openDrawer(Gravity.LEFT);
                        Toast.makeText(getActivity(), "Button nav", Toast.LENGTH_SHORT).show();
                        break;
                    case MaterialSearchBar.BUTTON_SPEECH:
                     
                         openVoiceRecognizer();
                }

            }
        });


        //restore last queries from disk
      //  lastSearches = loadSearchSuggestionFromDisk();

        lastSearches = new ArrayList<>();
        lastSearches.add("Sample last searches 1");

        lastSearches.add("Sample last searches 2");

        searchBar.setLastSuggestions(lastSearches);

        return  view;
    }

    private  void openVoiceRecognizer(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        if(intent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivityForResult(intent,10);
        }else{
            Toast.makeText(getActivity(), "Speech Recognition is not supported on your current device", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if(resultCode == RESULT_OK && data!=null ){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchBar.setText(result.get(0));
                    if(!searchBar.getText().isEmpty()) {

                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment searchResultFragment = new SearchResultFragment(searchBar.getText().trim());
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchResultFragment).addToBackStack(null).commit();

                    }
                }
        }
    }
} 