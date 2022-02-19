package com.dacoders.buksue_libraryapp.TabFragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.FileDownloadHelper.DownloadService;
import com.dacoders.buksue_libraryapp.FileDownloadHelper.DownloadsAdapter;
import com.dacoders.buksue_libraryapp.FileDownloadHelper.NoDownloadsFragment;
import com.dacoders.buksue_libraryapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadFragment extends Fragment {

    DownloadService service;
    List<Collection_DAO> dlList;
    File[] fileList;
    RecyclerView recyclerView;
    private boolean isBound = false;
    TextView textView;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) iBinder;
            service = binder.getDownloadService();
            isBound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            isBound = false;


        }
    };

    public DownloadFragment(List<Collection_DAO> dlList) {
        this.dlList = dlList;



    }



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DownloadFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadFragment newInstance(String param1, String param2) {
        DownloadFragment fragment = new DownloadFragment();
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


        Intent intent = new Intent(getActivity(), DownloadService.class);

        getActivity().bindService(intent,connection,Context.BIND_AUTO_CREATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        recyclerView = view.findViewById(R.id.downloadsRecyclerView);
        textView = view.findViewById(R.id.downloadLabel);
        fileList = getAllDownloadedFiles();

        //go to 'no downloads' fragment if there is no downloads yet


        if(fileList.length==0&&dlList==null){
            AppCompatActivity activity  = (AppCompatActivity) getContext();
           Fragment nodl_fragment = new NoDownloadsFragment();

            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,nodl_fragment).addToBackStack(null).commit();




        }

        for(File o : fileList){
            System.out.println("file name in dir:"+o.getName()+"path: "+o.getAbsolutePath());
        }
        initDownloadAdapter();



        return  view;
    }

    private void initDownloadAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(dlList== null){
            dlList = new ArrayList<>();
        }
        DownloadsAdapter downloadRecyclerAdapter = new DownloadsAdapter(getActivity(),dlList,fileList);
        recyclerView.setAdapter(downloadRecyclerAdapter);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private File[] getAllDownloadedFiles(){
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/BukSU-ELibrary/");
        dir.mkdirs();

        return dir.listFiles();

    }


}