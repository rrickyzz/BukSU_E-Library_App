package com.dacoders.buksue_libraryapp.TabFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dacoders.buksue_libraryapp.BookCollectionAdapter.BookCollectionPagerAdapter;
import com.dacoders.buksue_libraryapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookCollectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    BookCollectionPagerAdapter adapter;
    List<String>  tabTitles;

    public BookCollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookCollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookCollectionFragment newInstance(String param1, String param2) {
        BookCollectionFragment fragment = new BookCollectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_book_collection, container, false);
        tabLayout = view.findViewById(R.id.bookCollectionTabLayout);
        viewPager2 = view.findViewById(R.id.bookCollectionViewPagerMain);
      //  FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = new BookCollectionPagerAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(false);
        tabTitles = new ArrayList<>();
        tabTitles.add("Section");
        tabTitles.add("Location");
        tabTitles.add("Subject");


        for(String title : tabTitles){
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
/*
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                    tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        }); */




        return view;
    }
}