package com.example.newentryclear.ui.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.newentryclear.Preferences;
import com.example.newentryclear.R;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    Button openPreferences;
    String newText;
    TextView usuario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        openPreferences = root.findViewById(R.id.preferenceOpen);
        usuario = root.findViewById(R.id.actualUser);
        SharedPreferences prefs = getContext().getSharedPreferences("com.example.newentry", Context.MODE_PRIVATE);
        newText = prefs.getString("ActiveUser" , "no usuario conectado");
        usuario.setText(String.format("Preferencias del usuario %s", newText));
        openPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.preferenceOpen:
                        startActivity(new Intent(getContext(), Preferences.class));
                }
            }
        });
        return root;


    }
}