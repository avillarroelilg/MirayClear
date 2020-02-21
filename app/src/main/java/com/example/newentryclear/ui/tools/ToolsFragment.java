package com.example.newentryclear.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.newentryclear.MainActivity;
import com.example.newentryclear.Preferences;
import com.example.newentryclear.R;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

      toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        startActivity(new Intent(getContext(), Preferences.class));

        //getFragmentManager().beginTransaction() .replace(android.R.id.content, new Preferences()) .commit();
        return root;
    }
}