package com.example.studystation.motivation;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.studystation.R;

public class WebView extends Fragment {

    private WebViewViewModel mViewModel;
    android.webkit.WebView webView;
    ProgressBar progressBar;
    String url;

    public static WebView newInstance(String url) {
        WebView fragment = new WebView();
        Bundle args = new Bundle();
        args.putString("url" , url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.web_view_fragment, container, false);
        webView = (android.webkit.WebView) rootView.findViewById(R.id.webViewForTextContent);
        progressBar = rootView.findViewById(R.id.progressBarWebView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });


        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
        webView.loadUrl(url);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WebViewViewModel.class);
        // TODO: Use the ViewModel
    }


}