package com.dacoders.buksue_libraryapp.BookSearchHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dacoders.buksue_libraryapp.Book.BookModelClass;
import com.dacoders.buksue_libraryapp.BookAccess.BookOptions;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

import me.grantland.widget.AutofitTextView;

public class SearchResultRecyclerAdapter extends FirebaseRecyclerAdapter<BookModelClass,SearchResultRecyclerAdapter.SearchResultViewHolder>  {



    AppCompatActivity activity;

    public SearchResultRecyclerAdapter(FirebaseRecyclerOptions<BookModelClass> bookModelClassFirebaseRecyclerOptions) {
        super(bookModelClassFirebaseRecyclerOptions);
    }


    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item,parent,false);

        activity = (AppCompatActivity) view.getContext();

        return new SearchResultViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position, @NonNull BookModelClass model) {



       holder.title.setText(model.getTitle());
       holder.author.setText(model.getAuthor());



        holder.frame.setOnClickListener(click->{

            Fragment bookOptions = new BookOptions(model);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,bookOptions).addToBackStack(null).commit();

        });


    }


    public  class  SearchResultViewHolder extends RecyclerView .ViewHolder{


        View frame;
        AutofitTextView title,author;


        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            frame = itemView.findViewById(R.id.search_result_item_frame);
            title = itemView.findViewById(R.id.searchResultItemBookTitle);
            author = itemView.findViewById(R.id.searchResultItemBookAuthor);

        }
    }
}
