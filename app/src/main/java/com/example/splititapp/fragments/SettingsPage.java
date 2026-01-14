package com.example.splititapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.splititapp.HistoryActivity;
import com.example.splititapp.R;

public class SettingsPage extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settingspage, container, false);

        // Find the History button by the ID we added to the XML
        Button btnHistory = view.findViewById(R.id.btnHistory);

        btnHistory.setOnClickListener(v -> {
            // This opens the HistoryActivity (we will create this file next)
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        });

        return view;
    }
}