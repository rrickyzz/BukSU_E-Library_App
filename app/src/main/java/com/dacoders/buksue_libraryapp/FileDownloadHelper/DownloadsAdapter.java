package com.dacoders.buksue_libraryapp.FileDownloadHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dacoders.buksue_libraryapp.BookAccess.ReadBookFromStorageFragment;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.HomeActivity;
import com.dacoders.buksue_libraryapp.R;
import com.dacoders.buksue_libraryapp.TabFragments.DownloadFragment;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.droidsonroids.gif.GifImageView;

public class DownloadsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    final int VIEW_TYPE_CURRENT_DOWNLOADS = 0 ;
    final int VIEW_TYPE_OLD_DOWNLOADS = 1 ;



    Context context;
    List<Collection_DAO> downloadList;
    File[] filesList ;

    public DownloadsAdapter(Context context, List<Collection_DAO> downloadList, File[] filesList) {
        this.context = context;
        this.downloadList = downloadList;
        this.filesList = filesList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if(downloadList!=null) {

            if (viewType == VIEW_TYPE_CURRENT_DOWNLOADS) {
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_recycler_items, parent, false);
                return new CurrentDownloadsHolder(v1);
            }

        }

        if(viewType == VIEW_TYPE_OLD_DOWNLOADS){
            View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.old_download_recycler_items,parent,false);
            return new OldDownloadsHolder(v2);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof CurrentDownloadsHolder){

            ((CurrentDownloadsHolder) holder).currentDownloadFileName.setText(downloadList.get(position).getFileName());

            PRDownloaderConfig config  = PRDownloaderConfig.newBuilder().setDatabaseEnabled(true).build();

            PRDownloader.initialize(((CurrentDownloadsHolder) holder).currentDownloadFileName.getContext(),config);

           String url = downloadList.get(position).getFileUrl();
            String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            String fileName = downloadList.get(position).getFileName() ;
           File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/BukSU-ELibrary/");
             dir.mkdirs();

             //check if file is already downloaded.














            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/BukSU-ELibrary/");


            File[] files= dir.listFiles();
            int counter = 0;
             boolean isFileExistingAlready = false;



            for(File f: files){
                if(f.getName().equals(fileName)) {

                    //updates its file name with post fix depending on how often it exist in
                    isFileExistingAlready = true;
                     counter++;


                }






            }

            if(isFileExistingAlready){
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/BukSU-ELibrary/");
                fileName = "("+counter+")"+fileName;

            }

















            int downloadId = PRDownloader.download(url,file.getAbsolutePath(),fileName).build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                            ((CurrentDownloadsHolder) holder).gifIcon.setVisibility(View.VISIBLE);
                            ((CurrentDownloadsHolder) holder).downloadedFileIcon.setVisibility(View.INVISIBLE);

                            ((CurrentDownloadsHolder) holder).downloadingPercentage.setVisibility(View.VISIBLE);
                            ((CurrentDownloadsHolder) holder).progressBar.setVisibility(View.VISIBLE);





                        }
                    }).setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                            ((CurrentDownloadsHolder) holder).gifIcon.setVisibility(View.INVISIBLE);

                        }
                    }).setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {


                            downloadList.remove(holder.getAbsoluteAdapterPosition());
                            notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                            notifyItemRangeChanged(holder.getAbsoluteAdapterPosition(),downloadList.size()+filesList.length);

                            // refresh fragment after each download
                            HomeActivity activity = (HomeActivity) context;

                            Fragment dl_fragment = new DownloadFragment();

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dl_fragment).addToBackStack(null).commit();






                        }
                    }).setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                            long progressPercent = progress.currentBytes*100/progress.totalBytes;


                            ((CurrentDownloadsHolder) holder).progressBar.setProgress((int)progressPercent);
                            ((CurrentDownloadsHolder) holder).progressBar.setIndeterminate(false);
                            ((CurrentDownloadsHolder) holder).downloadingPercentage.setText(Integer.valueOf((int) progressPercent)+" % ");


                        }
                    }).start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            ((CurrentDownloadsHolder) holder).gifIcon.setVisibility(View.INVISIBLE);
                            ((CurrentDownloadsHolder) holder).downloadedFileIcon.setVisibility(View.VISIBLE);
                            ((CurrentDownloadsHolder) holder).downloadingPercentage.setVisibility(View.INVISIBLE);
                            ((CurrentDownloadsHolder) holder).progressBar.setVisibility(View.INVISIBLE);
                            ((CurrentDownloadsHolder) holder).downloadCancel.setVisibility(View.GONE);


                            downloadList.remove(holder.getAbsoluteAdapterPosition());
                            notifyDataSetChanged();
                            notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                            notifyItemRangeChanged(holder.getAbsoluteAdapterPosition(),downloadList.size()+filesList.length);


                            // refresh fragment after each download
                            HomeActivity activity = (HomeActivity) context;

                             Fragment dl_fragment = new DownloadFragment();

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dl_fragment).addToBackStack(null).commit();









                        }

                        @Override
                        public void onError(Error error) {
                            ((CurrentDownloadsHolder) holder).gifIcon.setVisibility(View.INVISIBLE);


                        }
                    });





            ((CurrentDownloadsHolder) holder).downloadCancel.setOnClickListener(click->{
                PRDownloader.cancel(downloadId);
            });








        }





        if(holder instanceof OldDownloadsHolder){
            String filename="";
            int iterator = 0 ;

            for(File f : filesList){

                if(iterator == position){

                   filename =  f.getName();
                }

                iterator++;

            }


            ((OldDownloadsHolder) holder).fileName.setText(filename);
            String finalFilename = filename;
            ((OldDownloadsHolder) holder).options.setOnClickListener(click->
            {
                PopupMenu popupMenu = new PopupMenu(((OldDownloadsHolder) holder).options.getContext(),click);
                popupMenu.inflate(R.menu.download_item_context_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {



                        switch (menuItem.getItemId()){
                            case R.id.DeleteDownload:

                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE);
                                sweetAlertDialog.setContentText("Are you sure you want to delete this file?");
                                sweetAlertDialog.setConfirmText("Delete");
                                sweetAlertDialog.setCancelText("Cancel");
                                sweetAlertDialog.show();


                                sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setTextColor(R.color.textSecondary);
                                sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setTextColor(R.color.textSecondary);
                                sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        //delete file from downloads


                                        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/BukSU-ELibrary/");
                                        File file = new File(dir,finalFilename);
                                        file.delete();
                                        sweetAlertDialog.dismiss();




                                        // refresh fragment after each file deleted
                                        HomeActivity activity = (HomeActivity) context;

                                        Fragment dl_fragment = new DownloadFragment();

                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dl_fragment).addToBackStack(null).commit();


                                    }});






                        }

                        return false;
                    }
                });

            });



            ((OldDownloadsHolder) holder).frame.setOnClickListener(click->{

                int i=0;

                for(File f : filesList){

                    if(i == position){


                        Fragment readFromStoragefragment = new ReadBookFromStorageFragment(f);

                        HomeActivity activity = (HomeActivity) context;

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,readFromStoragefragment).addToBackStack(null).commit();




                    }

                  i++;
                }




            });



        }

    }



    @Override
    public int getItemCount() {

        if(downloadList==null){

            return filesList.length;


        }

        return downloadList.size()+filesList.length;


    }


    @Override
    public int getItemViewType(int position) {

        if(downloadList!=null){

        if(position < downloadList.size()){
            return VIEW_TYPE_CURRENT_DOWNLOADS;
        }

        if(position - downloadList.size() < filesList.length){
            return VIEW_TYPE_OLD_DOWNLOADS;
        }
        }
        return -1;
    }

    public  class  CurrentDownloadsHolder extends RecyclerView.ViewHolder{

        GifImageView gifIcon;
        ImageView downloadedFileIcon,downloadCancel;
        TextView currentDownloadFileName,downloadingPercentage;
        ProgressBar progressBar;

        public CurrentDownloadsHolder(@NonNull View itemView) {


                super(itemView);

            if (downloadList != null) {
                gifIcon = itemView.findViewById(R.id.downloadingGif);
                downloadedFileIcon = itemView.findViewById(R.id.downloadedFileIcon);
                downloadCancel = itemView.findViewById(R.id.downloadCancel);
                currentDownloadFileName = itemView.findViewById(R.id.CurrentDownloadFileName);
                progressBar = itemView.findViewById(R.id.progressBar);
                downloadingPercentage = itemView.findViewById(R.id.downloadingPercent);
            }
        }
    }


    public class OldDownloadsHolder extends RecyclerView.ViewHolder{


        FrameLayout frame;
        TextView fileName;
        ImageView options;

        public OldDownloadsHolder(@NonNull View itemView) {
            super(itemView);


            fileName = itemView.findViewById(R.id.OLdDownloadedFileFileName);
            options = itemView.findViewById(R.id.OldDownloadedFileOptionsMenu);
            frame = itemView.findViewById(R.id.downloadItemFrame);
        }


    }
}
