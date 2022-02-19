package com.dacoders.buksue_libraryapp.BookAccess;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dacoders.buksue_libraryapp.Book.BookModelClass;
import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;
import com.dacoders.buksue_libraryapp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadBookFromUrlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadBookFromUrlFragment extends Fragment {


    String url;
    String bookTitle;
    String bookAuthor;
    PDFView pdfView;
    TextView pageCount;
    com.jb.dev.progress_indicator.dotGrowProgressBar progressBar;


    public ReadBookFromUrlFragment(BookModelClass book){
        this.url=book.getFileUrl();
        this.bookTitle=book.getTitle();
        this.bookAuthor=book.getAuthor();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReadBookFromUrlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReadBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadBookFromUrlFragment newInstance(String param1, String param2) {
        ReadBookFromUrlFragment fragment = new ReadBookFromUrlFragment();
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
        View view = inflater.inflate(R.layout.fragment_read_book, container, false);
         progressBar = view.findViewById(R.id.readBookProgressBar);


         pdfView = view.findViewById(R.id.pdfView);
         pageCount = view.findViewById(R.id.pageCount);
         new RetrievePdfFromUrl().execute(url);
        return view;
    }


    class RetrievePdfFromUrl extends AsyncTask<String,Void, InputStream>{


        @Override
        protected InputStream doInBackground(String... strings) {
            progressBar.setVisibility(View.VISIBLE);
            InputStream inputStream =null;


            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode()==200){
                    //response success
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                //    progressBar.setVisibility(View.INVISIBLE);

                }else{
                    Toast.makeText(getActivity(), "Error on loading file", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("input stream value: "+inputStream);
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            progressBar.setVisibility(View.INVISIBLE);
            pdfView.fromStream(inputStream) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    // allows to draw something on the current page, usually visible in the middle of the screen
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                    //  .linkHandler(DefaultLinkHandler)
                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
            .load();

           // pageCount.setText(pdfView.getCurrentPage()+" out of "+pdfView.getPageCount());








        }
    }
}