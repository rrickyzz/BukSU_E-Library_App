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

import com.dacoders.buksue_libraryapp.BookAccess.BookOptions;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class SearchResultRecyclerAdapter extends FirebaseRecyclerAdapter<Collection_DAO,SearchResultRecyclerAdapter.SearchResultViewHolder>  {



    AppCompatActivity activity;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SearchResultRecyclerAdapter(@NonNull FirebaseRecyclerOptions<Collection_DAO> options) {
        super(options);
    }


    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item,parent,false);

        activity = (AppCompatActivity) view.getContext();

        return new SearchResultViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position, @NonNull Collection_DAO model) {



        holder.title.setText("Title: "+model.getTitle());
        holder.category.setText("Category: "+model.getCategory());
        holder.author.setText("Author: "+model.getAuthor());





        holder.frame.setOnClickListener(click->{

            Fragment bookOptions = new BookOptions(model);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,bookOptions).addToBackStack(null).commit();

        });


    }


    public  class  SearchResultViewHolder extends RecyclerView .ViewHolder{


        View frame;
        TextView title,category,author;


        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            frame = itemView.findViewById(R.id.search_result_item_frame);
            title = itemView.findViewById(R.id.search_result_item_Title);
            category = itemView.findViewById(R.id.search_result_item_category);
            author = itemView.findViewById(R.id.search_result_item_author);
        }
    }
}
