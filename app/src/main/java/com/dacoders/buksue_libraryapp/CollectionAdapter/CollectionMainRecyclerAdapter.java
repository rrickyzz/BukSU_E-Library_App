package com.dacoders.buksue_libraryapp.CollectionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.CollectionModel.Category;
import com.dacoders.buksue_libraryapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.itemanimator.SlideInOutRightItemAnimator;

public class CollectionMainRecyclerAdapter extends RecyclerView.Adapter<CollectionMainRecyclerAdapter.MainViewHolder> {



    private Context context;
    private List<Category> allCategoryList;
    private List<Collection_DAO> bookList;
    private List<Collection_DAO> list = new ArrayList<>();



    public CollectionMainRecyclerAdapter(Context context, List<Category> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
        this.bookList = new ArrayList<>();


    }

    @NonNull
    @Override
    public CollectionMainRecyclerAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.collection_category_row_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull CollectionMainRecyclerAdapter.MainViewHolder holder, int position) {

        holder.categoryTitle.setText(allCategoryList.get(position).getCategoryName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.itemRecyclerView.setLayoutManager(layoutManager);
        holder.itemRecyclerView.setHasFixedSize(true);
        //   SetHorizontalRecycler(holder.itemRecyclerView,allCategoryList);


        readBookList(new BookListCallBack() {



            @Override
            public void onCallBack(List<Collection_DAO> bookList) {


                List<Collection_DAO> filterList = new ArrayList<>();



                for(Collection_DAO item : bookList){

                    System.out.println(holder.categoryTitle.getText());
                    System.out.println(item.getCategory());
                    if(holder.categoryTitle.getText().equals(item.getCategory())){
                        filterList.add(item);

                    }
                    ChildRecyclerAdapter childRecyclerAdapter = new ChildRecyclerAdapter(filterList);
                    holder.itemRecyclerView.setAdapter(childRecyclerAdapter);
                    holder.itemRecyclerView.setItemAnimator(new SlideInOutRightItemAnimator(holder.itemRecyclerView));
                    childRecyclerAdapter.notifyDataSetChanged();


                }



            }


        });














    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }


    public static final class MainViewHolder extends RecyclerView.ViewHolder{

        TextView categoryTitle;
        RecyclerView itemRecyclerView;

        public MainViewHolder(@NonNull View itemView) {

            super(itemView);

            categoryTitle = itemView.findViewById(R.id.collectionCategoryTitle);
            itemRecyclerView = itemView.findViewById(R.id.childRecyclerView);



        }
    }




    private void readBookList(BookListCallBack bookListCallBack){

        // we will get the default FirebaseDatabase instance
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        DatabaseReference collectionRef = databaseReference.child("collection");

        //bookList = new ArrayList<>();


        collectionRef.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Collection_DAO obj = dataSnapshot.getValue(Collection_DAO.class);

                        list.add(obj);

                        bookListCallBack.onCallBack(list);


                }
                list.clear();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());

            }
        }));





    }


    private interface BookListCallBack{
        void onCallBack(List<Collection_DAO> bookList);

    }






}
