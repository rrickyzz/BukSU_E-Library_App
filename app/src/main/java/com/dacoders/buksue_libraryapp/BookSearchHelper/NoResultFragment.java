package com.dacoders.buksue_libraryapp.BookSearchHelper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dacoders.buksue_libraryapp.R;
import com.dacoders.buksue_libraryapp.TabFragments.SearchFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String key;
    TextView textView,searchAgain;

    public NoResultFragment(String key) {
        this.key = key;
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoResultFragment newInstance(String param1, String param2) {
        NoResultFragment fragment = new NoResultFragment();
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
        View view  = inflater.inflate(R.layout.fragment_no_result, container, false);
        textView = view.findViewById(R.id.noResultFoundTextView);
        textView.setText(textView.getText()+"\" "+key+"\" ");

        return view;


    }




}

