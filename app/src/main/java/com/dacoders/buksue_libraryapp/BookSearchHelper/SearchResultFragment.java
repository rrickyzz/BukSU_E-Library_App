package com.dacoders.buksue_libraryapp.BookSearchHelper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.Book.BookModelClass;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView textView;
    String searchKey;

    public String getSearchKey() {
        return searchKey;
    }

    SearchResultRecyclerAdapter adapter;
    private RecyclerView recyclerView;


    public SearchResultFragment(String searchKey) {
        this.searchKey = searchKey;
    }

    public SearchResultFragment(int contentLayoutId, String searchKey) {
        super(contentLayoutId);
        this.searchKey = searchKey;
    }

    public SearchResultFragment(){

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(String param1, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        textView = view.findViewById(R.id.searchResultKey);

        recyclerView = view.findViewById(R.id.searchResultRecyclerView);

        searchBook(searchKey);




        return view;
    }


    private void searchBook(CharSequence text) {


        // we will get the default FirebaseDatabase instance
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        String txt = String.valueOf(text);
        DatabaseReference collectionRef = databaseReference.child("collection");

       Query query = collectionRef.orderByChild("title").startAt(txt).endAt(txt+"~");
        Query query2 = collectionRef.orderByChild("author").startAt(txt).endAt(txt+"~");
        Query query3 = collectionRef.orderByChild("isbn").equalTo(txt);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                   // Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();

                }else{

                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    Fragment noResultFragment = new NoResultFragment(searchKey);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, noResultFragment).commit();
                    return;



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<BookModelClass> options = new FirebaseRecyclerOptions.Builder<BookModelClass>()
                .setQuery(query,BookModelClass.class).build();










        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter= new SearchResultRecyclerAdapter(options);





        adapter.startListening();


        recyclerView.setAdapter(adapter);


        adapter.notifyDataSetChanged();




        textView.setText("Search result(s) of "+"\" "+searchKey+"\" "+" :");






    }

    @Override
    public void onStart() {

        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {

        super.onStop();
        adapter.stopListening();
    }
}