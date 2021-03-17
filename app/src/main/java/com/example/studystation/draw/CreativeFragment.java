package com.example.studystation.draw;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.studystation.R;

public class CreativeFragment extends Fragment {

    private CreativeViewModel mViewModel;
    LinearLayout drawView;

    public static CreativeFragment newInstance() {
        return new CreativeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.creative_fragment, container, false);
        drawView = rootView.findViewById(R.id.drawFragment);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyCanvas myCanvas = new MyCanvas(CreativeFragment.this.getContext());
        drawView.addView(myCanvas);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreativeViewModel.class);
        // TODO: Use the ViewModel
    }

}