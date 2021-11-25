package com.dacoders.buksue_libraryapp.TabFragments;

import static java.lang.Thread.sleep;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dacoders.buksue_libraryapp.CollectionAdapter.CollectionMainRecyclerAdapter;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.CollectionModel.Category;
import com.dacoders.buksue_libraryapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView categoryTitleRecycler;
    CollectionMainRecyclerAdapter categoryRecyclerAdapter;
    List<Category> allCategoryList = new ArrayList<>();

    public List<Category> getAllCategoryList() {
        return allCategoryList;
    }

    List<Collection_DAO> list = new ArrayList<>();




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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


        final View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        // Inflate the layout for this fragment





        //add categories

        Category category1 = new Category("English Literature");

        Category category2 = new Category("Mathematics");

        Category category3 = new Category("Filipiniana");

        Category category4 = new Category("History");
        allCategoryList.clear();

        allCategoryList.add(category1);

        allCategoryList.add(category2);

        allCategoryList.add(category3);

        allCategoryList.add(category4);


        categoryTitleRecycler = rootView.findViewById(R.id.collectionMainRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        categoryTitleRecycler.setLayoutManager(layoutManager);
        categoryRecyclerAdapter = new CollectionMainRecyclerAdapter(getActivity(),allCategoryList);

        categoryTitleRecycler.setAdapter(categoryRecyclerAdapter);









        //fetchCategoryList();



        return rootView;
    }









}