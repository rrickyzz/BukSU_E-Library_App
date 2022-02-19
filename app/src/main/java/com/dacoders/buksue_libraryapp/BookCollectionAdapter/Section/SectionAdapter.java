package com.dacoders.buksue_libraryapp.BookCollectionAdapter.Section;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dacoders.buksue_libraryapp.Book.BookModel;
import com.dacoders.buksue_libraryapp.Book.BookModelClass;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter  extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder>{
    Context context;
    List<String> sectionNameList;
    SectionChildAdapter childAdapter;


    public SectionAdapter(Context context, List<String> sectionNameList) {
        this.context = context;
        this.sectionNameList = sectionNameList;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(context).inflate(R.layout.section_recycler_main_items,parent,false);

        return new SectionViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        holder.sectionlabel.setText(sectionNameList.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.sectionRecycler.setLayoutManager(layoutManager);
        holder.sectionRecycler.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("collection").orderByChild("section").equalTo(holder.sectionlabel.getText().toString());

        FirebaseRecyclerOptions<BookModelClass> options = new FirebaseRecyclerOptions.Builder<BookModelClass>()
                .setQuery(query,BookModelClass.class).build();





        childAdapter = new SectionChildAdapter(options);
        childAdapter.startListening();
        childAdapter.notifyDataSetChanged();
        holder.sectionRecycler.setAdapter(childAdapter);






    }

    @Override
    public int getItemCount() {
        return sectionNameList.size();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder{

        TextView sectionlabel;
        RecyclerView sectionRecycler;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);

            sectionlabel = itemView.findViewById(R.id.sectionRowItemTextView);
            sectionRecycler = itemView.findViewById(R.id.sectionChildRecyclerView);


        }
    }



}



