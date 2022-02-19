package com.dacoders.buksue_libraryapp.BookCollectionAdapter.Subject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dacoders.buksue_libraryapp.Book.BookModelClass;
import com.dacoders.buksue_libraryapp.BookCollectionAdapter.Location.LocationChildAdapter;
import com.dacoders.buksue_libraryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    Context context;
    List<String> subjectNameList;
    SubjectChildAdapter childAdapter;

    public SubjectAdapter(Context context, List<String> subjectNameList) {
        this.context = context;
        this.subjectNameList = subjectNameList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(context).inflate(R.layout.subject_recycler_main_items,parent,false);
        return new SubjectViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {

        holder.subjectLabel.setText(subjectNameList.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setHasFixedSize(true);


        Query query = FirebaseDatabase.getInstance().getReference("collection").orderByChild("subject").equalTo(holder.subjectLabel.getText().toString());

        FirebaseRecyclerOptions<BookModelClass> options = new FirebaseRecyclerOptions.Builder<BookModelClass>()
                .setQuery(query,BookModelClass.class).build();


        childAdapter = new SubjectChildAdapter(options);
        childAdapter.startListening();
        childAdapter.notifyDataSetChanged();

        holder.recyclerView.setAdapter(childAdapter);



    }

    @Override
    public int getItemCount() {
        return subjectNameList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder{

        TextView subjectLabel;
        RecyclerView recyclerView;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectLabel = itemView.findViewById(R.id.subjectRowItemTextView);
            recyclerView = itemView.findViewById(R.id.subjectChildRecyclerView);
        }
    }
}
