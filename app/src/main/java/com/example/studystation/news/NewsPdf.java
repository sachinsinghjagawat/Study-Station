package com.example.studystation.news;

import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.studystation.R;
import com.example.studystation.studentNotes.PdfOpen;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsPdf extends Fragment {

    private NewsPdfViewModel mViewModel;
    public PDFView pdfView;
    ProgressBar progressBar;
    String urlOfPdf;


    public static NewsPdf newInstance(String url) {
        NewsPdf fragment = new NewsPdf();
        Bundle args = new Bundle();
        args.putString("url" , url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_pdf_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pdfView = (PDFView) view.findViewById(R.id.pdfView2);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);

        if (getArguments() != null) {
            urlOfPdf = getArguments().getString("url");
        }

        new RetrivePdfStream().execute(urlOfPdf);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewsPdfViewModel.class);
        // TODO: Use the ViewModel
    }

    class RetrivePdfStream extends AsyncTask<String , Void , InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;

            try {
                URL url = new URL (strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return inputStream;

        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            progressBar.setVisibility(View.GONE);
                        }
                    })
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
                    .load();
        }
    }


}