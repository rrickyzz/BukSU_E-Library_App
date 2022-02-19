package com.dacoders.buksue_libraryapp.BookCollectionAdapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dacoders.buksue_libraryapp.BookCollectionAdapter.Author.ByAuthorFragment;
import com.dacoders.buksue_libraryapp.BookCollectionAdapter.Subject.BySubjectFragment;
import com.dacoders.buksue_libraryapp.BookCollectionAdapter.Location.ByLocationFragment;
import com.dacoders.buksue_libraryapp.BookCollectionAdapter.Section.BySectionFragment;

public class BookCollectionPagerAdapter extends FragmentStateAdapter {


    public BookCollectionPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position+1){
            case 1: return new BySectionFragment();
            case 2: return new ByLocationFragment();
            case 3: return new BySubjectFragment();

        }

        return new BySectionFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
