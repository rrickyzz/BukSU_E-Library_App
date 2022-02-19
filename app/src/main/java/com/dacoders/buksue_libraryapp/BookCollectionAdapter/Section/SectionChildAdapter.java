package com.dacoders.buksue_libraryapp.BookCollectionAdapter.Section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dacoders.buksue_libraryapp.Book.BookModel;
import com.dacoders.buksue_libraryapp.Book.BookModelClass;
import com.dacoders.buksue_libraryapp.BookAccess.BookOptions;
import com.dacoders.buksue_libraryapp.BookSearchHelper.NoResultFragment;
import com.dacoders.buksue_libraryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import java.util.List;

import me.grantland.widget.AutofitTextView;

public class SectionChildAdapter extends FirebaseRecyclerAdapter<BookModelClass,SectionChildAdapter.SectionChildViewHolder> {

    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SectionChildAdapter(@NonNull FirebaseRecyclerOptions<BookModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SectionChildViewHolder holder, int position, @NonNull BookModelClass model) {



        holder.title.setText(model.getTitle());
        holder.frame.setOnClickListener(click->{
            AppCompatActivity activity = (AppCompatActivity) holder.title.getContext();
            Fragment frag = new BookOptions(model);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
     });

    }

    @NonNull
    @Override
    public SectionChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_child_recycler_items,parent,false);
        return new SectionChildViewHolder(view);
    }


    public static class SectionChildViewHolder extends RecyclerView.ViewHolder{

        View frame;
        AutofitTextView title;

        public SectionChildViewHolder(@NonNull View itemView) {
            super(itemView);

            frame = itemView.findViewById(R.id.sectionChildElementFrame);
            title = itemView.findViewById(R.id.sectionItemBookTitleTextView);
        }
    }

}
