package com.dacoders.buksue_libraryapp.CollectionAdapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dacoders.buksue_libraryapp.BookAccess.BookOptions;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.CollectionModel.Category;
import com.dacoders.buksue_libraryapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildRecyclerAdapter.myViewHolder> {
    public ChildRecyclerAdapter(List<Collection_DAO> items) {

        this.list = items;
    }


    List<Collection_DAO> list;


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_category_elements,parent,false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {


        if(list.isEmpty()){
            holder.title.setText("Empty");
        }else {
            holder.title.setText(list.get(position).getTitle());
        }

        holder.frame.setOnClickListener(view -> {

            AppCompatActivity activity  = (AppCompatActivity) view.getContext();
              Fragment bookOptions = new BookOptions(list.get(position));
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,bookOptions).addToBackStack(null).commit();


        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        View frame;
        TextView title;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.categoryElementBookTitle);
            frame = itemView.findViewById(R.id.elementFrame);




        }
    }


}
