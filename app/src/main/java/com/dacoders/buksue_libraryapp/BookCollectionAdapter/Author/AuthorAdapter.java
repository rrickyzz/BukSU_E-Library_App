package com.dacoders.buksue_libraryapp.BookCollectionAdapter.Author;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dacoders.buksue_libraryapp.R;

import java.util.List;

public class AuthorAdapter extends  RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder>{

    Context context;
    List<String> authorNameList;

    public AuthorAdapter(Context context, List<String> authorNameList) {
        this.context = context;
        this.authorNameList = authorNameList;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(context).inflate(R.layout.author_recycler_main_items,parent,false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        holder.authorLabel.setText(authorNameList.get(position));

    }

    @Override
    public int getItemCount() {
        return authorNameList.size();
    }


    public static class AuthorViewHolder extends RecyclerView.ViewHolder{

        TextView authorLabel;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            authorLabel = itemView.findViewById(R.id.authorRowItemTextView);
        }
    }
}
