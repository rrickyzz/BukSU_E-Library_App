package com.dacoders.buksue_libraryapp.BookCollectionAdapter.Location;

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
import com.dacoders.buksue_libraryapp.BookCollectionAdapter.Section.SectionChildAdapter;
import com.dacoders.buksue_libraryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    Context context;
    List<String> locationNameList;
    LocationChildAdapter childAdapter;

    public LocationAdapter(Context context, List<String> locationNameList) {
        this.context = context;
        this.locationNameList = locationNameList;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(context).inflate(R.layout.location_recycler_main_items,parent,false);
        return new LocationViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {

        holder.locationLabel.setText(locationNameList.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setHasFixedSize(true);


        Query query = FirebaseDatabase.getInstance().getReference("collection").orderByChild("location").equalTo(holder.locationLabel.getText().toString().replaceAll("\\s",""));

        FirebaseRecyclerOptions<BookModelClass> options = new FirebaseRecyclerOptions.Builder<BookModelClass>()
                .setQuery(query,BookModelClass.class).build();

        childAdapter = new LocationChildAdapter(options);
        childAdapter.startListening();
        childAdapter.notifyDataSetChanged();

        holder.recyclerView.setAdapter(childAdapter);

    }

    @Override
    public int getItemCount() {
        return locationNameList.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder{

        TextView locationLabel;
        RecyclerView recyclerView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.locationChildRecyclerView);
            locationLabel = itemView.findViewById(R.id.locationRowItemTextView);

        }
    }




}
